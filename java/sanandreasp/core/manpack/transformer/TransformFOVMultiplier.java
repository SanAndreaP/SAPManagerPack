package sanandreasp.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class TransformFOVMultiplier implements IClassTransformer, Opcodes {

	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.client.entity.EntityPlayerSP") )
			return transformPlayerSP(bytes);
		if( transformedName.equals("net.minecraft.item.Item") )
			return transformItem(bytes);
		
		return bytes;
	}
	
	private byte[] transformItem(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);
		MethodNode method = new MethodNode(ACC_PUBLIC, "getFOVItemMultiplier", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;F)F", null, null);
		{
			AnnotationVisitor annotVisitor = method.visitAnnotation("Lcpw/mods/fml/relauncher/SideOnly;", true);
			annotVisitor.visitEnum("value", "Lcpw/mods/fml/relauncher/Side;", "CLIENT");
			annotVisitor.visitEnd();
		}
		method.visitCode();
		
		Label l0 = new Label();
		method.visitLabel(l0);
		
		/** ORIGINAL CODE! **/
		  method.visitVarInsn(FLOAD, 3);
		/** DEBUGGING CODE! **/
//		  mn.visitInsn(FCONST_0);
		/** END **/
		
		method.visitInsn(FRETURN);
		Label l1 = new Label();
		method.visitLabel(l1);
		method.visitLocalVariable("this", "Lnet/minecraft/item/Item;", null, l0, l1, 0);
		method.visitLocalVariable("stack", "Lnet/minecraft/item/ItemStack;", null, l0, l1, 1);
		method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l1, 2);
		method.visitLocalVariable("fov", "F", null, l0, l1, 3);
		
		method.visitMaxs(0, 0);
		method.visitEnd();
		
		clazz.visitEnd();
		clazz.methods.add(method);
	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}
	
	private byte[] transformPlayerSP(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);
		MethodNode method = ASMHelper.findMethod(ASMHelper.getNotchedMethod("getFOVMultiplier", "net/minecraft/client/entity/EntityPlayerSP/func_71151_f"), "()F", clazz);
		
		InsnList needle = new InsnList();
		needle.add(new FrameNode(Opcodes.F_CHOP, 2, null, 0, null));
		needle.add(new VarInsnNode(FLOAD, 1));
		needle.add(new InsnNode(FRETURN));
		
		AbstractInsnNode insertPoint = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
		
		InsnList injectList = new InsnList();
		injectList.add(new FrameNode(F_CHOP, 2, null, 0, null));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", ASMHelper.getRemappedMF("isUsingItem", "func_71039_bw"), "()Z"));
		LabelNode l13 = new LabelNode();
		injectList.add(new JumpInsnNode(IFEQ, l13));
		injectList.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/Item", ASMHelper.getRemappedMF("itemsList", "field_77698_e"), "[Lnet/minecraft/item/Item;"));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", ASMHelper.getRemappedMF("getItemInUse", "func_71011_bu"), "()Lnet/minecraft/item/ItemStack;"));
		injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/item/ItemStack", ASMHelper.getRemappedMF("itemID", "field_77993_c"), "I"));
		injectList.add(new InsnNode(AALOAD));
		injectList.add(new JumpInsnNode(IFNULL, l13));
		injectList.add(new LabelNode());
		injectList.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/Item", ASMHelper.getRemappedMF("itemsList", "field_77698_e"), "[Lnet/minecraft/item/Item;"));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", ASMHelper.getRemappedMF("getItemInUse", "func_71011_bu"), "()Lnet/minecraft/item/ItemStack;"));
		injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/item/ItemStack", ASMHelper.getRemappedMF("itemID", "field_77993_c"), "I"));
		injectList.add(new InsnNode(AALOAD));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/entity/EntityPlayerSP", ASMHelper.getRemappedMF("getItemInUse", "func_71011_bu"), "()Lnet/minecraft/item/ItemStack;"));
		injectList.add(new VarInsnNode(ALOAD, 0));
		injectList.add(new VarInsnNode(FLOAD, 1));
		injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/Item", "getFOVItemMultiplier", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;F)F"));
		injectList.add(new InsnNode(FRETURN));
		injectList.add(l13);
	    
	    method.instructions.insertBefore(insertPoint, injectList);
	    
	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		return bytes;
	}

}
