package sanandreasp.core.manpack.transformer;

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

public class TransformHorseArmor implements IClassTransformer, Opcodes
{
    private String RF_dataWatcher;
    private String RF_horseChest;
    private String RF_horseArmorTextures;
    private String RF_itemsList;
    private String RF_field110280bR;
    private String RF_field110286bQ;
    private String RF_field110273bx;
    private String RF_itemIDItemStack;
    private String RM_updateObject;
    private String RM_getStackInSlot;
    private String RM_addObject;
    private String RM_getItem;
    private String RM_getWatchableObjectItemStack;
    private String RM_isItemEqual;
    private String RM_playSound;
    private String NTM_interact;
    private String NTM_func110211v;
    private String NTM_entityInit;
    private String NTM_func110232cE;
    private String NTM_setHorseTexturePaths;
    private String NTM_addObject;
    private String NTM_isHorseSaddled;
    private String NTM_onInventoryChanged;
    private String NTM_playSound;
    private String NTM_func110241cb;
    private String NTM_getTotalArmorValue;
    private String NTC_EntityHorse;
    private String NTC_DataWatcher;
    private String NTC_Item;
    private String NTC_ItemStack;
    private String NTF_horseChest;
    private String NTF_dataWatcher;
    private String NTF_horseArmorIron;
    private String NTF_horseArmorDiamond;
    private String NTF_itemIDItem;
    private String NTF_itemIDItemStack;
    private String NTF_field110280bR;
    private String NTF_horseArmorTextures;
    private String NTF_field110286bQ;
    private String NTF_field110273bx;
    private String NTF_armorValues;
    
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.passive.EntityHorse") ) {
		    this.initiateMappings();
			return this.transformHorse(bytes);
		}

		return bytes;
	}

	private void initiateMappings() {
        RF_dataWatcher = ASMHelper.getRemappedMF("dataWatcher", "field_70180_af");
        RF_horseChest =  ASMHelper.getRemappedMF("horseChest",  "field_110296_bG");
        RF_itemsList =   ASMHelper.getRemappedMF("itemsList",   "field_77698_e");
        RF_field110280bR = ASMHelper.getRemappedMF("field_110280_bR", "field_110280_bR");
        RF_field110286bQ = ASMHelper.getRemappedMF("field_110286_bQ", "field_110286_bQ");
        RF_horseArmorTextures = ASMHelper.getRemappedMF("horseArmorTextures", "field_110270_bw");
        RF_field110273bx = ASMHelper.getRemappedMF("field_110273_bx", "field_110273_bx");
        RF_itemIDItemStack = ASMHelper.getRemappedMF("itemID", "field_77993_c");
        
        RM_updateObject = ASMHelper.getRemappedMF("updateObject", "func_75692_b");
        RM_getStackInSlot = ASMHelper.getRemappedMF("getStackInSlot", "func_70301_a");
        RM_addObject = ASMHelper.getRemappedMF("addObject", "func_75682_a");
        RM_getItem = ASMHelper.getRemappedMF("getItem", "func_77973_b");
        RM_getWatchableObjectItemStack = ASMHelper.getRemappedMF("getWatchableObjectItemStack", "func_82710_f");
        RM_isItemEqual = ASMHelper.getRemappedMF("isItemEqual", "func_77969_a");
        RM_playSound = ASMHelper.getRemappedMF("playSound", "func_85030_a");
        
        NTM_interact = ASMHelper.getNotchedMethod("interact", "net/minecraft/entity/passive/EntityHorse/func_70085_c");
        NTM_func110211v = ASMHelper.getNotchedMethod("func_110211_v", "net/minecraft/entity/passive/EntityHorse/func_110211_v");
        NTM_entityInit = ASMHelper.getNotchedMethod("entityInit", "net/minecraft/entity/passive/EntityHorse/func_70088_a");
        NTM_func110232cE = ASMHelper.getNotchedMethod("func_110232_cE", "net/minecraft/entity/passive/EntityHorse/func_110232_cE");
        NTM_setHorseTexturePaths = ASMHelper.getNotchedMethod("setHorseTexturePaths", "net/minecraft/entity/passive/EntityHorse/func_110247_cG");
        NTM_addObject = ASMHelper.getNotchedMethod("addObject", "net/minecraft/entity/DataWatcher/func_75682_a");
        NTM_isHorseSaddled = ASMHelper.getNotchedMethod("isHorseSaddled", "net/minecraft/entity/passive/EntityHorse/func_110257_ck");
        NTM_onInventoryChanged = ASMHelper.getNotchedMethod("onInventoryChanged", "net/minecraft/entity/passive/EntityHorse/func_76316_a");
        NTM_playSound = ASMHelper.getNotchedMethod("playSound", "net/minecraft/entity/Entity/func_85030_a");
        NTM_func110241cb = ASMHelper.getNotchedMethod("func_110241_cb", "net/minecraft/entity/passive/EntityHorse/func_110241_cb");
        NTM_getTotalArmorValue = ASMHelper.getNotchedMethod("getTotalArmorValue", "net/minecraft/entity/passive/EntityHorse/func_70658_aO");
        
        NTC_EntityHorse = ASMHelper.getNotchedClassName("net/minecraft/entity/passive/EntityHorse");
        NTC_DataWatcher = ASMHelper.getNotchedClassName("net/minecraft/entity/DataWatcher");
        NTC_Item = ASMHelper.getNotchedClassName("net/minecraft/item/Item");
        NTC_ItemStack = ASMHelper.getNotchedClassName("net/minecraft/item/ItemStack");
        
        NTF_horseChest = ASMHelper.getNotchedField("horseChest", "net/minecraft/entity/passive/EntityHorse/field_110296_bG");
        NTF_dataWatcher = ASMHelper.getNotchedField("dataWatcher", "net/minecraft/entity/Entity/field_70180_af");
        NTF_horseArmorIron = ASMHelper.getNotchedField("horseArmorIron", "net/minecraft/item/Item/field_111215_ce");
        NTF_itemIDItem = ASMHelper.getNotchedField("itemID", "net/minecraft/item/Item/field_77779_bT");
        NTF_itemIDItemStack = ASMHelper.getNotchedField("itemID", "net/minecraft/item/ItemStack/field_77993_c");
        NTF_horseArmorDiamond = ASMHelper.getNotchedField("horseArmorDiamond", "net/minecraft/item/Item/field_111213_cg");
        NTF_field110280bR = ASMHelper.getNotchedField("field_110280_bR", "net/minecraft/entity/passive/EntityHorse/field_110280_bR");
        NTF_horseArmorTextures = ASMHelper.getNotchedField("horseArmorTextures", "net/minecraft/entity/passive/EntityHorse/field_110270_bw");
        NTF_field110286bQ = ASMHelper.getNotchedField("field_110286_bQ", "net/minecraft/entity/passive/EntityHorse/field_110286_bQ");
        NTF_field110273bx = ASMHelper.getNotchedField("field_110273_bx", "net/minecraft/entity/passive/EntityHorse/field_110273_bx");
        NTF_armorValues = ASMHelper.getNotchedField("armorValues", "net/minecraft/entity/passive/EntityHorse/field_110272_by");
	}

    private byte[] transformHorse(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);

		MethodNode method = this.injectMethodSCAI();
		clazz.methods.add(method);
		
		method = injectMethodGCAI();
		clazz.methods.add(method);

		this.transformInteract(ASMHelper.findMethod(NTM_interact, "(Lnet/minecraft/entity/player/EntityPlayer;)Z", clazz));
        this.transformIsValidArmor(ASMHelper.findMethod(NTM_func110211v, "(I)Z", clazz));
        this.transformEntityInit(ASMHelper.findMethod(NTM_entityInit, "()V", clazz));
        this.transformUpdateHorseSlots(ASMHelper.findMethod(NTM_func110232cE, "()V", clazz));
        this.transformOnInvChanged(ASMHelper.findMethod(NTM_onInventoryChanged, "(Lnet/minecraft/inventory/InventoryBasic;)V", clazz));
        this.transformGetTotalArmorValue(ASMHelper.findMethod(NTM_getTotalArmorValue, "()I", clazz));
        
        method = ASMHelper.findMethod(NTM_setHorseTexturePaths, "()V", clazz);
        if( method != null ) {
            this.transformArmorTexture(method);
        }

	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        
//        ASMHelper.writeClassToFile(bytes, "C:\\Users\\SanAndreas\\ClassFiles\\EntityHorse.class");
        
		return bytes;
	}
    
    private MethodNode injectMethodGCAI() {
        MethodNode method = new MethodNode(ACC_PRIVATE, "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(ALOAD, 0);
        method.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        method.visitIntInsn(BIPUSH, 23);
        method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", RM_getWatchableObjectItemStack, "(I)Lnet/minecraft/item/ItemStack;");
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
        method.visitInsn(ICONST_0);
        method.visitInsn(ICONST_0);
        method.visitInsn(ICONST_0);
        method.visitMethodInsn(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(III)V");
        method.visitVarInsn(ASTORE, 1);
        method.visitLabel(l1);
        method.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        method.visitVarInsn(ALOAD, 0);
        method.visitFieldInsn(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_dataWatcher, "Lnet/minecraft/entity/DataWatcher;");
        method.visitIntInsn(BIPUSH, 23);
        method.visitVarInsn(ALOAD, 1);
        method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", RM_updateObject, "(ILjava/lang/Object;)V");
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
        needle.add(new FieldInsnNode(GETSTATIC, NTC_EntityHorse, NTF_armorValues, "[I"));
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, NTC_EntityHorse, NTM_func110241cb, "()I"));
        
        AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
        newInstr.add(new VarInsnNode(ASTORE, 1));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new TypeInsnNode(INSTANCEOF, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
        LabelNode l2 = new LabelNode();
        newInstr.add(new JumpInsnNode(IFEQ, l2));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_getItem, "()Lnet/minecraft/item/Item;"));
        newInstr.add(new TypeInsnNode(CHECKCAST, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new VarInsnNode(ALOAD, 1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "sanandreasp/core/manpack/helpers/ItemHorseArmor", "getArmorValue", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I"));
        newInstr.add(new InsnNode(IRETURN));
        newInstr.add(l2);
        
        method.instructions.insertBefore(pointer, newInstr);
    }
    
    private void transformOnInvChanged(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, NTC_EntityHorse, NTM_isHorseSaddled, "()Z"));
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
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, NTC_EntityHorse, NTM_playSound, "(Ljava/lang/String;FF)V"));
        needle.add(new LabelNode());
        
        pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
        
        newInstr = new InsnList();
        newInstr.add(new VarInsnNode(ALOAD, 4));
        newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/item/ItemStack", RF_itemIDItemStack, "I"));
        LabelNode l9 = new LabelNode();
        newInstr.add(new JumpInsnNode(IFNE, l9));
        newInstr.add(new VarInsnNode(ALOAD, 4));
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_isItemEqual, "(Lnet/minecraft/item/ItemStack;)Z"));
        newInstr.add(new JumpInsnNode(IFNE, l9));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new LdcInsnNode("mob.horse.armor"));
        newInstr.add(new LdcInsnNode(new Float("0.5")));
        newInstr.add(new InsnNode(FCONST_1));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/passive/EntityHorse", RM_playSound, "(Ljava/lang/String;FF)V"));
        newInstr.add(l9);
        
        method.instructions.insert(pointer, newInstr);
    }
	
	private void transformArmorTexture(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new FieldInsnNode(GETFIELD, NTC_EntityHorse, NTF_field110280bR, "[Ljava/lang/String;"));
	    needle.add(new InsnNode(ICONST_2));
	    needle.add(new FieldInsnNode(GETSTATIC, NTC_EntityHorse, NTF_horseArmorTextures, "[Ljava/lang/String;"));
	    needle.add(new VarInsnNode(ILOAD, 3));
	    needle.add(new InsnNode(AALOAD));
	    needle.add(new InsnNode(AASTORE));
	    LabelNode l8 = new LabelNode();
	    needle.add(l8);
	    needle.add(new LineNumberNode(-1, l8));
	    needle.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
	    needle.add(new InsnNode(DUP));
	    needle.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V"));
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new InsnNode(DUP_X1));
	    needle.add(new FieldInsnNode(GETFIELD, NTC_EntityHorse, NTF_field110286bQ, "Ljava/lang/String;"));
	    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    needle.add(new FieldInsnNode(GETSTATIC, NTC_EntityHorse, NTF_field110273bx, "[Ljava/lang/String;"));
	    needle.add(new VarInsnNode(ILOAD, 3));
	    needle.add(new InsnNode(AALOAD));
	    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    needle.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;"));
	    needle.add(new FieldInsnNode(PUTFIELD, NTC_EntityHorse, NTF_field110286bQ, "Ljava/lang/String;"));
	    
	    AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle).getPrevious();

	    ASMHelper.removeNeedleFromHaystack(method.instructions, needle);

        InsnList newInstr = new InsnList();
	    LabelNode l17 = new LabelNode();
	    newInstr.add(l17);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_getCustomArmorItem", "()Lnet/minecraft/item/ItemStack;"));
	    newInstr.add(new VarInsnNode(ASTORE, 4));
	    LabelNode l18 = new LabelNode();
	    newInstr.add(l18);
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_getItem, "()Lnet/minecraft/item/Item;"));
	    newInstr.add(new TypeInsnNode(INSTANCEOF, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
	    LabelNode l19 = new LabelNode();
	    newInstr.add(new JumpInsnNode(IFEQ, l19));
	    LabelNode l20 = new LabelNode();
	    newInstr.add(l20);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_field110280bR, "[Ljava/lang/String;"));
	    newInstr.add(new InsnNode(ICONST_2));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_getItem, "()Lnet/minecraft/item/Item;"));
	    newInstr.add(new TypeInsnNode(CHECKCAST, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "sanandreasp/core/manpack/helpers/ItemHorseArmor", "getArmorTexture", "(Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;"));
	    newInstr.add(new InsnNode(AASTORE));
	    LabelNode l21 = new LabelNode();
	    newInstr.add(l21);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
	    newInstr.add(new InsnNode(DUP));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_field110286bQ, "Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V"));
	    newInstr.add(new LdcInsnNode("cst-"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    newInstr.add(new VarInsnNode(ALOAD, 4));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", "getUnlocalizedName", "()Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;"));
	    newInstr.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/passive/EntityHorse", "field_110286_bQ", "Ljava/lang/String;"));
	    LabelNode l22 = new LabelNode();
	    newInstr.add(l22);
	    LabelNode l23 = new LabelNode();
	    newInstr.add(new JumpInsnNode(GOTO, l23));
	    newInstr.add(l19);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_field110280bR, "[Ljava/lang/String;"));
	    newInstr.add(new InsnNode(ICONST_2));
	    newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/entity/passive/EntityHorse", RF_horseArmorTextures, "[Ljava/lang/String;"));
	    newInstr.add(new VarInsnNode(ILOAD, 3));
	    newInstr.add(new InsnNode(AALOAD));
	    newInstr.add(new InsnNode(AASTORE));
	    LabelNode l24 = new LabelNode();
	    newInstr.add(l24);
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
	    newInstr.add(new InsnNode(DUP));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_field110286bQ, "Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;"));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V"));
	    newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/entity/passive/EntityHorse", RF_field110273bx, "[Ljava/lang/String;"));
	    newInstr.add(new VarInsnNode(ILOAD, 3));
	    newInstr.add(new InsnNode(AALOAD));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;"));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;"));
	    newInstr.add(new FieldInsnNode(PUTFIELD, "net/minecraft/entity/passive/EntityHorse", RF_field110286bQ, "Ljava/lang/String;"));
	    newInstr.add(l23);
	    
	    method.instructions.insert(pointer, newInstr);
	}

	private void transformUpdateHorseSlots(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new VarInsnNode(ALOAD, 0));
	    needle.add(new FieldInsnNode(GETFIELD, NTC_EntityHorse, NTF_horseChest, "Lnet/minecraft/inventory/AnimalChest;"));

	    AbstractInsnNode pointer = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();

	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new VarInsnNode(ALOAD, 0));
	    newInstr.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/passive/EntityHorse", RF_horseChest, "Lnet/minecraft/inventory/AnimalChest;"));
	    newInstr.add(new InsnNode(ICONST_1));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/inventory/AnimalChest", RM_getStackInSlot, "(I)Lnet/minecraft/item/ItemStack;"));
	    newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/entity/passive/EntityHorse", "_SAP_setCustomArmorItem", "(Lnet/minecraft/item/ItemStack;)V"));
	    newInstr.add(new LabelNode());

	    method.instructions.insertBefore(pointer, newInstr);
	}

	private void transformEntityInit(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new FieldInsnNode(GETFIELD, NTC_EntityHorse, NTF_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        needle.add(new IntInsnNode(BIPUSH, 22));
        needle.add(new InsnNode(ICONST_0));
        needle.add(new MethodInsnNode(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, NTC_DataWatcher, NTM_addObject, "(ILjava/lang/Object;)V"));

        AbstractInsnNode pointer = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(ALOAD, 0));
        newInstr.add(new FieldInsnNode(GETFIELD, NTC_EntityHorse, RF_dataWatcher, "Lnet/minecraft/entity/DataWatcher;"));
        newInstr.add(new IntInsnNode(BIPUSH, 23));
        newInstr.add(new TypeInsnNode(NEW, "net/minecraft/item/ItemStack"));
        newInstr.add(new InsnNode(DUP));
        newInstr.add(new InsnNode(ICONST_0));
        newInstr.add(new InsnNode(ICONST_0));
        newInstr.add(new InsnNode(ICONST_0));
        newInstr.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/item/ItemStack", "<init>", "(III)V"));
        newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/DataWatcher", RM_addObject, "(ILjava/lang/Object;)V"));

        method.instructions.insert(pointer, newInstr);
	}

	private void transformIsValidArmor(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new FieldInsnNode(GETSTATIC, NTC_Item, NTF_horseArmorIron, "Lnet/minecraft/item/Item;"));
	    needle.add(new FieldInsnNode(GETFIELD, NTC_Item, NTF_itemIDItem, "I"));
	    needle.add(new JumpInsnNode(IF_ICMPEQ, new LabelNode()));

	    JumpInsnNode jumpNode = (JumpInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);
	    LabelNode label1 = jumpNode.label;

	    InsnList newInstr = new InsnList();
	    newInstr.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/Item", RF_itemsList, "[Lnet/minecraft/item/Item;"));
	    newInstr.add(new VarInsnNode(ILOAD, 0));
	    newInstr.add(new InsnNode(AALOAD));
	    newInstr.add(new TypeInsnNode(INSTANCEOF, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
	    newInstr.add(new JumpInsnNode(IFNE, label1));

	    method.instructions.insert(jumpNode, newInstr);
	}

	private void transformInteract(MethodNode method) {
	    InsnList needle = new InsnList();
	    needle.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
	    needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
	    needle.add(new FieldInsnNode(Opcodes.GETFIELD, NTC_ItemStack, NTF_itemIDItemStack, "I"));
	    needle.add(new FieldInsnNode(Opcodes.GETSTATIC, NTC_Item, NTF_horseArmorDiamond, "Lnet/minecraft/item/Item;"));
	    needle.add(new FieldInsnNode(GETFIELD, NTC_Item, NTF_itemIDItem, "I"));
	    needle.add(new JumpInsnNode(IF_ICMPNE, new LabelNode()));

	    JumpInsnNode icmpneNode = (JumpInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);        // get ICMPNE-Node for removal
	    LabelNode label20 = icmpneNode.label;                                                                          // get Label 20

	    needle.add(new LabelNode());                                                                                   // get Label 24
	    LabelNode label24 = (LabelNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

	    needle.add(new LineNumberNode(-1, new LabelNode()));                                                           // get ICONST-Node for removal
	    needle.add(new InsnNode(ICONST_3));
	    InsnNode iconstNode = (InsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

	    InsnList newInstr = new InsnList();
	    newInstr.add(new JumpInsnNode(IF_ICMPNE, label24));
	    newInstr.add(new LabelNode());
	    newInstr.add(new InsnNode(ICONST_3));
	    newInstr.add(new VarInsnNode(ISTORE, 4));
	    newInstr.add(new LabelNode());
	    newInstr.add(new JumpInsnNode(GOTO, label20));

	    method.instructions.insert(icmpneNode, newInstr);

	    method.instructions.remove(icmpneNode);

	    newInstr = new InsnList();
	    newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
	    newInstr.add(new VarInsnNode(ALOAD, 2));
	    newInstr.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", RM_getItem, "()Lnet/minecraft/item/Item;"));
	    newInstr.add(new TypeInsnNode(INSTANCEOF, "sanandreasp/core/manpack/helpers/ItemHorseArmor"));
	    newInstr.add(new JumpInsnNode(IFEQ, label20));
	    newInstr.add(new LabelNode());
	    newInstr.add(new InsnNode(ICONST_4));

	    method.instructions.insert(iconstNode, newInstr);

	    method.instructions.remove(iconstNode);
	}
}
