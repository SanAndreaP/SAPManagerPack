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

public class TransformEntityCollision
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( "net.minecraft.world.World".equals(transformedName) ) {
            return transformWorld(bytes);
        }

        if( "net.minecraft.entity.Entity".equals(transformedName) ) {
            return transformEntity(bytes);
        }

        return bytes;
    }

    private static byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = ASMNames.getNewMethod(Opcodes.ACC_PUBLIC, ASMNames.MD_SAP_GET_BOUNDIN_GBOX);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(Opcodes.ALOAD, 2);
        method.visitInsn(Opcodes.ARETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", ASMNames.CL_T_ENTITY, null, l0, l1, 0);
        method.visitLocalVariable("entity", ASMNames.CL_T_ENTITY, null, l0, l1, 1);
        method.visitLocalVariable("oldAABB", ASMNames.CL_T_AXIS_ALIGNED_BB, null, l0, l1, 2);
        method.visitMaxs(1, 2);
        method.visitEnd();

        clazz.methods.add(method);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private static byte[] transformWorld(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMNames.findObfMethod(clazz, ASMNames.MDO_GET_COLLIDING_BB);

        InsnList needle = new InsnList();
        LabelNode ln = new LabelNode();
        needle.add(ln);
        needle.add(new LineNumberNode(-1, ln));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 2));
        needle.add(new VarInsnNode(Opcodes.DLOAD, -1));
        needle.add(new VarInsnNode(Opcodes.DLOAD, -1));
        needle.add(new VarInsnNode(Opcodes.DLOAD, -1));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_AABB_EXPAND, false));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_GET_ENTITIES_AABB_EXCLUDE, false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, -1));

        VarInsnNode insertPoint = (VarInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new LabelNode());
        injectList.add(ASMNames.getFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FD_SAPUTILS_EVENT_BUS));
        injectList.add(new TypeInsnNode(Opcodes.NEW, ASMNames.CL_COLLIDING_ENTITY_CHECK_EVENT));
        injectList.add(new InsnNode(Opcodes.DUP));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, insertPoint.var));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        injectList.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_COLLENTITY_CHKEVT_CTOR, false));
        injectList.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_EVENT_BUS_POST, false));
        injectList.add(new InsnNode(Opcodes.POP));

        method.instructions.insert(insertPoint, injectList);

        // insert entity-sensitive bounding box method

        needle = new InsnList();
        ln = new LabelNode();
        needle.add(ln);
        needle.add(new LineNumberNode(-1, ln));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 11));
        needle.add(new VarInsnNode(Opcodes.ILOAD, 12));
        needle.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEINTERFACE, ASMNames.MD_LIST_GET, true));
        needle.add(new TypeInsnNode(Opcodes.CHECKCAST, ASMNames.CL_T_ENTITY));
        needle.add(ASMNames.getObfMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MDO_GET_ENTITY_BOUNDING_BOX, false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, -1));

        insertPoint = (VarInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        injectList = new InsnList();
        injectList.add(new LabelNode());
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 11));
        injectList.add(new VarInsnNode(Opcodes.ILOAD, 12));
        injectList.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEINTERFACE, ASMNames.MD_LIST_GET, true));
        injectList.add(new TypeInsnNode(Opcodes.CHECKCAST, ASMNames.CL_T_ENTITY));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, insertPoint.var));
        needle.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_SAP_GET_BOUNDING_BOX, false));
        injectList.add(new VarInsnNode(Opcodes.ASTORE, insertPoint.var));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
