package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformEntityThrowable
    implements IClassTransformer, Opcodes
{
    private static final String F_motionZ =     ASMHelper.getRemappedMF("motionZ", "field_70179_y");
    private static final String F_worldObj =    ASMHelper.getRemappedMF("worldObj", "field_70170_p");

    private static final String M_getVecFromPool =  ASMHelper.getRemappedMF("getVecFromPool", "func_72345_a");
    private static final String M_onUpdate =        ASMHelper.getRemappedMF("onUpdate", "func_70071_h_");
    private static final String M_rayTraceBlocks =  ASMHelper.getRemappedMF("rayTraceBlocks", "func_72933_a");

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
            MethodNode method = ASMHelper.findMethod(M_onUpdate, "()V", clazz);

            InsnList needle = new InsnList();
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", F_motionZ, "D"));
            needle.add(new InsnNode(DADD));
            needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/util/Vec3Pool", M_getVecFromPool, "(DDD)Lnet/minecraft/util/Vec3;"));
            needle.add(new VarInsnNode(ASTORE, 2));
            needle.add(new LabelNode());
            needle.add(new LineNumberNode(-1, new LabelNode()));
            needle.add(new VarInsnNode(ALOAD, 0));
            needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/projectile/EntityThrowable", F_worldObj, "Lnet/minecraft/world/World;"));
            needle.add(new VarInsnNode(ALOAD, 1));
            needle.add(new VarInsnNode(ALOAD, 2));

            AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

            needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", M_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;"));

            ASMHelper.remLastNodeFromNeedle(method.instructions, needle);

            InsnList injectList = new InsnList();
            injectList.add(new VarInsnNode(ALOAD, 0));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/projectile/EntityThrowable", "_SAP_canImpactOnLiquid", "()Z"));
            injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", M_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;"));

            method.instructions.insert(insertPoint, injectList);
        }

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

}
