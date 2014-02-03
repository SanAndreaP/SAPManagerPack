package sanandreasp.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class TransformELBAttackingPlayer implements IClassTransformer, Opcodes
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.entity.EntityLivingBase") ) {
			return transformAttackingPlayer(bytes);
		}
		if( transformedName.equals("sanandreasp.core.manpack.helpers.TransformAccessors") ) {
			return transformAccessors(bytes);
		}
		return bytes;
	}
	
	private byte[] transformAttackingPlayer(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);
		
		/** ADD GETTER FOR ATTACKING PLAYER **/
		{
			MethodNode method = new MethodNode(ACC_PUBLIC, "_SanAndreasP_getAttackingPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;", null, null);
			method.visitCode();
			Label l0 = new Label();
			method.visitLabel(l0);
			method.visitVarInsn(ALOAD, 0);
			method.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", ASMHelper.getRemappedMF("attackingPlayer", "field_70717_bb"), "Lnet/minecraft/entity/player/EntityPlayer;");
			method.visitInsn(ARETURN);
			Label l1 = new Label();
			method.visitLabel(l1);
			method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			method.visitMaxs(0, 0);
			method.visitEnd();
			clazz.methods.add(method);
		}
		
		/** ADD SETTER FOR ATTACKING PLAYER **/
		{
			MethodNode method = new MethodNode(ACC_PUBLIC, "_SanAndreasP_setAttackingPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)V", null, null);
			method.visitCode();
			Label l0 = new Label();
			method.visitLabel(l0);
			method.visitVarInsn(ALOAD, 0);
			method.visitVarInsn(ALOAD, 1);
			method.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", ASMHelper.getRemappedMF("attackingPlayer", "field_70717_bb"), "Lnet/minecraft/entity/player/EntityPlayer;");
			Label l1 = new Label();
			method.visitLabel(l1);
			method.visitInsn(RETURN);
			Label l2 = new Label();
			method.visitLabel(l2);
			method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 0);
			method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l2, 1);
			method.visitMaxs(0, 0);
			method.visitEnd();
			clazz.methods.add(method);
		}
		
		/** ADD GETTER FOR RECENTLY HIT **/
		{
			MethodNode method = new MethodNode(ACC_PUBLIC, "_SanAndreasP_getRecentlyHit", "()I", null, null);
			method.visitCode();
			Label l0 = new Label();
			method.visitLabel(l0);
			method.visitVarInsn(ALOAD, 0);
			method.visitFieldInsn(GETFIELD, "net/minecraft/entity/EntityLivingBase", ASMHelper.getRemappedMF("recentlyHit", "field_70718_bc"), "I");
			method.visitInsn(IRETURN);
			Label l1 = new Label();
			method.visitLabel(l1);
			method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
			method.visitMaxs(0, 0);
			method.visitEnd();
			clazz.methods.add(method);
		}
		
		/** ADD SETTER FOR RECENTLY HIT **/
		{
			MethodNode method = new MethodNode(ACC_PUBLIC, "_SanAndreasP_setRecentlyHit", "(I)V", null, null);
			method.visitCode();
			Label l0 = new Label();
			method.visitLabel(l0);
			method.visitVarInsn(ALOAD, 0);
			method.visitVarInsn(ILOAD, 1);
			method.visitFieldInsn(PUTFIELD, "net/minecraft/entity/EntityLivingBase", ASMHelper.getRemappedMF("recentlyHit", "field_70718_bc"), "I");
			Label l1 = new Label();
			method.visitLabel(l1);
			method.visitInsn(RETURN);
			Label l2 = new Label();
			method.visitLabel(l2);
			method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 0);
			method.visitLocalVariable("hit", "I", null, l0, l2, 1);
			method.visitMaxs(0, 0);
			method.visitEnd();
			clazz.methods.add(method);
		}
		
	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_MAXS);
		
		return bytes;
	}
	
	private byte[] transformAccessors(byte[] bytes) {
		ClassNode clazz = ASMHelper.createClassNode(bytes);
		
		int complete = 0;
		for( MethodNode method : clazz.methods ) {
			if( method.name.equals("getELAttackingPlayer") ) {
				method.instructions.clear();
				method.visitCode();
				Label l0 = new Label();
				method.visitLabel(l0);
				method.visitVarInsn(ALOAD, 0);
				method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SanAndreasP_getAttackingPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;");
				method.visitInsn(ARETURN);
				Label l1 = new Label();
				method.visitLabel(l1);
				method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
				method.visitMaxs(0, 0);
				method.visitEnd();
				complete++;
				continue;
			} else if( method.name.equals("setELAttackingPlayer") ) {
				method.instructions.clear();
				method.visitCode();
				Label l0 = new Label();
				method.visitLabel(l0);
				method.visitVarInsn(ALOAD, 1);
				method.visitVarInsn(ALOAD, 0);
				method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SanAndreasP_setAttackingPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)V");
				Label l1 = new Label();
				method.visitLabel(l1);
				method.visitInsn(RETURN);
				Label l2 = new Label();
				method.visitLabel(l2);
				method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l2, 0);
				method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 1);
				method.visitMaxs(0, 0);
				method.visitEnd();
				complete++;
				continue;
			} else if( method.name.equals("getELRecentlyHit") ) {
				method.instructions.clear();
				method.visitCode();
				Label l0 = new Label();
				method.visitLabel(l0);
				method.visitVarInsn(ALOAD, 0);
				method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SanAndreasP_getRecentlyHit", "()I");
				method.visitInsn(IRETURN);
				Label l1 = new Label();
				method.visitLabel(l1);
				method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
				method.visitMaxs(0, 0);
				method.visitEnd();
				complete++;
				continue;
			} else if( method.name.equals("setELRecentlyHit") ) {
				method.instructions.clear();
				method.visitCode();
				Label l0 = new Label();
				method.visitLabel(l0);
				method.visitVarInsn(ALOAD, 1);
				method.visitVarInsn(ILOAD, 0);
				method.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SanAndreasP_setRecentlyHit", "(I)V");
				Label l1 = new Label();
				method.visitLabel(l1);
				method.visitInsn(RETURN);
				Label l2 = new Label();
				method.visitLabel(l2);
				method.visitLocalVariable("hit", "I", null, l0, l2, 0);
				method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 1);
				method.visitMaxs(0, 0);
				method.visitEnd();
				complete++;
				continue;
			}
			if( complete >= 4 ) {
				break;
			}
		}
		
	    bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
	    
		return bytes;
	}
}
