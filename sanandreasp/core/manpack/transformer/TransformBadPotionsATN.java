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

import sanandreasp.core.manpack.ManPackLoadingPlugin;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.IClassTransformer;

public class TransformBadPotionsATN implements IClassTransformer, Opcodes {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(transformedName.equals("net.minecraft.potion.Potion") && ManPackLoadingPlugin.isServer())
			return transformPotion(bytes);
		
		return bytes;
	}
	
	private byte[] transformPotion(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);
		MethodNode mn = new MethodNode(ACC_PUBLIC, "isBadEffect", "()Z", null, null);
		mn.visitCode();
		Label l0 = new Label();
		mn.visitLabel(l0);
		mn.visitLineNumber(272, l0);
		mn.visitVarInsn(ALOAD, 0);
		mn.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", "isBadEffect", "Z");
		mn.visitInsn(IRETURN);
		Label l1 = new Label();
		mn.visitLabel(l1);
		mn.visitLocalVariable("this", "Lnet/minecraft/potion/Potion;", null, l0, l1, 0);
		mn.visitMaxs(0, 0);
		mn.visitEnd();
		
		cn.methods.add(mn);
		
	    bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
	    
		return bytes;
	}

}
