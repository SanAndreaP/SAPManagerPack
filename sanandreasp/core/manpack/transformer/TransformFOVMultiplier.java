package sanandreasp.core.manpack.transformer;

import java.util.List;

import net.minecraft.item.Item;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;


import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformFOVMultiplier implements IClassTransformer, Opcodes {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(transformedName.equals("net.minecraft.client.entity.EntityPlayerSP"))
			return transformPlayerSP(bytes);
		if(transformedName.equals("net.minecraft.item.Item"))
			return transformItem(bytes);
		
		return bytes;
	}
	
	private byte[] transformItem(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);
		MethodNode mn = new MethodNode(ACC_PUBLIC, "getFOVItemMultiplier", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;FF)F", null, null);
		{
			AnnotationVisitor av0 = mn.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
			av0.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");
			av0.visitEnd();
		}
		mn.visitCode();
		Label l0 = new Label();
		mn.visitLabel(l0);
		mn.visitLineNumber(1246, l0);
		mn.visitVarInsn(FLOAD, 3);
		mn.visitInsn(FRETURN);
		Label l1 = new Label();
		mn.visitLabel(l1);
		mn.visitLocalVariable("this", "Lnet/minecraft/item/Item;", null, l0, l1, 0);
		mn.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l1, 1);
		mn.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l1, 2);
		mn.visitLocalVariable("fov", "F", null, l0, l1, 3);
		mn.visitLocalVariable("speed", "F", null, l0, l1, 4);
		mn.visitMaxs(0, 0);
		mn.visitEnd();
		
		cn.visitEnd();
		cn.methods.add(mn);
	    bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}
	
	private byte[] transformPlayerSP(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);
		MethodNode mn = ASMHelper.findMethod("getFOVMultiplier", "()F", cn);
		
		InsnList needle = new InsnList();
		needle.add(new FrameNode(F_CHOP, 2, null, 0, null));
		needle.add(new VarInsnNode(FLOAD, 1));
		needle.add(new InsnNode(FRETURN));
		
		InsnList call = new InsnList();
		call.add(new FrameNode(F_CHOP, 2, null, 0, null));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "isUsingItem", "()Z"));
		LabelNode l13 = new LabelNode();
		call.add(new JumpInsnNode(IFEQ, l13));
		call.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/Item", "itemsList", "[Lnet/minecraft/item/Item;"));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getItemInUse", "()Lnet/minecraft/item/ItemStack;"));
		call.add(new FieldInsnNode(GETFIELD, "net/minecraft/item/ItemStack", "itemID", "I"));
		call.add(new InsnNode(AALOAD));
		call.add(new JumpInsnNode(IFNULL, l13));
		LabelNode l14 = new LabelNode();
		call.add(l14);
		call.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/Item", "itemsList", "[Lnet/minecraft/item/Item;"));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getItemInUse", "()Lnet/minecraft/item/ItemStack;"));
		call.add(new FieldInsnNode(GETFIELD, "net/minecraft/item/ItemStack", "itemID", "I"));
		call.add(new InsnNode(AALOAD));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", "getItemInUse", "()Lnet/minecraft/item/ItemStack;"));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new VarInsnNode(FLOAD, 1));
		call.add(new VarInsnNode(ALOAD, 0));
		call.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/entity/EntityPlayerSP", "speedOnGround", "F"));
		call.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/Item", "getFOVItemMultiplier", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;FF)F"));
		call.add(new InsnNode(FRETURN));
		call.add(l13);
		
	    List<AbstractInsnNode> ret = InstructionComparator.insnListFindStart(mn.instructions, needle);
	    if(ret.size() != 1)
	        throw new RuntimeException("Needle not found in Haystack!");
	    
	    mn.instructions.insertBefore(ret.get(0), call);
	    bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}

}
