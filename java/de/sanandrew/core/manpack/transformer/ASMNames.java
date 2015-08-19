/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.transformer.ASMObfuscationHelper.NameMapping;
import de.sanandrew.core.manpack.transformer.ASMObfuscationHelper.NameMapping.Type;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ASMNames
{
    /*
     * OBFUSCATED NAMES
     */
    public static final String MDO_ENDERMAN_SHOULD_ATK_PLAYER = "net/minecraft/entity/monster/EntityEnderman/shouldAttackPlayer (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MDO_WORLD_GET_COLLIDING_BB = "net/minecraft/world/World/getCollidingBoundingBoxes (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MDO_AABB_EXPAND = "net/minecraft/util/AxisAlignedBB/expand (DDD)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MDO_WORLD_GET_ENTITIES_EXCLUDE = "net/minecraft/world/World/getEntitiesWithinAABBExcludingEntity (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MDO_ENTITY_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/getBoundingBox ()Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MDO_VEC3_CREATE_VECTOR_HELPER = "net/minecraft/util/Vec3/createVectorHelper (DDD)Lnet/minecraft/util/Vec3;";
    public static final String MDO_WORLD_RAY_TRACE_BLOCKS = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MDO_WORLD_RAY_TRACE_BLOCKS_Z = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MDO_THROWABLE_ON_UPDATE = "net/minecraft/entity/projectile/EntityThrowable/onUpdate ()V";
    public static final String MDO_PLAYER_UPDATE_RIDDEN = "net/minecraft/entity/player/EntityPlayer/updateRidden ()V";
    public static final String MDO_ENTITY_IS_SNEAKING = "net/minecraft/entity/Entity/isSneaking ()Z";
    public static final String MDO_DATAWATCHER_GET_OBJ_STACK = "net/minecraft/entity/DataWatcher/getWatchableObjectItemStack (I)Lnet/minecraft/item/ItemStack;";
    public static final String MDO_DATAWATCHER_UPDATE_OBJ = "net/minecraft/entity/DataWatcher/updateObject (ILjava/lang/Object;)V";
    public static final String MDO_HORSE_INTERACT = "net/minecraft/entity/passive/EntityHorse/interact (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MDO_HORSE_FUNC146085A = "net/minecraft/entity/passive/EntityHorse/func_146085_a (Lnet/minecraft/item/Item;)Z";
    public static final String MDO_HORSE_ENTITY_INIT = "net/minecraft/entity/passive/EntityHorse/entityInit ()V";
    public static final String MDO_HORSE_FUNC110232CE = "net/minecraft/entity/passive/EntityHorse/func_110232_cE ()V";
    public static final String MDO_HORSE_ON_INV_CHANGED = "net/minecraft/entity/passive/EntityHorse/onInventoryChanged (Lnet/minecraft/inventory/InventoryBasic;)V";
    public static final String MDO_HORSE_GET_TOTAL_ARMOR_VAL = "net/minecraft/entity/passive/EntityHorse/getTotalArmorValue ()I";
    public static final String MDO_HORSE_SET_TEXTURE_PATH = "net/minecraft/entity/passive/EntityHorse/setHorseTexturePaths ()V";
    public static final String MDO_HORSE_FUNC110241CB = "net/minecraft/entity/passive/EntityHorse/func_110241_cb ()I";
    public static final String MDO_ITEMSTACK_GET_ITEM = "net/minecraft/item/ItemStack/getItem ()Lnet/minecraft/item/Item;";
    public static final String MDO_HORSE_IS_SADDLED = "net/minecraft/entity/passive/EntityHorse/isHorseSaddled ()Z";
    public static final String MDO_ENTITY_PLAY_SOUND = "net/minecraft/entity/Entity/playSound (Ljava/lang/String;FF)V";
    public static final String MDO_ITEMSTACK_IS_ITEM_EQUAL = "net/minecraft/item/ItemStack/isItemEqual (Lnet/minecraft/item/ItemStack;)Z";
    public static final String MDO_ITEMSTACK_GET_UNLOC_NAME = "net/minecraft/item/ItemStack/getUnlocalizedName ()Ljava/lang/String;";
    public static final String MDO_INVBASIC_GET_STACK_IN_SLOT = "net/minecraft/inventory/InventoryBasic/getStackInSlot (I)Lnet/minecraft/item/ItemStack;";
    public static final String MDO_DATAWATCHER_ADD_OBJECT = "net/minecraft/entity/DataWatcher/addObject (ILjava/lang/Object;)V";

    public static final String FDO_ENTPLAYER_INVENTORY = "net/minecraft/entity/player/EntityPlayer/inventory Lnet/minecraft/entity/player/InventoryPlayer;";
    public static final String FDO_INVPLAYER_ARMORINVENTORY = "net/minecraft/entity/player/InventoryPlayer/armorInventory [Lnet/minecraft/item/ItemStack;";
    public static final String FDO_ENTITY_MOTIONZ = "net/minecraft/entity/Entity/motionZ D";
    public static final String FDO_ENTITY_WORLDOBJ = "net/minecraft/entity/Entity/worldObj Lnet/minecraft/world/World;";
    public static final String FDO_WORLD_ISREMOTE = "net/minecraft/world/World/isRemote Z";
    public static final String FDO_ENTITY_RIDING_ENTITY = "net/minecraft/entity/Entity/ridingEntity Lnet/minecraft/entity/Entity;";
    public static final String FDO_ENTITY_DATAWATCHER = "net/minecraft/entity/Entity/dataWatcher Lnet/minecraft/entity/DataWatcher;";
    public static final String FDO_ITEMS_IRON_SHOVEL = "net/minecraft/init/Items/iron_shovel Lnet/minecraft/item/Item;";
    public static final String FDO_HORSE_ARMOR_VALUES = "net/minecraft/entity/passive/EntityHorse/armorValues [I";
    public static final String FDO_HORSE_FIELD110280BR = "net/minecraft/entity/passive/EntityHorse/field_110280_bR [Ljava/lang/String;";
    public static final String FDO_HORSE_FIELD110286BQ = "net/minecraft/entity/passive/EntityHorse/field_110286_bQ Ljava/lang/String;";
    public static final String FDO_HORSE_CHEST = "net/minecraft/entity/passive/EntityHorse/horseChest Lnet/minecraft/inventory/AnimalChest;";
    public static final String FDO_ITEMS_HORSE_ARMOR_IRON = "net/minecraft/init/Items/iron_horse_armor Lnet/minecraft/item/Item;";
    public static final String FDO_ITEMS_HORSE_ARMOR_DIAMOND = "net/minecraft/init/Items/diamond_horse_armor Lnet/minecraft/item/Item;";

    /*
     * NON-OBFUSCATED NAMES
     */
    public static final String CL_ENTITY = "net/minecraft/entity/Entity";
    public static final String CL_ENDER_FACING_EVENT = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent";
    public static final String CL_COLLIDING_ENTITY_CHECK_EVENT = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent";
    public static final String CL_ENTITY_THROWABLE = "net/minecraft/entity/projectile/EntityThrowable";
    public static final String CL_ENTITY_PLAYER = "net/minecraft/entity/player/EntityPlayer";
    public static final String CL_ENTITY_HORSE = "net/minecraft/entity/passive/EntityHorse";
    public static final String CL_ITEM_STACK = "net/minecraft/item/ItemStack";
    public static final String CL_ITEM_HORSE_ARMOR = "de/sanandrew/core/manpack/item/AItemHorseArmor";
    public static final String CL_STRING_BUILDER = "java/lang/StringBuilder";
    public static final String CL_ANIMAL_CHEST = "net/minecraft/inventory/AnimalChest";

    public static final String CL_T_ENTITY = "Lnet/minecraft/entity/Entity;";
    public static final String CL_T_AXIS_ALIGNED_BB = "Lnet/minecraft/util/AxisAlignedBB;";
    public static final String CL_T_ENTITY_THROWABLE = "Lnet/minecraft/entity/projectile/EntityThrowable;";
    public static final String CL_T_ENTITY_PLAYER = "Lnet/minecraft/entity/player/EntityPlayer;";
    public static final String CL_T_ENTITY_HORSE = "Lnet/minecraft/entity/passive/EntityHorse;";
    public static final String CL_T_ITEM_STACK = "Lnet/minecraft/item/ItemStack;";

    public static final String MD_ENDER_FACING_EVENT_CTOR = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent/<init> (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V";
    public static final String MD_EVENT_BUS_POST = "cpw/mods/fml/common/eventhandler/EventBus/post (Lcpw/mods/fml/common/eventhandler/Event;)Z";
    public static final String MD_LIST_GET = "java/util/List/get (I)Ljava/lang/Object;";
    public static final String MD_SAP_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_SAP_COLLENTITY_CHKEVT_CTOR = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent/<init> (Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V";
    public static final String MD_SAP_CAN_IMPACT_ON_LIQUID = "net/minecraft/entity/projectile/EntityThrowable/_SAP_canImpactOnLiquid ()Z";
    public static final String MD_SAP_CAN_DISMOUNT_ON_INPUT = "net/minecraft/entity/Entity/_SAP_canDismountOnInput (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_SAP_GET_CUSTOM_ARMOR_ITEM = "net/minecraft/entity/passive/EntityHorse/_SAP_getCustomArmorItem ()Lnet/minecraft/item/ItemStack;";
    public static final String MD_SAP_SET_CUSTOM_ARMOR_ITEM = "net/minecraft/entity/passive/EntityHorse/_SAP_setCustomArmorItem (Lnet/minecraft/item/ItemStack;)V";
    public static final String MD_ITEMSTACK_INIT = "net/minecraft/item/ItemStack/<init> (Lnet/minecraft/item/Item;I)V";
    public static final String MD_SAP_GET_ARMOR_VALUE = "de/sanandrew/core/manpack/item/AItemHorseArmor/getArmorValue (Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I";
    public static final String MD_SAP_GET_ARMOR_TEXTURE = "de/sanandrew/core/manpack/item/AItemHorseArmor/getArmorTexture (Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;";
    public static final String MD_STRING_VALUE_OF = "java/lang/String/valueOf (Ljava/lang/Object;)Ljava/lang/String;";
    public static final String MD_STRINGBUILDER_INIT = "java/lang/StringBuilder/<init> (Ljava/lang/String;)V";
    public static final String MD_STRINGBUILDER_APPEND = "java/lang/StringBuilder/append (Ljava/lang/String;)Ljava/lang/StringBuilder;";
    public static final String MD_STRINGBUILDER_TO_STRING = "java/lang/StringBuilder/toString ()Ljava/lang/String;";
    public static final String MD_INTEGER_VALUE_OF = "java/lang/Integer/valueOf (I)Ljava/lang/Integer;";

    public static final String FD_FORGE_EVENT_BUS = "net/minecraftforge/common/MinecraftForge/EVENT_BUS Lcpw/mods/fml/common/eventhandler/EventBus;";
    public static final String FD_SAPUTILS_EVENT_BUS = "de/sanandrew/core/manpack/util/helpers/SAPUtils/EVENT_BUS Lcpw/mods/fml/common/eventhandler/EventBus;";

    static final Pattern OWNERNAME = Pattern.compile("(\\S*)/(.*)");
    public static Triplet<String, String, String[]> getSrgNameMd(String method) {
        Matcher mtch = OWNERNAME.matcher(method);
        if( !mtch.find() ) {
            ModCntManPack.MOD_LOG.log(Level.FATAL, "Method string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        NameMapping map = ASMObfuscationHelper.getInstance().getMapping(method, Type.METHOD);

        String owner = mtch.group(1);
        String[] splitMd = mtch.group(2).split(" ");

        String name = ASMHelper.isMCP ? map.mcpName : map.srgName;
        String[] additData = Arrays.copyOfRange(splitMd, 1, splitMd.length);

        return Triplet.with(owner, name, additData);
    }

    public static Triplet<String, String, String> getSrgNameFd(String field) {
        Matcher mtch = OWNERNAME.matcher(field);
        if( !mtch.find() ) {
            ModCntManPack.MOD_LOG.log(Level.FATAL, "Field string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        NameMapping map = ASMObfuscationHelper.getInstance().getMapping(field.split(" ")[0], Type.FIELD);

        String owner = mtch.group(1);
        String[] splitFd = mtch.group(2).split(" ");

        String name = ASMHelper.isMCP ? map.mcpName : map.srgName;
        String desc = splitFd[1];

        return Triplet.with(owner, name, desc);
    }


//    private static final Pattern OWNER_NAME_DESC_PATTERN = Pattern.compile("(\\S*)\\/(\\S*?) (.*)");

//    public static MethodNode getNewMethod(int access, String method) {
//        String[] methodSplit = method.split(" ");
//
//        String name = methodSplit[0];
//        String desc = methodSplit[1];
//        String signature = methodSplit.length > 2 ? methodSplit[2] : null;
//        String[] throwing = methodSplit.length > 3 ? methodSplit[3].split(";") : null;
//
//        return new MethodNode(access, name, desc, signature, throwing);
//    }
//
//    public static MethodNode findObfMethod(ClassNode cn, String mcpMapping) {
//        return ASMObfuscationHelper.getInstance().findMethod(cn, mcpMapping);
//    }
//
//    public static MethodInsnNode getObfMethodInsnNode(int opcode, String method, boolean intf) {
//        return ASMObfuscationHelper.getInstance().getMethodInsnNode(opcode, method, intf);
//    }
//
//    public static MethodInsnNode getObfMethodInsnNode(int opcode, String method, String owner, boolean intf) {
//        MethodInsnNode node = ASMObfuscationHelper.getInstance().getMethodInsnNode(opcode, method, intf);
//        node.owner = owner;
//        return node;
//    }
//
//    public static MethodInsnNode getMethodInsnNode(int opcode, String method, boolean intf) {
//        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(method);
//        matcher.find();
//        return new MethodInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3), intf);
//    }
//
//    public static void visitObfMethodInsn(MethodNode node, int opcode, String method, boolean intf) {
//        MethodInsnNode methodNode = getObfMethodInsnNode(opcode, method, intf);
//        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
//    }
//
//    public static void visitObfMethodInsn(MethodNode node, int opcode, String method, String owner, boolean intf) {
//        MethodInsnNode methodNode = getObfMethodInsnNode(opcode, method, owner, intf);
//        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
//    }
//
//    public static void visitMethodInsn(MethodNode node, int opcode, String method, boolean intf) {
//        MethodInsnNode methodNode = getMethodInsnNode(opcode, method, intf);
//        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
//    }
//
//    public static FieldInsnNode getObfFieldInsnNode(int opcode, String field) {
//        return ASMObfuscationHelper.getInstance().getFieldInsnNode(opcode, field);
//    }
//
//    public static FieldInsnNode getObfFieldInsnNode(int opcode, String field, String owner) {
//        FieldInsnNode node = ASMObfuscationHelper.getInstance().getFieldInsnNode(opcode, field);
//        node.owner = owner;
//        return node;
//    }
//
//    public static FieldInsnNode getFieldInsnNode(int opcode, String field) {
//        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(field);
//        matcher.find();
//        return new FieldInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3));
//    }
//
//    public static FieldInsnNode getFieldInsnNode(int opcode, String field, String owner) {
//        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(field);
//        matcher.find();
//        return new FieldInsnNode(opcode, owner, matcher.group(2), matcher.group(3));
//    }
//
//    public static void visitObfFieldInsn(MethodNode node, int opcode, String field) {
//        FieldInsnNode fieldNode = getObfFieldInsnNode(opcode, field);
//        node.visitFieldInsn(opcode, fieldNode.owner, fieldNode.name, fieldNode.desc);
//    }
//
//    public static void visitObfFieldInsn(MethodNode node, int opcode, String field, String owner) {
//        FieldInsnNode fieldNode = getObfFieldInsnNode(opcode, field, owner);
//        node.visitFieldInsn(opcode, fieldNode.owner, fieldNode.name, fieldNode.desc);
//    }
}
