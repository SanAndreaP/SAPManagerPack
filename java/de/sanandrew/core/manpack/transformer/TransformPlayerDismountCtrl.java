package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformPlayerDismountCtrl implements IClassTransformer, Opcodes
{
    private boolean isInitialized = false;

    private String NTM_updateRidden;
    private String NTM_isSneaking;

    private String NTC_EntityPlayer;
    private String NTC_World;

    private String NTF_worldObj;
    private String NTF_isRemote;

    private String RF_ridingEntity;

    @Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.player.EntityPlayer") ) {
//		    ASMHelper.writeClassToFile(bytes, "C:/Users/SanAndreas/ClassFiles/EntityPlayer.class");
		    this.initiateMappings();
			return this.transformPlayer(bytes);
		}

		if( transformedName.equals("net.minecraft.entity.Entity") ) {
		    this.initiateMappings();
			return this.transformEntity(bytes);
		}

		return bytes;
	}

    private void initiateMappings() {
        if( this.isInitialized ) {
            return;
        }

        this.NTM_updateRidden = ASMHelper.getRemappedMF("updateRidden", "func_70098_U");
        this.NTM_isSneaking = ASMHelper.getRemappedMF("isSneaking", "func_70093_af");
        this.NTC_EntityPlayer = "net/minecraft/entity/player/EntityPlayer";
        this.NTC_World = "net/minecraft/world/World";
        this.NTF_worldObj = ASMHelper.getRemappedMF("worldObj", "field_70170_p");
        this.NTF_isRemote = ASMHelper.getRemappedMF("isRemote", "field_72995_K");
        this.RF_ridingEntity = ASMHelper.getRemappedMF("ridingEntity", "field_70154_o");
    }

	private byte[] transformEntity(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);

		MethodNode method = new MethodNode(ACC_PUBLIC, "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", null, null);
		method.visitCode();
		Label l0 = new Label();
		method.visitLabel(l0);
		method.visitInsn(ICONST_1);
		method.visitInsn(IRETURN);
		Label l1 = new Label();
		method.visitLabel(l1);
		method.visitLocalVariable("this", "Lnet/minecraft/entity/Entity;", null, l0, l1, 0);
		method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l1, 1);
		method.visitMaxs(1, 2);
		method.visitEnd();

		clazz.methods.add(method);

	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}

	private byte[] transformPlayer(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);
		MethodNode method = ASMHelper.findMethod(this.NTM_updateRidden, "()V", clazz);

		InsnList needle = new InsnList();
		needle.add(new VarInsnNode(ALOAD, 0));
		needle.add(new FieldInsnNode(GETFIELD, this.NTC_EntityPlayer, this.NTF_worldObj, "L" + this.NTC_World + ";"));
		needle.add(new FieldInsnNode(GETFIELD, this.NTC_World, this.NTF_isRemote, "Z"));
		LabelNode ln1 = new LabelNode();
		needle.add(new JumpInsnNode(IFNE, ln1));
		needle.add(new VarInsnNode(ALOAD, 0));
		needle.add(new MethodInsnNode(INVOKEVIRTUAL, this.NTC_EntityPlayer, this.NTM_isSneaking, "()Z"));
		needle.add(new JumpInsnNode(IFEQ, ln1));

		AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

		InsnList injectList = new InsnList();
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/player/EntityPlayer", this.RF_ridingEntity, "Lnet/minecraft/entity/Entity;"));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z"));
		injectList.add(new JumpInsnNode(IFEQ, ((JumpInsnNode)insertPoint).label));

	    method.instructions.insert(insertPoint, injectList);

//	    ASMHelper.writeClassToFile(ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_MAXS), "C:/Users/SanAndreas/ClassFiles/EntityPlayer.class");

	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		return bytes;
	}

}
