/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class TransformEnderman
        implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if( "net.minecraft.entity.monster.EntityEnderman".equals(transformedName) ) {
            return transformEnderman(bytes);
        }

        return bytes;
    }

    private static byte[] transformEnderman(byte[] bytes) {
        ClassNode clazz = ASMHelper.createClassNode(bytes);
        MethodNode method = ASMHelper.findMethod(clazz, ASMNames.MD_ENDERMAN_SHOULD_ATTACK_PLAYER);

        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_PLAYER_INVENTORY));
        needle.add(ASMHelper.getFieldInsnNode(Opcodes.GETFIELD, ASMNames.FD_INVPLAYER_ARMOR_INVENTORY));
        needle.add(new InsnNode(Opcodes.ICONST_3));
        needle.add(new InsnNode(Opcodes.AALOAD));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 2));

        AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(ASMHelper.getFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FD_SAPUTILS_EVENT_BUS));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, ASMNames.CL_ENDER_FACING_EVENT));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_ENDERFACINGEVENT_INIT, false));
        newInstr.add(ASMHelper.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_EVENT_BUS_POST, false));
        LabelNode l1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l1);

        method.instructions.insertBefore(insertPt, newInstr);

        return ASMHelper.createBytes(clazz, /*ClassWriter.COMPUTE_FRAMES |*/ ClassWriter.COMPUTE_MAXS);
    }
}
