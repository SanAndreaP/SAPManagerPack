package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class TransformEnderman
    implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if( "net.minecraft.entity.monster.EntityEnderman".equals(transformedName) ) {
            ClassNode cn = ASMHelper.createClassNode(basicClass);
            
            // TODO generate SRG name
            this.transformShouldAttackPlayer(ASMHelper.findMethod("shouldAttackPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", cn));
            
            return ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }
        return basicClass;
    }

    private void transformShouldAttackPlayer(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new LabelNode());
        needle.add(new LineNumberNode(-1, new LabelNode()));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 1));
        // TODO generate SRG name
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/EntityPlayer", "inventory", "Lnet/minecraft/entity/player/InventoryPlayer;"));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/player/InventoryPlayer", "armorInventory", "[Lnet/minecraft/item/ItemStack;"));
        needle.add(new InsnNode(Opcodes.ICONST_3));
        needle.add(new InsnNode(Opcodes.AALOAD));
        needle.add(new VarInsnNode(Opcodes.ASTORE, 2));
        
        AbstractInsnNode insertPt = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle);
        
        InsnList newInstr = new InsnList();
        
        LabelNode l0 = new LabelNode();
        newInstr.add(l0);
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 1));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent", "<init>", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z"));
        LabelNode l1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l1);
        
        method.instructions.insertBefore(insertPt, newInstr);
    }
}
