/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformBadPotionsATN
        implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if( transformedName.equals("net.minecraft.potion.Potion") ) {
			return this.transformPotion(bytes);
		}

		return bytes;
	}

	private byte[] transformPotion(byte[] bytes) {
		ClassNode cn = ASMHelper.createClassNode(bytes);

		if( ASMHelper.hasClassMethodName(cn, ASMNames.M_isBadEffect) ) {
			return bytes;
		}

		MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, ASMNames.M_isBadEffect, "()Z", null, null);
		method.visitCode();
		Label l0 = new Label();
		method.visitLabel(l0);
		method.visitVarInsn(Opcodes.ALOAD, 0);
		method.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/potion/Potion", ASMNames.F_isBadEffect, "Z");
		method.visitInsn(Opcodes.IRETURN);
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
