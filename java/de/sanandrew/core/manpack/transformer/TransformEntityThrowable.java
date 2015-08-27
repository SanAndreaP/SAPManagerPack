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
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( "net.minecraft.entity.projectile.EntityThrowable".equals(transformedName) ) {
            return transformLqThrowable(bytes);
        }

        return bytes;
    }

    private static byte[] transformLqThrowable(byte[] bytes) {
        ClassNode classNode = ASMHelper.createClassNode(bytes);

        MethodNode method = ASMHelper.getMethodNode(Opcodes.ACC_PUBLIC, ASMNames.MD_SAP_CAN_IMPACT_ON_LIQUID);

        method.visitCode();
        Label label1 = new Label();
        method.visitLabel(label1);
        method.visitInsn(Opcodes.ICONST_0);
        method.visitInsn(Opcodes.IRETURN);
        Label label2 = new Label();
        method.visitLabel(label2);
        method.visitLocalVariable("this", ASMNames.CL_T_ENTITY_THROWABLE, null, label1, label2, 0);
        method.visitMaxs(0, 0);
        method.visitEnd();
        classNode.methods.add(method);

        method = ASMHelper.findMethod(classNode, ASMNames.MD_THROWABLE_ON_UPDATE);

        InsnList needle = new InsnList();
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_THROWABLE_MOTION_Z));
        needle.add(new InsnNode(Opcodes.DADD));
        needle.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESTATIC, ASMNames.MD_VEC3_CREATE_VECTOR_HELPER, false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 2));
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_THROWABLE_WORLD_OBJ));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
        needle.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_WORLD_RAY_TRACE_BLOCKS, false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, -1));

        VarInsnNode insertPoint = (VarInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new LabelNode());
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_THROWABLE_WORLD_OBJ));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_CAN_IMPACT_ON_LIQUID, false));
        injectList.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_WORLD_RAY_TRACE_BLOCKS_Z, false));
        injectList.add(new VarInsnNode(Opcodes.ASTORE, insertPoint.var));

        method.instructions.insert(insertPoint, injectList);

        return ASMHelper.createBytes(classNode, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
    }
}
