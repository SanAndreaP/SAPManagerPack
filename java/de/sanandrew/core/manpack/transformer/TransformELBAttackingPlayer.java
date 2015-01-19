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

public class TransformELBAttackingPlayer
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( transformedName.equals("net.minecraft.entity.EntityLivingBase") ) {
            return transformAttackingPlayer(bytes);
        }

        if( transformedName.equals("de.sanandrew.core.manpack.util.TransformAccessors") ) {
            return transformAccessors(bytes);
        }

        return bytes;
    }

    private static byte[] transformAccessors(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        int complete = 0;
        for( MethodNode method : clazz.methods ) {
            switch( method.name ) {
                case "getELAttackingPlayer": {
                    method.instructions.clear();
                    method.visitCode();
                    Label l0 = new Label();
                    method.visitLabel(l0);
                    method.visitVarInsn(Opcodes.ALOAD, 0);
                    method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SAP_getAttackingPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;", false);
                    method.visitInsn(Opcodes.ARETURN);
                    Label l1 = new Label();
                    method.visitLabel(l1);
                    method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
                    method.visitMaxs(0, 0);
                    method.visitEnd();
                    complete++;
                    continue;
                }
                case "setELAttackingPlayer": {
                    method.instructions.clear();
                    method.visitCode();
                    Label l0 = new Label();
                    method.visitLabel(l0);
                    method.visitVarInsn(Opcodes.ALOAD, 1);
                    method.visitVarInsn(Opcodes.ALOAD, 0);
                    method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SAP_setAttackingPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)V", false);
                    Label l1 = new Label();
                    method.visitLabel(l1);
                    method.visitInsn(Opcodes.RETURN);
                    Label l2 = new Label();
                    method.visitLabel(l2);
                    method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l2, 0);
                    method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 1);
                    method.visitMaxs(0, 0);
                    method.visitEnd();
                    complete++;
                    continue;
                }
                case "getELRecentlyHit": {
                    method.instructions.clear();
                    method.visitCode();
                    Label l0 = new Label();
                    method.visitLabel(l0);
                    method.visitVarInsn(Opcodes.ALOAD, 0);
                    method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SAP_getRecentlyHit", "()I", false);
                    method.visitInsn(Opcodes.IRETURN);
                    Label l1 = new Label();
                    method.visitLabel(l1);
                    method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
                    method.visitMaxs(0, 0);
                    method.visitEnd();
                    complete++;
                    continue;
                }
                case "setELRecentlyHit": {
                    method.instructions.clear();
                    method.visitCode();
                    Label l0 = new Label();
                    method.visitLabel(l0);
                    method.visitVarInsn(Opcodes.ALOAD, 1);
                    method.visitVarInsn(Opcodes.ILOAD, 0);
                    method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/EntityLivingBase", "_SAP_setRecentlyHit", "(I)V", false);
                    Label l1 = new Label();
                    method.visitLabel(l1);
                    method.visitInsn(Opcodes.RETURN);
                    Label l2 = new Label();
                    method.visitLabel(l2);
                    method.visitLocalVariable("hit", "I", null, l0, l2, 0);
                    method.visitLocalVariable("entity", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 1);
                    method.visitMaxs(0, 0);
                    method.visitEnd();
                    complete++;
                    continue;
                }
            }

            if( complete >= 4 ) {
                break;
            }
        }

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        return bytes;
    }

    private static byte[] transformAttackingPlayer(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        /** ADD GETTER FOR ATTACKING PLAYER **/
        {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_getAttackingPlayer", "()Lnet/minecraft/entity/player/EntityPlayer;", null, null);
            method.visitCode();
            Label l0 = new Label();
            method.visitLabel(l0);
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/EntityLivingBase", ASMNames.F_attackingPlayer, "Lnet/minecraft/entity/player/EntityPlayer;");
            method.visitInsn(Opcodes.ARETURN);
            Label l1 = new Label();
            method.visitLabel(l1);
            method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
            method.visitMaxs(0, 0);
            method.visitEnd();
            clazz.methods.add(method);
        }

        /** ADD SETTER FOR ATTACKING PLAYER **/
        {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_setAttackingPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)V", null, null);
            method.visitCode();
            Label l0 = new Label();
            method.visitLabel(l0);
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitVarInsn(Opcodes.ALOAD, 1);
            method.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/EntityLivingBase", ASMNames.F_attackingPlayer, "Lnet/minecraft/entity/player/EntityPlayer;");
            Label l1 = new Label();
            method.visitLabel(l1);
            method.visitInsn(Opcodes.RETURN);
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
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_getRecentlyHit", "()I", null, null);
            method.visitCode();
            Label l0 = new Label();
            method.visitLabel(l0);
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/entity/EntityLivingBase", ASMNames.F_recentlyHit, "I");
            method.visitInsn(Opcodes.IRETURN);
            Label l1 = new Label();
            method.visitLabel(l1);
            method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l1, 0);
            method.visitMaxs(0, 0);
            method.visitEnd();
            clazz.methods.add(method);
        }

        /** ADD SETTER FOR RECENTLY HIT **/
        {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_setRecentlyHit", "(I)V", null, null);
            method.visitCode();
            Label l0 = new Label();
            method.visitLabel(l0);
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitVarInsn(Opcodes.ILOAD, 1);
            method.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/entity/EntityLivingBase", ASMNames.F_recentlyHit, "I");
            Label l1 = new Label();
            method.visitLabel(l1);
            method.visitInsn(Opcodes.RETURN);
            Label l2 = new Label();
            method.visitLabel(l2);
            method.visitLocalVariable("this", "Lnet/minecraft/entity/EntityLivingBase;", null, l0, l2, 0);
            method.visitLocalVariable("hit", "I", null, l0, l2, 1);
            method.visitMaxs(0, 0);
            method.visitEnd();
            clazz.methods.add(method);
        }

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
