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
import org.objectweb.asm.tree.LocalVariableNode;
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
            this.transformTeleportTo(ASMHelper.findMethod(ASMNames.M_teleportTo, "(DDD)Z", cn));

            basicClass = ASMHelper.createBytes(cn, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            
            return basicClass;
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

    private void transformTeleportTo(MethodNode method) {
        InsnList needle = new InsnList();
        needle.add(new VarInsnNode(Opcodes.ALOAD, 0));
        needle.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "worldObj", "Lnet/minecraft/world/World;"));
        needle.add(new LdcInsnNode("portal"));
        needle.add(new VarInsnNode(Opcodes.DLOAD, 25));
        needle.add(new VarInsnNode(Opcodes.DLOAD, 27));
        needle.add(new VarInsnNode(Opcodes.DLOAD, 29));
        needle.add(new VarInsnNode(Opcodes.FLOAD, 22));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new VarInsnNode(Opcodes.FLOAD, 23));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new VarInsnNode(Opcodes.FLOAD, 24));
        needle.add(new InsnNode(Opcodes.F2D));
        needle.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnParticle", "(Ljava/lang/String;DDDDDD)V"));

        AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle).getPrevious();

        ASMHelper.removeNeedleFromHaystack(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new VarInsnNode(Opcodes.DLOAD, 25));
        newInstr.add(new VarInsnNode(Opcodes.DLOAD, 27));
        newInstr.add(new VarInsnNode(Opcodes.DLOAD, 29));
        newInstr.add(new VarInsnNode(Opcodes.FLOAD, 22));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new VarInsnNode(Opcodes.FLOAD, 23));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new VarInsnNode(Opcodes.FLOAD, 24));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType", "TELEPORT_FX", "Lde/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "<init>", "(Lnet/minecraft/entity/monster/EntityEnderman;DDDDDDLde/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType;)V"));
        newInstr.add(new VarInsnNode(Opcodes.ASTORE, 31));
        newInstr.add(new LabelNode());
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z"));
        LabelNode l17 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFNE, l17));
        newInstr.add(new LabelNode());
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "worldObj", "Lnet/minecraft/world/World;"));
        newInstr.add(new LdcInsnNode("portal"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getX", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getY", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getZ", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getD1", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getD2", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 31));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "getD3", "()D"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnParticle", "(Ljava/lang/String;DDDDDD)V"));
        newInstr.add(l17);
        
        method.instructions.insert(node, newInstr);
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
        needle.add(new LineNumberNode(-1, l31));
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
        needle.add(new LineNumberNode(-1, l32));
        needle.add(new IincInsnNode(1, 1));
        needle.add(new JumpInsnNode(Opcodes.GOTO, l29));
        needle.add(l30);

        AbstractInsnNode node = ASMHelper.findFirstNodeFromNeedle(method.instructions, needle).getPrevious();

        ASMHelper.removeNeedleFromHaystack(method.instructions, needle);

        InsnList newInstr = new InsnList();
        newInstr.add(new LabelNode());
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lcpw/mods/fml/common/eventhandler/EventBus;"));
        newInstr.add(new TypeInsnNode(Opcodes.NEW, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent"));
        newInstr.add(new InsnNode(Opcodes.DUP));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new InsnNode(Opcodes.DCONST_0));
        newInstr.add(new FieldInsnNode(Opcodes.GETSTATIC, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType", "IDLE_FX", "Lde/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "de/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent", "<init>", "(Lnet/minecraft/entity/monster/EntityEnderman;DDDDDDLde/sanandrew/core/manpack/util/event/entity/EnderSpawnParticleEvent$EnderParticleType;)V"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/eventhandler/EventBus", "post", "(Lcpw/mods/fml/common/eventhandler/Event;)Z"));
        LabelNode label1 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.IFNE, label1));
        newInstr.add(new LabelNode());
        newInstr.add(new InsnNode(Opcodes.ICONST_0));
        newInstr.add(new VarInsnNode(Opcodes.ISTORE, 2));
        newInstr.add(new LabelNode());
        LabelNode l5 = new LabelNode();
        newInstr.add(new JumpInsnNode(Opcodes.GOTO, l5));
        LabelNode l6 = new LabelNode();
        newInstr.add(l6);
        newInstr.add(new FrameNode(Opcodes.F_FULL, 3, new Object[] {"net/minecraft/entity/monster/EntityEnderman", "net/minecraft/world/World", Opcodes.INTEGER}, 0, new Object[] {}));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "worldObj", "Lnet/minecraft/world/World;"));
        newInstr.add(new LdcInsnNode("portal"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posX", "D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new LdcInsnNode(new Double("0.5")));
        newInstr.add(new InsnNode(Opcodes.DSUB));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "width", "F"));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new InsnNode(Opcodes.DMUL));
        newInstr.add(new InsnNode(Opcodes.DADD));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posY", "D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "height", "F"));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new InsnNode(Opcodes.DMUL));
        newInstr.add(new InsnNode(Opcodes.DADD));
        newInstr.add(new LdcInsnNode(new Double("0.25")));
        newInstr.add(new InsnNode(Opcodes.DSUB));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "posZ", "D"));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new LdcInsnNode(new Double("0.5")));
        newInstr.add(new InsnNode(Opcodes.DSUB));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "width", "F"));
        newInstr.add(new InsnNode(Opcodes.F2D));
        newInstr.add(new InsnNode(Opcodes.DMUL));
        newInstr.add(new InsnNode(Opcodes.DADD));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new LdcInsnNode(new Double("0.5")));
        newInstr.add(new InsnNode(Opcodes.DSUB));
        newInstr.add(new LdcInsnNode(new Double("2.0")));
        newInstr.add(new InsnNode(Opcodes.DMUL));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new InsnNode(Opcodes.DNEG));
        newInstr.add(new VarInsnNode(Opcodes.ALOAD, 0));
        newInstr.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/monster/EntityEnderman", "rand", "Ljava/util/Random;"));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/util/Random", "nextDouble", "()D"));
        newInstr.add(new LdcInsnNode(new Double("0.5")));
        newInstr.add(new InsnNode(Opcodes.DSUB));
        newInstr.add(new LdcInsnNode(new Double("2.0")));
        newInstr.add(new InsnNode(Opcodes.DMUL));
        newInstr.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/world/World", "spawnParticle", "(Ljava/lang/String;DDDDDD)V"));
        newInstr.add(new LabelNode());
        newInstr.add(new IincInsnNode(2, 1));
        newInstr.add(l5);
        newInstr.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
        newInstr.add(new VarInsnNode(Opcodes.ILOAD, 2));
        newInstr.add(new InsnNode(Opcodes.ICONST_2));
        newInstr.add(new JumpInsnNode(Opcodes.IF_ICMPLT, l6));
        newInstr.add(label1);

        method.instructions.insert(node, newInstr);
    }
}
