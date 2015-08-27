/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ASMNames
{
    private static final Map<String, String> MAPPINGS = new HashMap<>();

    static final Pattern OWNERNAME = Pattern.compile("(\\S*)/(.*)");

    public static final String MD_AABB_EXPAND                   = "net/minecraft/util/AxisAlignedBB/expand (DDD)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_ANIMALCHEST_GET_STACK_IN_SLOT = "net/minecraft/inventory/AnimalChest/getStackInSlot (I)Lnet/minecraft/item/ItemStack;";
    public static final String MD_DATAWATCHER_ADD_OBJECT        = "net/minecraft/entity/DataWatcher/addObject (ILjava/lang/Object;)V";
    public static final String MD_DATAWATCHER_GET_OBJ_STACK     = "net/minecraft/entity/DataWatcher/getWatchableObjectItemStack (I)Lnet/minecraft/item/ItemStack;";
    public static final String MD_DATAWATCHER_UPDATE_OBJ        = "net/minecraft/entity/DataWatcher/updateObject (ILjava/lang/Object;)V";
    public static final String MD_ENDERFACINGEVENT_INIT         = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent/<init> (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V";
    public static final String MD_ENDERMAN_SHOULD_ATTACK_PLAYER = "net/minecraft/entity/monster/EntityEnderman/shouldAttackPlayer (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_ENTITY_GET_BOUNDING_BOX       = "net/minecraft/entity/Entity/getBoundingBox ()Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_EVENT_BUS_POST                = "cpw/mods/fml/common/eventhandler/EventBus/post (Lcpw/mods/fml/common/eventhandler/Event;)Z";
    public static final String MD_HORSE_ENTITY_INIT             = "net/minecraft/entity/passive/EntityHorse/entityInit ()V";
    public static final String MD_HORSE_FUNC110232CE            = "net/minecraft/entity/passive/EntityHorse/func_110232_cE ()V";
    public static final String MD_HORSE_FUNC110241CB            = "net/minecraft/entity/passive/EntityHorse/func_110241_cb ()I";
    public static final String MD_HORSE_FUNC146085A             = "net/minecraft/entity/passive/EntityHorse/func_146085_a (Lnet/minecraft/item/Item;)Z";
    public static final String MD_HORSE_GET_TOTAL_ARMOR_VAL     = "net/minecraft/entity/passive/EntityHorse/getTotalArmorValue ()I";
    public static final String MD_HORSE_INTERACT                = "net/minecraft/entity/passive/EntityHorse/interact (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_HORSE_IS_SADDLED              = "net/minecraft/entity/passive/EntityHorse/isHorseSaddled ()Z";
    public static final String MD_HORSE_ON_INV_CHANGED          = "net/minecraft/entity/passive/EntityHorse/onInventoryChanged (Lnet/minecraft/inventory/InventoryBasic;)V";
    public static final String MD_HORSE_PLAY_SOUND              = "net/minecraft/entity/passive/EntityHorse/playSound (Ljava/lang/String;FF)V";
    public static final String MD_HORSE_SET_TEXTURE_PATH        = "net/minecraft/entity/passive/EntityHorse/setHorseTexturePaths ()V";
    public static final String MD_INTEGER_VALUE_OF              = "java/lang/Integer/valueOf (I)Ljava/lang/Integer;";
    public static final String MD_ITEMSTACK_GET_ITEM            = "net/minecraft/item/ItemStack/getItem ()Lnet/minecraft/item/Item;";
    public static final String MD_ITEMSTACK_GET_UNLOC_NAME      = "net/minecraft/item/ItemStack/getUnlocalizedName ()Ljava/lang/String;";
    public static final String MD_ITEMSTACK_INIT                = "net/minecraft/item/ItemStack/<init> (Lnet/minecraft/item/Item;I)V";
    public static final String MD_ITEMSTACK_IS_ITEM_EQUAL       = "net/minecraft/item/ItemStack/isItemEqual (Lnet/minecraft/item/ItemStack;)Z";
    public static final String MD_LIST_GET                      = "java/util/List/get (I)Ljava/lang/Object;";
    public static final String MD_PLAYER_IS_SNEAKING            = "net/minecraft/entity/player/EntityPlayer/isSneaking ()Z";
    public static final String MD_PLAYER_UPDATE_RIDDEN          = "net/minecraft/entity/player/EntityPlayer/updateRidden ()V";
    public static final String MD_SAP_CAN_DISMOUNT_ON_INPUT     = "net/minecraft/entity/Entity/_SAP_canDismountOnInput (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_SAP_CAN_IMPACT_ON_LIQUID      = "net/minecraft/entity/projectile/EntityThrowable/_SAP_canImpactOnLiquid ()Z";
    public static final String MD_SAP_COLLENTITYCHKEVT_INIT     = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent/<init> (Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V";
    public static final String MD_SAP_ENTITY_GET_BOUNDING_BOX   = "net/minecraft/entity/Entity/_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_SAP_GET_ARMOR_TEXTURE         = "de/sanandrew/core/manpack/item/AItemHorseArmor/getArmorTexture (Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;";
    public static final String MD_SAP_GET_ARMOR_VALUE           = "de/sanandrew/core/manpack/item/AItemHorseArmor/getArmorValue (Lnet/minecraft/entity/passive/EntityHorse;Lnet/minecraft/item/ItemStack;)I";
    public static final String MD_SAP_GET_CUSTOM_ARMOR_ITEM     = "net/minecraft/entity/passive/EntityHorse/_SAP_getCustomArmorItem ()Lnet/minecraft/item/ItemStack;";
    public static final String MD_SAP_SET_CUSTOM_ARMOR_ITEM     = "net/minecraft/entity/passive/EntityHorse/_SAP_setCustomArmorItem (Lnet/minecraft/item/ItemStack;)V";
    public static final String MD_STRING_VALUE_OF               = "java/lang/String/valueOf (Ljava/lang/Object;)Ljava/lang/String;";
    public static final String MD_STRINGBUILDER_APPEND          = "java/lang/StringBuilder/append (Ljava/lang/String;)Ljava/lang/StringBuilder;";
    public static final String MD_STRINGBUILDER_INIT            = "java/lang/StringBuilder/<init> (Ljava/lang/String;)V";
    public static final String MD_STRINGBUILDER_TO_STRING       = "java/lang/StringBuilder/toString ()Ljava/lang/String;";
    public static final String MD_THROWABLE_ON_UPDATE           = "net/minecraft/entity/projectile/EntityThrowable/onUpdate ()V";
    public static final String MD_VEC3_CREATE_VECTOR_HELPER     = "net/minecraft/util/Vec3/createVectorHelper (DDD)Lnet/minecraft/util/Vec3;";
    public static final String MD_WORLD_GET_COLLIDING_BB        = "net/minecraft/world/World/getCollidingBoundingBoxes (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MD_WORLD_GET_ENTITIES_EXCLUDE    = "net/minecraft/world/World/getEntitiesWithinAABBExcludingEntity (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MD_WORLD_RAY_TRACE_BLOCKS        = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MD_WORLD_RAY_TRACE_BLOCKS_Z      = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;";

    public static final String FD_HORSE_ARMOR_VALUES        = "net/minecraft/entity/passive/EntityHorse/armorValues [I";
    public static final String FD_HORSE_CHEST               = "net/minecraft/entity/passive/EntityHorse/horseChest Lnet/minecraft/inventory/AnimalChest;";
    public static final String FD_HORSE_DATAWATCHER         = "net/minecraft/entity/passive/EntityHorse/dataWatcher Lnet/minecraft/entity/DataWatcher;";
    public static final String FD_HORSE_FIELD110280BR       = "net/minecraft/entity/passive/EntityHorse/field_110280_bR [Ljava/lang/String;";
    public static final String FD_HORSE_FIELD110286BQ       = "net/minecraft/entity/passive/EntityHorse/field_110286_bQ Ljava/lang/String;";
    public static final String FD_INVPLAYER_ARMOR_INVENTORY = "net/minecraft/entity/player/InventoryPlayer/armorInventory [Lnet/minecraft/item/ItemStack;";
    public static final String FD_ITEMS_DIAMOND_HORSE_ARMOR = "net/minecraft/init/Items/diamond_horse_armor Lnet/minecraft/item/Item;";
    public static final String FD_ITEMS_IRON_HORSE_ARMOR    = "net/minecraft/init/Items/iron_horse_armor Lnet/minecraft/item/Item;";
    public static final String FD_ITEMS_IRON_SHOVEL         = "net/minecraft/init/Items/iron_shovel Lnet/minecraft/item/Item;";
    public static final String FD_PLAYER_INVENTORY          = "net/minecraft/entity/player/EntityPlayer/inventory Lnet/minecraft/entity/player/InventoryPlayer;";
    public static final String FD_PLAYER_RIDING_ENTITY      = "net/minecraft/entity/player/EntityPlayer/ridingEntity Lnet/minecraft/entity/Entity;";
    public static final String FD_PLAYER_WORLD_OBJ          = "net/minecraft/entity/player/EntityPlayer/worldObj Lnet/minecraft/world/World;";
    public static final String FD_SAPUTILS_EVENT_BUS        = "de/sanandrew/core/manpack/util/helpers/SAPUtils/EVENT_BUS Lcpw/mods/fml/common/eventhandler/EventBus;";
    public static final String FD_THROWABLE_MOTION_Z        = "net/minecraft/entity/projectile/EntityThrowable/motionZ D";
    public static final String FD_THROWABLE_WORLD_OBJ       = "net/minecraft/entity/projectile/EntityThrowable/worldObj Lnet/minecraft/world/World;";
    public static final String FD_WORLD_IS_REMOTE           = "net/minecraft/world/World/isRemote Z";

    public static final String CL_COLLIDING_ENTITY_CHECK_EVENT = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent";
    public static final String CL_ENDER_FACING_EVENT = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent";
    public static final String CL_ENTITY = "net/minecraft/entity/Entity";
    public static final String CL_ITEM_HORSE_ARMOR = "de/sanandrew/core/manpack/item/AItemHorseArmor";
    public static final String CL_ITEM_STACK = "net/minecraft/item/ItemStack";
    public static final String CL_STRING_BUILDER = "java/lang/StringBuilder";

    public static final String CL_T_AXIS_ALIGNED_BB = "Lnet/minecraft/util/AxisAlignedBB;";
    public static final String CL_T_ENTITY = "Lnet/minecraft/entity/Entity;";
    public static final String CL_T_ENTITY_HORSE = "Lnet/minecraft/entity/passive/EntityHorse;";
    public static final String CL_T_ENTITY_PLAYER = "Lnet/minecraft/entity/player/EntityPlayer;";
    public static final String CL_T_ENTITY_THROWABLE = "Lnet/minecraft/entity/projectile/EntityThrowable;";
    public static final String CL_T_ITEM_STACK = "Lnet/minecraft/item/ItemStack;";

    public static final String OR_SAP_ENTITY_GET_BOUNDING_BOX = "public " + MD_SAP_ENTITY_GET_BOUNDING_BOX;
    public static final String OR_SAP_CAN_DISMOUNT_ON_INPUT = "public " + MD_SAP_CAN_DISMOUNT_ON_INPUT;
    public static final String OR_SAP_CAN_IMPACT_ON_LIQUID = "public " + MD_SAP_CAN_IMPACT_ON_LIQUID;

    static {
        MAPPINGS.put(MD_ENDERMAN_SHOULD_ATTACK_PLAYER,  "func_70821_d");
        MAPPINGS.put(MD_WORLD_GET_COLLIDING_BB,         "func_72945_a");
        MAPPINGS.put(MD_AABB_EXPAND,                    "func_72314_b");
        MAPPINGS.put(MD_WORLD_GET_ENTITIES_EXCLUDE,     "func_72839_b");
        MAPPINGS.put(MD_ENTITY_GET_BOUNDING_BOX,        "func_70046_E");
        MAPPINGS.put(MD_THROWABLE_ON_UPDATE,            "func_70071_h_");
        MAPPINGS.put(MD_VEC3_CREATE_VECTOR_HELPER,      "func_72443_a");
        MAPPINGS.put(MD_WORLD_RAY_TRACE_BLOCKS,         "func_72933_a");
        MAPPINGS.put(MD_WORLD_RAY_TRACE_BLOCKS_Z,       "func_72901_a");
        MAPPINGS.put(MD_PLAYER_IS_SNEAKING,             "func_70093_af");
        MAPPINGS.put(MD_PLAYER_UPDATE_RIDDEN,           "func_70098_U");
        MAPPINGS.put(MD_HORSE_INTERACT,                 "func_70085_c");
        MAPPINGS.put(MD_HORSE_FUNC146085A,              "func_146085_a");
        MAPPINGS.put(MD_HORSE_ENTITY_INIT,              "func_70088_a");
        MAPPINGS.put(MD_HORSE_FUNC110232CE,             "func_110232_cE");
        MAPPINGS.put(MD_HORSE_ON_INV_CHANGED,           "func_76316_a");
        MAPPINGS.put(MD_HORSE_GET_TOTAL_ARMOR_VAL,      "func_70658_aO");
        MAPPINGS.put(MD_HORSE_SET_TEXTURE_PATH,         "func_110247_cG");
        MAPPINGS.put(MD_DATAWATCHER_GET_OBJ_STACK,      "func_82710_f");
        MAPPINGS.put(MD_DATAWATCHER_UPDATE_OBJ,         "func_75692_b");
        MAPPINGS.put(MD_HORSE_FUNC110241CB,             "func_110241_cb");
        MAPPINGS.put(MD_ITEMSTACK_GET_ITEM,             "func_77973_b");
        MAPPINGS.put(MD_HORSE_IS_SADDLED,               "func_110257_ck");
        MAPPINGS.put(MD_HORSE_PLAY_SOUND,               "func_85030_a");
        MAPPINGS.put(MD_ITEMSTACK_IS_ITEM_EQUAL,        "func_77969_a");
        MAPPINGS.put(MD_ITEMSTACK_GET_UNLOC_NAME,       "func_77977_a");
        MAPPINGS.put(MD_ANIMALCHEST_GET_STACK_IN_SLOT,  "func_70301_a");
        MAPPINGS.put(MD_DATAWATCHER_ADD_OBJECT,         "func_75682_a");

        MAPPINGS.put(FD_PLAYER_INVENTORY,           "field_71071_by");
        MAPPINGS.put(FD_INVPLAYER_ARMOR_INVENTORY,  "field_70460_b");
        MAPPINGS.put(FD_THROWABLE_MOTION_Z,         "field_70179_y");
        MAPPINGS.put(FD_THROWABLE_WORLD_OBJ,        "field_70170_p");
        MAPPINGS.put(FD_PLAYER_WORLD_OBJ,           "field_70170_p");
        MAPPINGS.put(FD_WORLD_IS_REMOTE,            "field_72995_K");
        MAPPINGS.put(FD_PLAYER_RIDING_ENTITY,       "field_70154_o");
        MAPPINGS.put(FD_HORSE_DATAWATCHER,          "field_70180_af");
        MAPPINGS.put(FD_ITEMS_IRON_SHOVEL,          "field_151037_a");
        MAPPINGS.put(FD_HORSE_ARMOR_VALUES,         "field_110272_by");
        MAPPINGS.put(FD_HORSE_FIELD110280BR,        "field_110280_bR");
        MAPPINGS.put(FD_HORSE_FIELD110286BQ,        "field_110286_bQ");
        MAPPINGS.put(FD_HORSE_CHEST,                "field_110296_bG");
        MAPPINGS.put(FD_ITEMS_IRON_HORSE_ARMOR,     "field_151138_bX");
        MAPPINGS.put(FD_ITEMS_DIAMOND_HORSE_ARMOR,  "field_151125_bZ");
    }
    public static Triplet<String, String, String[]> getSrgNameMd(String method) {
        Matcher mtch = OWNERNAME.matcher(method);
        if( !mtch.find() ) {
            ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, "Method string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        String srgName = ASMHelper.isMCP ? null : MAPPINGS.get(method);

        String owner = mtch.group(1);
        String[] splitMd = mtch.group(2).split(" ");

        String name = srgName == null ? splitMd[0] : srgName;
        String[] additData = Arrays.copyOfRange(splitMd, 1, splitMd.length);

        return Triplet.with(owner, name, additData);
    }

    public static Triplet<String, String, String> getSrgNameFd(String field) {
        Matcher mtch = OWNERNAME.matcher(field);
        if( !mtch.find() ) {
            ManPackLoadingPlugin.MOD_LOG.log(Level.FATAL, "Field string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        String srgName = ASMHelper.isMCP ? null : MAPPINGS.get(field);

        String owner = mtch.group(1);
        String[] splitFd = mtch.group(2).split(" ");

        String name = srgName == null ? splitFd[0] : srgName;
        String desc = splitFd[1];

        return Triplet.with(owner, name, desc);
    }
}
