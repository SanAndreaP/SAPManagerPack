package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
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
            
            this.transformShouldAttackPlayer(ASMHelper.findMethod(ASMNames.M_shouldAttackPlayer, "(Lnet/minecraft/entity/player/EntityPlayer;)Z", cn));
            this.transformOnLivingUpdate(ASMHelper.findMethod(ASMNames.M_onLivingUpdate, "()V", cn));
            
            return ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        }
        return basicClass;
    }

    private void transformShouldAttackPlayer(MethodNode method) {
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
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent", "<init>", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z"));
        LabelNode l1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFEQ, l1));
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new InsnNode(Opcodes.IRETURN));
        newInstr.add(l1);
        
        method.instructions.insertBefore(insertPt, newInstr);
    }
    
    private void transformOnLivingUpdate(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        needle.add(new InsnNode(Opcodes.ICONST_0));
        needle.add(new VarInsnNode(Opcodes.ISTORE, 1));
        LabelNode l29 = new LabelNode();
        needle.add(l29);
        needle.add(new FrameNode(Opcodes.F_APPEND,1, new Object[] {Opcodes.INTEGER}, 0, null));
        needle.add(new VarInsnNode(Opcodes.ILOAD, 1));
        needle.add(new InsnNode(Opcodes.ICONST_2));
        LabelNode l30 = new LabelNode();
        needle.add(new JumpInsnNode(Opcodes.IF_ICMPGE, l30));
        LabelNode l31 = new LabelNode();
        needle.add(l31);
        needle.add(new LineNumberNode(211, l31));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "worldObj", "Lnet/minecraft/world/World;"));
        needle.add(new LdcInsnNode("portal"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posX", "D"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new LdcInsnNode(new Double("0.5")));
        needle.add(new InsnNode(Opcodes.DSUB));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "width", "F"));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new InsnNode(Opcodes.DMUL));
        needle.add(new InsnNode(Opcodes.DADD));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posY", "D"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "height", "F"));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new InsnNode(Opcodes.DMUL));
        needle.add(new InsnNode(Opcodes.DADD));
        needle.add(new LdcInsnNode(new Double("0.25")));
        needle.add(new InsnNode(Opcodes.DSUB));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posZ", "D"));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new LdcInsnNode(new Double("0.5")));
        needle.add(new InsnNode(Opcodes.DSUB));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "width", "F"));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new InsnNode(Opcodes.DMUL));
        needle.add(new InsnNode(Opcodes.DADD));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new LdcInsnNode(new Double("0.5")));
        needle.add(new InsnNode(Opcodes.DSUB));
        needle.add(new LdcInsnNode(new Double("2.0")));
        needle.add(new InsnNode(Opcodes.DMUL));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new InsnNode(Opcodes.DNEG));
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        needle.add(new LdcInsnNode(new Double("0.5")));
        needle.add(new InsnNode(Opcodes.DSUB));
        needle.add(new LdcInsnNode(new Double("2.0")));
        needle.add(new InsnNode(Opcodes.DMUL));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnParticle", "(Ljava/lang/String;DDDDDD)V"));
        LabelNode l32 = new LabelNode();
        needle.add(l32);
        needle.add(new LineNumberNode(209, l32));
        needle.add(new IincInsnNode(1, 1));
        needle.add(new JumpInsnNode(Opcodes.GOTO, l29));
        needle.add(l30);
        
        AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle).getPrevious();
        
        ASMHelper.removeNeedleFromHaystack(method.instructions, needle);
        
        
    }
}
