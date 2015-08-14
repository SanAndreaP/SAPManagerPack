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

    private static byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = ASMNames.getNewMethod(Opcodes.ACC_PUBLIC, ASMNames.MD_SAP_CAN_DISMOUNT_ON_INPUT_NEW);
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

    private static byte[] transformPlayer(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMNames.findObfMethod(clazz, ASMNames.MDO_PLAYER_UPDATE_RIDDEN);

        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_ENTITY_WORLDOBJ, ASMNames.CL_ENTITY_PLAYER));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_WORLD_ISREMOTE));
        LabelNode ln1 = new LabelNode();
        needle.add(new JumpInsnNode(Opcodes.IFNE, ln1));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_ENTITY_IS_SNEAKING, ASMNames.CL_ENTITY_PLAYER, false));
        needle.add(new JumpInsnNode(Opcodes.IFEQ, ln1));

        AbstractInsnNode insertPoint = ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_ENTITY_RIDING_ENTITY, ASMNames.CL_ENTITY_PLAYER));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_CAN_DISMOUNT_ON_INPUT, false));
        injectList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) insertPoint).label));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
