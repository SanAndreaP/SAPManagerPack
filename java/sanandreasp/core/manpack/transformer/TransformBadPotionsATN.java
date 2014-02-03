package sanandreasp.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformBadPotionsATN implements IClassTransformer, Opcodes
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.potion.Potion") ) {
			return transformPotion(bytes);
		}
		
		return bytes;
	}
	
	private byte[] transformPotion(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);
		
		for( MethodNode method : cn.methods ) {
			if( method.name.equals(ASMHelper.getNotchedMethod("isBadEffect", "net/minecraft/potion/Potion/func_76398_f")) ) {
				return bytes;
			}
		}
		
		MethodNode method = new MethodNode(ACC_PUBLIC, ASMHelper.getNotchedMethod("isBadEffect", "net/minecraft/potion/Potion/func_76398_f"), "()Z", null, null);
		method.visitCode();
		Label l0 = new Label();
		method.visitLabel(l0);
		method.visitVarInsn(ALOAD, 0);
		method.visitFieldInsn(GETFIELD, "net/minecraft/potion/Potion", ASMHelper.getRemappedMF("isBadEffect", "field_76418_K"), "Z");
		method.visitInsn(IRETURN);
		Label l1 = new Label();
		method.visitLabel(l1);
		method.visitLocalVariable("this", "Lnet/minecraft/potion/Potion;", null, l0, l1, 0);
		method.visitMaxs(0, 0);
		method.visitEnd();
		
		cn.methods.add(method);
		
	    bytes = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
	    
		return bytes;
	}
}
