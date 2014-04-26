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

public class TransformEntityThrowable implements IClassTransformer, Opcodes
{
    private String NTC_EntityThrowable;
    private String NTC_Vec3Pool;
    private String NTC_World;
    private String NTM_onUpdate;
    private String NTM_getVecFromPool;
    private String NTM_rayTraceBlocks;
    private String NTF_motionZ;
    private String NTF_worldObj;
    private String RM_rayTraceBlocks;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( transformedName.equals("net.minecraft.entity.projectile.EntityThrowable") ) {
            this.initiateMappings();
            return this.transformLqThrowable(bytes);
        }

        return bytes;
    }

    private void initiateMappings() {
        this.NTC_EntityThrowable = ASMHelper.getNotchedClassName("net/minecraft/entity/projectile/EntityThrowable");
        this.NTC_Vec3Pool = ASMHelper.getNotchedClassName("net/minecraft/util/Vec3Pool");
        this.NTC_World = ASMHelper.getNotchedClassName("net/minecraft/world/World");

        this.NTM_onUpdate = ASMHelper.getNotchedMethod("onUpdate", "net/minecraft/entity/projectile/EntityThrowable/func_70071_h_");
        this.NTM_getVecFromPool = ASMHelper.getNotchedMethod("getVecFromPool", "net/minecraft/util/Vec3Pool/func_72345_a");
        this.NTM_rayTraceBlocks = ASMHelper.getNotchedMethod("rayTraceBlocks", "net/minecraft/world/World/func_72933_a");

        this.NTF_motionZ = ASMHelper.getNotchedField("motionZ", "net/minecraft/entity/Entity/field_70179_y");
        this.NTF_worldObj = ASMHelper.getNotchedField("worldObj", "net/minecraft/entity/Entity/field_70170_p");

        this.RM_rayTraceBlocks = ASMHelper.getRemappedMF("rayTraceBlocks", "func_72901_a");
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
	        method.visitLocalVariable("this", "Lnet/minecraft/entity/projectile/EntityThrowable;", null, label1, label2, 0);
	        method.visitMaxs(0, 0);
	        method.visitEnd();
	        clazz.methods.add(method);
        }

        {
	        MethodNode method = ASMHelper.findMethod(this.NTM_onUpdate, "()V", clazz);

	        InsnList needle = new InsnList();
	        needle.add(new FieldInsnNode(GETFIELD, this.NTC_EntityThrowable, this.NTF_motionZ, "D"));
	        needle.add(new InsnNode(DADD));
	        needle.add(new MethodInsnNode(INVOKEVIRTUAL, this.NTC_Vec3Pool, this.NTM_getVecFromPool, "(DDD)Lnet/minecraft/util/Vec3;"));
	        needle.add(new VarInsnNode(ASTORE, 2));
	        needle.add(new LabelNode());
	        needle.add(new LineNumberNode(-1, new LabelNode()));
	        needle.add(new VarInsnNode(ALOAD, 0));
	        needle.add(new FieldInsnNode(GETFIELD, this.NTC_EntityThrowable, this.NTF_worldObj, "Lnet/minecraft/world/World;"));
	        needle.add(new VarInsnNode(ALOAD, 1));
	        needle.add(new VarInsnNode(ALOAD, 2));

	        AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

	        needle.add(new MethodInsnNode(INVOKEVIRTUAL, this.NTC_World, this.NTM_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;"));

	        ASMHelper.remLastNodeFromNeedle(method.instructions, needle);

	        InsnList injectList = new InsnList();
	        injectList.add(new VarInsnNode(ALOAD, 0));
	        injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/projectile/EntityThrowable", "_SAP_canImpactOnLiquid", "()Z"));
	        injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/world/World", this.RM_rayTraceBlocks, "(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;"));

	        method.instructions.insert(insertPoint, injectList);
        }

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

}
