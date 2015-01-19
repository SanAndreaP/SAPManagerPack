/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformEntityThrowable
        implements IClassTransformer, Opcodes
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( transformedName.equals("net.minecraft.entity.projectile.EntityThrowable") ) {
            return transformLqThrowable(bytes);
        }

        return bytes;
    }

    private static byte[] transformLqThrowable(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);

        {
            MethodNode methodNode = new MethodNode(ACC_PUBLIC, "_SAP_canImpactOnLiquid", "()Z", null, null);

            methodNode.visitCode();
            Label label1 = new Label();
            methodNode.visitLabel(label1);
            methodNode.visitInsn(ICONST_0);
            methodNode.visitInsn(IRETURN);
            Label label2 = new Label();
            methodNode.visitLabel(label2);
            methodNode.visitLocalVariable("this", "Lnet/minecraft/entity/projectile/EntityThrowable;", null, label1,
                                      label2, 0
            );
            methodNode.visitMaxs(0, 0);
            methodNode.visitEnd();
            classNode.methods.add(methodNode);
        }

        {
            MethodNode methodNode = ASMHelper.findMethod(classNode, ASMNames.M_onUpdate, "()V");

            InsnList needle = new InsnList();
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", ASMNames.F_motionZ, "D"));
            needle.add(new InsnNode(DADD));
            needle.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/util/Vec3", ASMNames.M_createVectorHelper, "(DDD)Lnet/minecraft/util/Vec3;", false));
            needle.add(new VarInsnNode(ASTORE, 2));
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            needle.add(new VarInsnNode(ALOAD, 0));
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", ASMNames.F_worldObj, "Lnet/minecraft/world/World;"));
            needle.add(new VarInsnNode(ALOAD, 1));
            needle.add(new VarInsnNode(ALOAD, 2));

            AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle);

            needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", ASMNames.M_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;", false));

            methodNode.instructions.remove(ASMHelper.findLastNodeFromNeedle(methodNode.instructions, needle));

            InsnList injectList = new InsnList();
            injectList.add(new VarInsnNode(ALOAD, 0));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/projectile/EntityThrowable", "_SAP_canImpactOnLiquid", "()Z", false));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", ASMNames.M_rayTraceBlocksB, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;", false));

            methodNode.instructions.insert(insertPoint, injectList);
        }

        bytes = ASMHelper.createBytes(classNode, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

}
