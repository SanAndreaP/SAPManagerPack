package de.sanandrew.core.manpack.transformer;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

import cpw.mods.fml.common.FMLLog;

import de.sanandrew.core.manpack.mod.ModCntManPack;

public class TransformHorseArmor implements IClassTransformer, Opcodes
{

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.passive.EntityHorse") ) {
			return this.transformHorse(bytes);
		}

		return bytes;
	}

    private byte[] transformHorse(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);

		MethodNode method = this.injectMethodSCAI();
		clazz.methods.add(method);

		method = this.injectMethodGCAI();
		clazz.methods.add(method);

		this.transformInteract(ASMHelper.findMethod(ASMNames.M_interact, "(Lnet/minecraft/entity/player/EntityPlayer;)Z", clazz));
        this.transformIsValidArmor(ASMHelper.findMethod(ASMNames.M_func146085a, "(Lnet/minecraft/item/Item;)Z", clazz));
        this.transformEntityInit(ASMHelper.findMethod(ASMNames.M_entityInit, "()V", clazz));
        this.transformUpdateHorseSlots(ASMHelper.findMethod(ASMNames.M_func110232cE, "()V", clazz));
        this.transformOnInvChanged(ASMHelper.findMethod(ASMNames.M_onInventoryChanged, "(Lnet/minecraft/inventory/InventoryBasic;)V", clazz));
        this.transformGetTotalArmorValue(ASMHelper.findMethod(ASMNames.M_getTotalArmorValue, "()I", clazz));

        try {
            this.transformArmorTexture(ASMHelper.findMethod(ASMNames.M_setHorseTexturePaths, "()V", clazz));
        } catch( ASMHelper.MethodNotFoundException e ) {
            FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "Running on dedicated server, no need to transform Method %s!", ASMNames.M_setHorseTexturePaths);
        }

	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		return bytes;
	}

    private MethodNode injectMethodGCAI() {
        MethodNode method = new MethodNode(ACC_PRIVATE, "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(ALOAD, 0);
        method.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        method.visitIntInsn(BIPUSH, 23);
        method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_getWatchableObjectIS, "(I)Lnet/minecraft/item/ItemStack;");
        method.visitInsn(ARETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l1, 0);
        method.visitMaxs(2, 1);
        method.visitEnd();

        return method;
    }

    private MethodNode injectMethodSCAI() {
        MethodNode method = new MethodNode(ACC_PRIVATE, "_SAP_setCustomArmorItem", "(Lnet/minecraft/item/ItemStack;)V", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(ALOAD, 1);
        Label l1 = new Label();
        method.visitJumpInsn(IFNONNULL, l1);
        method.visitTypeInsn(NEW, "net/minecraft/item/ItemStack");
        method.visitInsn(DUP);
        method.visitFieldInsn(GETSTATIC, "net/minecraft/init/Items", ASMNames.F_ironShovel, "Lnet/minecraft/item/Item;");
        method.visitInsn(ICONST_0);
        method.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V");
        method.visitVarInsn(ASTORE, 1);
        method.visitLabel(l1);
        method.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        method.visitVarInsn(ALOAD, 0);
        method.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        method.visitIntInsn(BIPUSH, 23);
        method.visitVarInsn(ALOAD, 1);
        method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_updateObject, "(ILjava/lang/Object;)V");
        Label l3 = new Label();
        method.visitLabel(l3);
        method.visitInsn(RETURN);
        Label l4 = new Label();
        method.visitLabel(l4);
        method.visitLocalVariable("this", "Lnet/minecraft/entity/passive/EntityHorse;", null, l0, l3, 0);
        method.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l3, 1);
        method.visitMaxs(5, 2);
        method.visitEnd();

        return method;
    }

    private void transformGetTotalArmorValue(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_armorValues, "[I"));
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMNames.M_func110241cb, "()I"));

        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();

        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
        newInstr.add(new VarInsnNode(ASTORE, 1));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new TypeInsnNode(INSTANCEOF, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
        LabelNode l2 = new LabelNode();
        newInstr.add(new JumpInsnNode(IFEQ, l2));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new TypeInsnNode(CHECKCAST, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/ItemHorseArmor", "getArmorValue", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I"));
        newInstr.add(new InsnNode(IRETURN));
        newInstr.add(l2);

        method.instructions.insertBefore(pointer, newInstr);
    }

    private void transformOnInvChanged(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMNames.M_isHorseSaddled, "()Z"));
        needle.add(new VarInsnNode(ISTORE, 3));

        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();

        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
        newInstr.add(new VarInsnNode(ASTORE, 4));

        method.instructions.insert(pointer, newInstr);

        needle = new InsnList();
        needle.add(new LdcInsnNode("mob.horse.armor"));
        needle.add(new LdcInsnNode(new Float("0.5")));
        needle.add(new InsnNode(FCONST_1));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMNames.M_playSound, "(Ljava/lang/String;FF)V"));
        needle.add(new LabelNode());

        pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        newInstr = new InsnList();
        newInstr.add(new VarInsnNode(ALOAD, 4));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Items", ASMNames.F_ironShovel, "Lnet/minecraft/item/Item;"));
        LabelNode l9 = new LabelNode();
        newInstr.add(new JumpInsnNode(IF_ACMPNE, l9));
        newInstr.add(new VarInsnNode(ALOAD, 4));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_isItemEqual, "(Lnet/minecraft/item/ItemStack;)Z"));
        newInstr.add(new JumpInsnNode(IFNE, l9));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new LdcInsnNode("mob.horse.armor"));
        newInstr.add(new LdcInsnNode(new Float("0.5")));
        newInstr.add(new InsnNode(FCONST_1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMNames.M_playSound, "(Ljava/lang/String;FF)V"));
        newInstr.add(l9);

        method.instructions.insert(pointer, newInstr);
    }

	private void transformArmorTexture(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", ASMNames.M_func110241cb, "()I"));
	    needle.add(new VarInsnNode(ISTORE, 3));

	    AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
	    LabelNode l17 = new LabelNode();
	    newInstr.add(l17);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
	    newInstr.add(new VarInsnNode(ASTORE, 4));
	    LabelNode l18 = new LabelNode();
	    newInstr.add(l18);
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
	    newInstr.add(new TypeInsnNode(INSTANCEOF, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
	    LabelNode l19 = new LabelNode();
	    newInstr.add(new JumpInsnNode(IFEQ, l19));
	    LabelNode l20 = new LabelNode();
	    newInstr.add(l20);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_field110280bR, "[Ljava/lang/String;"));
	    newInstr.add(new InsnNode(ICONST_2));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
	    newInstr.add(new TypeInsnNode(CHECKCAST, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/ItemHorseArmor", "getArmorTexture", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;"));
	    newInstr.add(new InsnNode(AASTORE));
	    LabelNode l21 = new LabelNode();
	    newInstr.add(l21);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
	    newInstr.add(new InsnNode(DUP));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_field110286bQ, "Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V"));
	    newInstr.add(new LdcInsnNode("cst-"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getUnlocalizedName, "()Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;"));
	    newInstr.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_field110286bQ, "Ljava/lang/String;"));
	    newInstr.add(new InsnNode(RETURN));
	    newInstr.add(l19);

	    method.instructions.insert(node, newInstr);
	}

	private void transformUpdateHorseSlots(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_horseChest, "Lnet/minecraft/inventory/AnimalChest;"));
	    needle.add(new InsnNode(ICONST_1));

	    AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_horseChest, "Lnet/minecraft/inventory/AnimalChest;"));
	    newInstr.add(new InsnNode(ICONST_1));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/inventory/AnimalChest", ASMNames.M_getStackInSlot, "(I)Lnet/minecraft/item/ItemStack;"));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_setCustomArmorItem", "(Lnet/minecraft/item/ItemStack;)V"));
	    newInstr.add(new LabelNode());

	    method.instructions.insertBefore(pointer, newInstr);
	}

	private void transformEntityInit(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        needle.add(new IntInsnNode(BIPUSH, 22));
        needle.add(new InsnNode(ICONST_0));
        needle.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_addObject, "(ILjava/lang/Object;)V"));

        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", ASMNames.F_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        newInstr.add(new IntInsnNode(BIPUSH, 23));
        newInstr.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
        newInstr.add(new InsnNode(DUP));
        newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Items", ASMNames.F_ironShovel, "Lnet/minecraft/item/Item;"));
        newInstr.add(new InsnNode(ICONST_0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(Lnet/minecraft/item/Item;I)V"));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", ASMNames.M_addObject, "(ILjava/lang/Object;)V"));

        method.instructions.insert(pointer, newInstr);
	}

	private void transformIsValidArmor(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Items", ASMNames.F_iron_horse_armor, "Lnet/minecraft/item/Item;"));
	    needle.add(new JumpInsnNode(IF_ACMPEQ, new LabelNode()));

	    AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new TypeInsnNode(INSTANCEOF, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
        LabelNode ln = new LabelNode();
        newInstr.add(new JumpInsnNode(IFEQ, ln));
        newInstr.add(new LabelNode());
        newInstr.add(new InsnNode(ICONST_1));
        newInstr.add(new InsnNode(IRETURN));
        newInstr.add(ln);
        newInstr.add(new FrameNode(F_SAME, 0, null, 0, null));

	    method.instructions.insertBefore(node, newInstr);
	}

	private void transformInteract(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new FieldInsnNode(GETSTATIC, "net/minecraft/init/Items", ASMNames.F_diamondHorseArmor, "Lnet/minecraft/item/Item;"));
	    needle.add(new JumpInsnNode(IF_ACMPNE, new LabelNode()));
	    needle.add(new LabelNode());
	    needle.add(new LineNumberNode(801, new LabelNode()));
	    needle.add(new InsnNode(ICONST_3));
	    needle.add(new VarInsnNode(ISTORE, 4));
	    needle.add(new LabelNode());
	    needle.add(new LineNumberNode(804, new LabelNode()));
	    needle.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));

	    AbstractInsnNode node = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
	    newInstr.add(new VarInsnNode(ALOAD, 2));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", ASMNames.M_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new TypeInsnNode(INSTANCEOF, "de/sanandrew/core/manpack/util/ItemHorseArmor"));
        LabelNode l8 = new LabelNode();
        newInstr.add(new JumpInsnNode(IFEQ, l8));
        newInstr.add(new InsnNode(ICONST_4));
        newInstr.add(new VarInsnNode(ISTORE, 4));
        newInstr.add(l8);
        newInstr.add(new FrameNode(F_SAME, 0, null, 0, null));

	    method.instructions.insert(node, newInstr);
	}
}
