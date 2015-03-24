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

            transformShouldAttackPlayer(ASMHelper.findMethod(cn, ASMNames.M_shouldAttackPlayer, "(Lnet/minecraft/entity/player/EntityPlayer;)Z"));

            basicClass = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }

        return basicClass;
    }

    private static void transformShouldAttackPlayer(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", ASMNames.F_inventory, "Lnet/minecraft/entity/player/InventoryPlayer;"));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", ASMNames.F_armorInventory, "[Lnet/minecraft/item/ItemStack;"));
        needle.add(new InsnNode(Opcodes.ICONST_3));
        needle.add(new InsnNode(Opcodes.AALOAD));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 2));

        AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent", "<init>", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V", false));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z", false));
        LabelNode l1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l1);

        method.instructions.insertBefore(insertPt, newInstr);
    }
}
