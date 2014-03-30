package sanandreasp.core.manpack.transformer;

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
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.player.EntityPlayer") ) {
			return this.transformPlayer(bytes);
		}

		if( transformedName.equals("net.minecraft.entity.Entity") ) {
			return this.transformEntity(bytes);
		}

		return bytes;
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
		MethodNode method = ASMHelper.findMethod(ASMHelper.getNotchedMethod("updateRidden", "net/minecraft/entity/player/EntityPlayer/func_70098_U"), "()V", clazz);

		InsnList needle = new InsnList();
		needle.add(new VarInsnNode(ALOAD, 0));
		needle.add(new FieldInsnNode(GETFIELD, ASMHelper.getNotchedClassName("net/minecraft/entity/player/EntityPlayer"), ASMHelper.getNotchedField("worldObj", "net/minecraft/entity/Entity/field_70170_p"), "Lnet/minecraft/world/World;"));
		needle.add(new FieldInsnNode(GETFIELD, ASMHelper.getNotchedClassName("net/minecraft/world/World"), ASMHelper.getNotchedField("isRemote", "net/minecraft/world/World/field_72995_K"), "Z"));
		LabelNode ln1 = new LabelNode();
		needle.add(new JumpInsnNode(IFNE, ln1));
		needle.add(new VarInsnNode(ALOAD, 0));
		needle.add(new MethodInsnNode(INVOKEVIRTUAL, ASMHelper.getNotchedClassName("net/minecraft/entity/player/EntityPlayer"), ASMHelper.getNotchedMethod("isSneaking", "net/minecraft/entity/Entity/func_70093_af"), "()Z"));
		needle.add(new JumpInsnNode(IFEQ, ln1));

		AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

		InsnList injectList = new InsnList();
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/player/EntityPlayer", "ridingEntity", "Lnet/minecraft/entity/Entity;"));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z"));
		injectList.add(new JumpInsnNode(IFEQ, ((JumpInsnNode)insertPoint).label));

	    method.instructions.insert(insertPoint, injectList);

	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		return bytes;
	}

}
