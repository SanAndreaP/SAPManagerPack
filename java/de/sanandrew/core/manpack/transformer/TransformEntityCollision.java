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
        if( transformedName.equals("net.minecraft.world.World") ) {
            return transformWorld(bytes);
        }

        if( transformedName.equals("net.minecraft.entity.Entity") ) {
            return transformEntity(bytes);
        }

        return bytes;
    }

    private static byte[] transformEntity(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);

        MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, "_SAP_getBoundingBox", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;", null, null);
        method.visitCode();
        Label l0 = new Label();
        method.visitLabel(l0);
        method.visitVarInsn(Opcodes.ALOAD, 2);
        method.visitInsn(Opcodes.ARETURN);
        Label l1 = new Label();
        method.visitLabel(l1);
        method.visitLocalVariable("this", "Lnet/minecraft/entity/Entity;", null, l0, l1, 0);
        method.visitLocalVariable("entity", "Lnet/minecraft/entity/Entity;", null, l0, l1, 1);
        method.visitLocalVariable("oldAABB", "Lnet/minecraft/util/AxisAlignedBB;", null, l0, l1, 2);
        method.visitMaxs(1, 2);
        method.visitEnd();

        clazz.methods.add(method);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
        return bytes;
    }

    private static byte[] transformWorld(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(clazz, ASMNames.M_getCollidingBBoxes, "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;");

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
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/util/AxisAlignedBB", ASMNames.M_aabbExpand, "(DDD)Lnet/minecraft/util/AxisAlignedBB;", false));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", ASMNames.M_getEntitiesExclude, "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;", false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, -1));

        VarInsnNode insertPoint = (VarInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        InsnList injectList = new InsnList();
        injectList.add(new LabelNode());
        injectList.add(new FieldInsnNode(Opcodes.GETSTATIC, "de/sanandrew/core/manpack/util/helpers/SAPUtils", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        injectList.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent"));
        injectList.add(new InsnNode(Opcodes.DUP));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 0));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, insertPoint.var));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 2));
        injectList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent", "<init>", "(Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V", false));
        injectList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false));
        injectList.add(new InsnNode(Opcodes.POP));

        method.instructions.insert(insertPoint, injectList);

        // insert entity-sensitive bounding box method

        needle = new InsnList();
        ln = new LabelNode();
        needle.add(ln);
        needle.add(new LineNumberNode(-1, ln));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 11));
        needle.add(new VarInsnNode(Opcodes.ILOAD, 12));
        needle.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true));
        needle.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/entity/Entity"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", ASMNames.M_getBoundingBox, "()Lnet/minecraft/util/AxisAlignedBB;", false));
        needle.add(new VarInsnNode(Opcodes.ASTORE, -1));

        insertPoint = (VarInsnNode) ASMHelper.findLastNodeFromNeedle(method.instructions, needle);

        injectList = new InsnList();
        injectList.add(new LabelNode());
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 11));
        injectList.add(new VarInsnNode(Opcodes.ILOAD, 12));
        injectList.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "get", "(I)Ljava/lang/Object;", true));
        injectList.add(new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/entity/Entity"));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, 1));
        injectList.add(new VarInsnNode(Opcodes.ALOAD, insertPoint.var));
        injectList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/entity/Entity", "_SAP_getBoundingBox", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;", false));
        injectList.add(new VarInsnNode(Opcodes.ASTORE, insertPoint.var));

        method.instructions.insert(insertPoint, injectList);

        bytes = ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);

        return bytes;
    }
}
