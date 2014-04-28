package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformPlayerDismountCtrl
    implements IClassTransformer, Opcodes
{
    private static final String F_isRemote =        ASMHelper.getRemappedMF("isRemote", "field_72995_K");
    private static final String F_worldObj =        ASMHelper.getRemappedMF("worldObj", "field_70170_p");
    private static final String F_ridingEntity =    ASMHelper.getRemappedMF("ridingEntity", "field_70154_o");

    private static final String M_isSneaking =   ASMHelper.getRemappedMF("updateRidden", "func_70098_U");
    private static final String M_updateRidden = ASMHelper.getRemappedMF("isSneaking", "func_70093_af");

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( transformedName.equals("net.minecraft.entity.player.EntityPlayer") ) {
            return this.transformPlayer(bytes);
        }

        if( transformedName.equals("net.minecraft.entity.Entity") ) {
            return this.transformEntity(bytes);
        }

        return bytes;
    }

    private byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = new MethodNode(ACC_PUBLIC, "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitInsn(ICONST_1);
        method.visitInsn(IRETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", "Lnet/minecraft/entity/Entity;", null, l0, l1, 0);
        method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l1, 1);
        method.visitMaxs(1, 2);
        method.visitEnd();

        clazz.methods.add(method);

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private byte[] transformPlayer(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(M_updateRidden, "()V", clazz);

        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/player/EntityPlayer", F_worldObj, "Lnet/minecraft/world/World;"));
        needle.add(new FieldInsnNode(GETFIELD, "net/minecraft/world/World", F_isRemote, "Z"));
        LabelNode ln1 = new LabelNode();
        needle.add(new JumpInsnNode(IFNE, ln1));
        needle.add(new VarInsnNode(ALOAD, 0));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", M_isSneaking, "()Z"));
        needle.add(new JumpInsnNode(IFEQ, ln1));

        AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new VarInsnNode(ALOAD, 0));
        injectList.add(new FieldInsnNode(GETFIELD, "net/minecraft/entity/player/EntityPlayer", F_ridingEntity, "Lnet/minecraft/entity/Entity;"));
        injectList.add(new VarInsnNode(ALOAD, 0));
        injectList.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/entity/Entity", "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z"));
        injectList.add(new JumpInsnNode(IFEQ, ((JumpInsnNode) insertPoint).label));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        return bytes;
    }

}
