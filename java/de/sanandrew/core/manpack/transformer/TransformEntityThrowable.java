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
            return this.transformLqThrowable(bytes);
        }

        return bytes;
    }

    private byte[] transformLqThrowable(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        {
            MethodNode method = new MethodNode(ACC_PUBLIC, "_SAP_canImpactOnLiquid", "()Z", null, null);

            method.visitCode();
            Label label1 = new Label();
            method.visitLabel(label1);
            method.visitInsn(ICONST_0);
            method.visitInsn(IRETURN);
            Label label2 = new Label();
            method.visitLabel(label2);
            method.visitLocalVariable("this", "Lnet/minecraft/entity/projectile/EntityThrowable;", null, label1,
                                      label2, 0);
            method.visitMaxs(0, 0);
            method.visitEnd();
            clazz.methods.add(method);
        }

        {
            MethodNode method = ASMHelper.findMethod(ASMNames.M_onUpdate, "()V", clazz);

            InsnList needle = new InsnList();
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", ASMNames.F_motionZ, "D"));
            needle.add(new InsnNode(DADD));
            needle.add(new MethodInsnNode(INVOKESTATIC, "net/minecraft/util/Vec3", ASMNames.M_createVectorHelper, "(DDD)Lnet/minecraft/util/Vec3;"));
            needle.add(new VarInsnNode(ASTORE, 2));
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            needle.add(new VarInsnNode(ALOAD, 0));
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", ASMNames.F_worldObj, "Lnet/minecraft/world/World;"));
            needle.add(new VarInsnNode(ALOAD, 1));
            needle.add(new VarInsnNode(ALOAD, 2));

            AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

            needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", ASMNames.M_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;"));

            ASMHelper.remLastNodeFromNeedle(method.instructions, needle);

            InsnList injectList = new InsnList();
            injectList.add(new VarInsnNode(ALOAD, 0));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/projectile/EntityThrowable", "_SAP_canImpactOnLiquid", "()Z"));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", ASMNames.M_rayTraceBlocksB, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;"));

            method.instructions.insert(insertPoint, injectList);
        }

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

}
