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
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( transformedName.equals("net.minecraft.entity.player.EntityPlayer") ) {
            return transformPlayer(bytes);
        }

        if( transformedName.equals("net.minecraft.entity.Entity") ) {
            return transformEntity(bytes);
        }

        return bytes;
    }

    private static byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitInsn(Opcodes.ICONST_1);
        method.visitInsn(Opcodes.IRETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", "Lnet/minecraft/entity/Entity;", null, l0, l1, 0);
        method.visitLocalVariable("player", "Lnet/minecraft/entity/player/EntityPlayer;", null, l0, l1, 1);
        method.visitMaxs(1, 2);
        method.visitEnd();

        clazz.methods.add(method);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private static byte[] transformPlayer(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(clazz, ASMNames.M_updateRidden, "()V");

        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", ASMNames.F_worldObj, "Lnet/minecraft/world/World;"));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/World", ASMNames.F_isRemote, "Z"));
        LabelNode ln1 = new LabelNode();
        needle.add(new JumpInsnNode(Opcodes.IFNE, ln1));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/player/EntityPlayer", ASMNames.M_isSneaking, "()Z", false));
        needle.add(new JumpInsnNode(Opcodes.IFEQ, ln1));

        AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", ASMNames.F_ridingEntity, "Lnet/minecraft/entity/Entity;"));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", "_SAP_canDismountWithLSHIFT", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", false));
        injectList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) insertPoint).label));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
