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
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if( "net.minecraft.entity.monster.EntityEnderman".equals(transformedName) ) {
            ClassNode cn = ASMHelper.createClassNode(basicClass);

            transformShouldAttackPlayer(ASMNames.findObfMethod(cn, ASMNames.MDO_ENDERMAN_SHOULD_ATTACK_PLAYER));

            basicClass = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }

        return basicClass;
    }

    private static void transformShouldAttackPlayer(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_ENTPLAYER_INVENTORY));
        needle.add(ASMNames.getObfFieldInsnNode(Opcodes.GETFIELD, ASMNames.FDO_INVPLAYER_ARMORINVENTORY));
        needle.add(new InsnNode(Opcodes.ICONST_3));
        needle.add(new InsnNode(Opcodes.AALOAD));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 2));

        AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(ASMNames.getFieldInsnNode(Opcodes.GETSTATIC, ASMNames.FD_FORGE_EVENT_BUS));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, ASMNames.CL_ENDER_FACING_EVENT));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKESPECIAL, ASMNames.MD_ENDER_FACING_EVENT_CTOR, false));
        newInstr.add(ASMNames.getMethodInsnNode(Opcodes.INVOKEVIRTUAL, ASMNames.MD_EVENT_BUS_POST, false));
        LabelNode l1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l1);

        method.instructions.insertBefore(insertPt, newInstr);
    }
}
