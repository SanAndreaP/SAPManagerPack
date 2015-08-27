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

/**
 * This transformer adds a hook for entities to control whether or not the rider of the entity can dismount it via sneaking.
 * The entity needs to override _SAP_canDismountOnInput() and should return true, if the rider can dismount, otherwise false.
 */
public class TransformPlayerDismountCtrl
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( "net.minecraft.entity.player.EntityPlayer".equals(transformedName) ) {
            return transformPlayer(bytes);
        }

        if( "net.minecraft.entity.Entity".equals(transformedName) ) {
            return transformEntity(bytes);
        }

        return bytes;
    }

    /**
     * Transforms the Entity.class by adding a new method called _SAP_canDismountOnInput.<br>
     * This method can be overridden by any entity to control wether or not the rider can dismount via sneaking (usually by pressing LSHIFT for the player).
     *
     * @param bytes     the class bytes to be transformed
     * @return the transformed class bytes
     */
    private static byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = ASMHelper.getMethodNode(Opcodes.ACC_PUBLIC, ASMNames.MD_SAP_CAN_DISMOUNT_ON_INPUT);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitInsn(Opcodes.ICONST_1);
        method.visitInsn(Opcodes.IRETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", ASMNames.CL_T_ENTITY, null, l0, l1, 0);
        method.visitLocalVariable("player", ASMNames.CL_T_ENTITY_PLAYER, null, l0, l1, 1);
        method.visitMaxs(1, 2);
        method.visitEnd();

        clazz.methods.add(method);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    /**
     * Transforms the EntityPlayer.class by hooking into the updateRidden method and adding a call to _SAP_canDismountOnInput
     * in order for the ridden entity to control whether or not the rider can dismount via sneaking.
     *
     * @param bytes     the class bytes to be transformed
     * @return the transformed class bytes
     */
    private static byte[] transformPlayer(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(clazz, ASMNames.MD_PLAYER_UPDATE_RIDDEN);

        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_PLAYER_WORLD_OBJ));
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_WORLD_IS_REMOTE));
        LabelNode ln1 = new LabelNode();
        needle.add(new JumpInsnNode(Opcodes.IFNE, ln1));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_PLAYER_IS_SNEAKING, false));
        needle.add(new JumpInsnNode(Opcodes.IFEQ, ln1));

        AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_PLAYER_RIDING_ENTITY));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_CAN_DISMOUNT_ON_INPUT, false));
        injectList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) insertPoint).label));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
