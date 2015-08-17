/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.mod.ModCntManPack;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformHorseArmor
        implements IClassTransformer
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.passive.EntityHorse") ) {
			return transformHorse(bytes);
		}

		return bytes;
	}

    private static byte[] transformHorse(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);

		MethodNode method = injectMethodSetCustomArmorItem();
		clazz.methods.add(method);

		method = injectMethodGetCustomArmorItem();
		clazz.methods.add(method);

        transformInteract(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_INTERACT));
        transformIsValidArmor(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_FUNC146085A));
        transformEntityInit(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_ENTITY_INIT));
        transformUpdateHorseSlots(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_FUNC110232CE));
        transformOnInvChanged(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_ON_INV_CHANGED));
        transformGetTotalArmorValue(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_GET_TOTAL_ARMOR_VAL));

        try {
            transformArmorTexture(ASMNames.findObfMethod(clazz, ASMNames.MDO_HORSE_SET_TEXTURE_PATH));
        } catch( ASMHelper.MethodNotFoundException e ) {
            ModCntManPack.MOD_LOG.log(Level.INFO, "Running on dedicated server, no need to transform Method setHorseTexturePaths!");
        }

	    bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

		return bytes;
	}

    private static MethodNode injectMethodGetCustomArmorItem() {
        MethodNode method = ASMNames.getNewMethod(Opcodes.ACC_PRIVATE, ASMNames.MD_SAP_GET_CUSTOM_ARMOR_ITEM_NEW);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(Opcodes.ALOAD, 0);
        ASMNames.visitObfFieldInsn(method, Opcodes.GETFIELD, ASMNames.FDO_ENTITY_DATAWATCHER, ASMNames.CL_ENTITY_HORSE);
        method.visitIntInsn(Opcodes.BIPUSH, 23);
        ASMNames.visitObfMethodInsn(method, Opcodes.INVOKEVIRTUAL, ASMNames.MDO_DATAWATCHER_GET_OBJ_STACK, false);
        method.visitInsn(Opcodes.ARETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", ASMNames.CL_T_ENTITY_HORSE, null, l0, l1, 0);
        method.visitMaxs(2, 1);
        method.visitEnd();

        return method;
    }

    private static MethodNode injectMethodSetCustomArmorItem() {
        MethodNode method = ASMNames.getNewMethod(Opcodes.ACC_PRIVATE, ASMNames.MD_SAP_SET_CUSTOM_ARMOR_ITEM_NEW);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(Opcodes.ALOAD, 1);
        Label l1 = new Label();
        method.visitJumpInsn(Opcodes.IFNONNULL, l1);
        method.visitTypeInsn(Opcodes.NEW, ASMNames.CL_ITEM_STACK);
        method.visitInsn(Opcodes.DUP);
        ASMNames.visitObfFieldInsn(method, Opcodes.GETSTATIC, ASMNames.FDO_ITEMS_IRON_SHOVEL);
        method.visitInsn(Opcodes.ICONST_0);
        ASMNames.visitMethodInsn(method, Opcodes.INVOKESPECIAL, ASMNames.MD_ITEMSTACK_INIT, false);
        method.visitVarInsn(Opcodes.ASTORE, 1);
        method.visitLabel(l1);
        method.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        method.visitVarInsn(Opcodes.ALOAD, 0);
        ASMNames.visitObfFieldInsn(method, Opcodes.GETFIELD, ASMNames.FDO_ENTITY_DATAWATCHER, ASMNames.CL_ENTITY_HORSE);
        method.visitIntInsn(Opcodes.BIPUSH, 23);
        method.visitVarInsn(Opcodes.ALOAD, 1);
        ASMNames.visitObfMethodInsn(method, Opcodes.INVOKEVIRTUAL, ASMNames.MDO_DATAWATCHER_UPDATE_OBJ, false);
        Label l3 = new Label();
        method.visitLabel(l3);
        method.visitInsn(Opcodes.RETURN);
        Label l4 = new Label();
        method.visitLabel(l4);
        method.visitLocalVariable("this", ASMNames.CL_T_ENTITY_HORSE, null, l0, l3, 0);
        method.visitLocalVariable("stack", ASMNames.CL_T_ITEM_STACK, null, l0, l3, 1);
        method.visitMaxs(5, 2);
        method.visitEnd();

        return method;
    }

    private static void transformGetTotalArmorValue(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FDO_HORSE_ARMOR_VALUES));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_HORSE_FUNC110241CB, false));

        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();

        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_SAP_GET_CUSTOM_ARMOR_ITEM, false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 1));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, ASMNames.CL_ITEM_HORSE_ARMOR));
        LabelNode l2 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l2));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
        newInstr.add(new TypeInsnNode(Opcodes.CHECKCAST, ASMNames.CL_ITEM_HORSE_ARMOR));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_GET_ARMOR_VALUE, false));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l2);

        method.instructions.insertBefore(pointer, newInstr);
    }

    private static void transformOnInvChanged(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_HORSE_IS_SADDLED, false));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 3));

        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_SAP_GET_CUSTOM_ARMOR_ITEM, false));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));

        method.instructions.insert(pointer, newInstr);

        needle = new InsnList();
        needle.add(new LdcInsnNode("mob.horse.armor"));
        needle.add(new LdcInsnNode(0.5F));
        needle.add(new InsnNode(Opcodes.FCONST_1));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ENTITY_PLAY_SOUND, ASMNames.CL_ENTITY_HORSE, false));
        needle.add(new LabelNode());

        pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
        newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FDO_ITEMS_IRON_SHOVEL));
        LabelNode l9 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IF_ACMPNE, l9));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_SAP_GET_CUSTOM_ARMOR_ITEM, false));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_IS_ITEM_EQUAL, false));
        newInstr.add(new JumpInsnNode(Opcodes.IFNE, l9));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new LdcInsnNode("mob.horse.armor"));
        newInstr.add(new LdcInsnNode(0.5F));
        newInstr.add(new InsnNode(Opcodes.FCONST_1));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ENTITY_PLAY_SOUND, ASMNames.CL_ENTITY_HORSE, false));
        newInstr.add(l9);

        method.instructions.insert(pointer, newInstr);
    }

	private static void transformArmorTexture(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_HORSE_FUNC110241CB, false));
	    needle.add(new VarInsnNode(Opcodes.ISTORE, 3));

	    AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
	    LabelNode l17 = new LabelNode();
	    newInstr.add(l17);
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_SAP_GET_CUSTOM_ARMOR_ITEM, false));
	    newInstr.add(new VarInsnNode(Opcodes.ASTORE, 4));
	    LabelNode l18 = new LabelNode();
	    newInstr.add(l18);
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
	    newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
	    newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, ASMNames.CL_ITEM_HORSE_ARMOR));
	    LabelNode l19 = new LabelNode();
	    newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l19));
	    LabelNode l20 = new LabelNode();
	    newInstr.add(l20);
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_HORSE_FIELD110280BR));
	    newInstr.add(new InsnNode(Opcodes.ICONST_2));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
	    newInstr.add(new TypeInsnNode(Opcodes.CHECKCAST, ASMNames.CL_ITEM_HORSE_ARMOR));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_GET_ARMOR_TEXTURE, false));
	    newInstr.add(new InsnNode(Opcodes.AASTORE));
	    LabelNode l21 = new LabelNode();
	    newInstr.add(l21);
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(new TypeInsnNode(Opcodes.NEW, ASMNames.CL_STRING_BUILDER));
	    newInstr.add(new InsnNode(Opcodes.DUP));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_HORSE_FIELD110286BQ));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_STRING_VALUE_OF, false));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_STRINGBUILDER_INIT, false));
	    newInstr.add(new LdcInsnNode("cst-"));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_STRINGBUILDER_APPEND, false));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 4));
	    newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_UNLOC_NAME, false));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_STRINGBUILDER_APPEND, false));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_STRINGBUILDER_TO_STRING, false));
        newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.PUTFIELD, ASMNames.FDO_HORSE_FIELD110286BQ));
	    newInstr.add(new InsnNode(Opcodes.RETURN));
	    newInstr.add(l19);

	    method.instructions.insert(node, newInstr);
	}

	private static void transformUpdateHorseSlots(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_HORSE_CHEST));
	    needle.add(new InsnNode(Opcodes.ICONST_1));

	    AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_HORSE_CHEST));
	    newInstr.add(new InsnNode(Opcodes.ICONST_1));
	    newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_INVBASIC_GET_STACK_IN_SLOT, ASMNames.CL_ANIMAL_CHEST, false));
	    newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_SAP_SET_CUSTOM_ARMOR_ITEM, false));
	    newInstr.add(new LabelNode());

	    method.instructions.insertBefore(pointer, newInstr);
	}

	private static void transformEntityInit(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
//        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_ENTITY_DATAWATCHER, ASMNames.CL_ENTITY_HORSE));
        needle.add(new IntInsnNode(Opcodes.BIPUSH, 22));
        needle.add(new InsnNode(Opcodes.ICONST_0));
//        needle.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false));
        needle.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_INTEGER_VALUE_OF, false));
//        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_addObject, "(ILjava/lang/Object;)V", false));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_DATAWATCHER_ADD_OBJECT, false));

        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
//        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_ENTITY_DATAWATCHER, ASMNames.CL_ENTITY_HORSE));
        newInstr.add(new IntInsnNode(Opcodes.BIPUSH, 23));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, ASMNames.CL_ITEM_STACK));
        newInstr.add(new InsnNode(Opcodes.DUP));
//        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", ASMNames.F_ironShovel, "Lnet/minecraft/item/Item;"));
        newInstr.add(ASMNames.getObfFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FDO_ITEMS_IRON_SHOVEL));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
//        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V", false));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_ITEMSTACK_INIT, false));
//        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_addObject, "(ILjava/lang/Object;)V", false));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_DATAWATCHER_ADD_OBJECT, false));

        method.instructions.insert(pointer, newInstr);
	}

	private static void transformIsValidArmor(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
//	    needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", ASMNames.F_iron_horse_armor, "Lnet/minecraft/item/Item;"));
	    needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FDO_ITEMS_HORSE_ARMOR_IRON));
	    needle.add(new JumpInsnNode(Opcodes.IF_ACMPEQ, new LabelNode()));

	    AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
//        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, "de/sanandrew/core/manpack/item/AItemHorseArmor"));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, ASMNames.CL_ITEM_HORSE_ARMOR));
        LabelNode ln = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, ln));
        newInstr.add(new LabelNode());
        newInstr.add(new InsnNode(Opcodes.ICONST_1));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(ln);
        newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

	    method.instructions.insertBefore(node, newInstr);
	}

	private static void transformInteract(MethodNode method) {
	    InsnList needle = new InsnList();
//	    needle.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraft/init/Items", ASMNames.F_diamondHorseArmor, "Lnet/minecraft/item/Item;"));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FDO_ITEMS_HORSE_ARMOR_DIAMOND));
	    needle.add(new JumpInsnNode(Opcodes.IF_ACMPNE, new LabelNode()));
	    needle.add(new LabelNode());
	    needle.add(new LineNumberNode(801, new LabelNode()));
	    needle.add(new InsnNode(Opcodes.ICONST_3));
	    needle.add(new VarInsnNode(Opcodes.ISTORE, 4));
	    needle.add(new LabelNode());
	    needle.add(new LineNumberNode(804, new LabelNode()));
	    needle.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

	    AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
	    newInstr.add(new VarInsnNode(Opcodes.ALOAD, 2));
//        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;", false));
        newInstr.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ITEMSTACK_GET_ITEM, false));
        newInstr.add(new TypeInsnNode(Opcodes.INSTANCEOF, ASMNames.CL_ITEM_HORSE_ARMOR));
        LabelNode l8 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l8));
        newInstr.add(new InsnNode(Opcodes.ICONST_4));
        newInstr.add(new VarInsnNode(Opcodes.ISTORE, 4));
        newInstr.add(l8);
        newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

	    method.instructions.insert(node, newInstr);
	}
}
