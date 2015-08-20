/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import de.sanandrew.core.manpack.mod.ModCntManPack;
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

    public static final String MDO_ENDERMAN_SHOULD_ATK_PLAYER = "net/minecraft/entity/monster/EntityEnderman/shouldAttackPlayer (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_WORLD_GET_COLLIDING_BB = "net/minecraft/world/World/getCollidingBoundingBoxes (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MD_AABB_EXPAND = "net/minecraft/util/AxisAlignedBB/expand (DDD)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_WORLD_GET_ENTITIES_EXCLUDE = "net/minecraft/world/World/getEntitiesWithinAABBExcludingEntity (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MD_ENTITY_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/getBoundingBox ()Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_VEC3_CREATE_VECTOR_HELPER = "net/minecraft/util/Vec3/createVectorHelper (DDD)Lnet/minecraft/util/Vec3;";
    public static final String MD_WORLD_RAY_TRACE_BLOCKS = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MD_WORLD_RAY_TRACE_BLOCKS_Z = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MD_THROWABLE_ON_UPDATE = "net/minecraft/entity/projectile/EntityThrowable/onUpdate ()V";
    public static final String MD_PLAYER_UPDATE_RIDDEN = "net/minecraft/entity/player/EntityPlayer/updateRidden ()V";
    public static final String MD_PLAYER_IS_SNEAKING = "net/minecraft/entity/player/EntityPlayer/isSneaking ()Z";
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

    public static final String FD_PLAYER_INVENTORY = "net/minecraft/entity/player/EntityPlayer/inventory Lnet/minecraft/entity/player/InventoryPlayer;";
    public static final String FD_INVPLAYER_ARMOR_INVENTORY = "net/minecraft/entity/player/InventoryPlayer/armorInventory [Lnet/minecraft/item/ItemStack;";
    public static final String FD_THROWABLE_MOTION_Z = "net/minecraft/entity/projectile/EntityThrowable/motionZ D";
    public static final String FD_THROWABLE_WORLD_OBJ = "net/minecraft/entity/projectile/EntityThrowable/worldObj Lnet/minecraft/world/World;";
    public static final String FD_PLAYER_WORLD_OBJ = "net/minecraft/entity/player/EntityPlayer/worldObj Lnet/minecraft/world/World;";
    public static final String FD_WORLD_IS_REMOTE = "net/minecraft/world/World/isRemote Z";
    public static final String FD_PLAYER_RIDING_ENTITY = "net/minecraft/entity/player/EntityPlayer/ridingEntity Lnet/minecraft/entity/Entity;";
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
    public static final String CL_ENDER_FACING_EVENT = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent";
    public static final String CL_COLLIDING_ENTITY_CHECK_EVENT = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent";
    public static final String CL_ENTITY_THROWABLE = "net/minecraft/entity/projectile/EntityThrowable";
    public static final String CL_ENTITY_PLAYER = "net/minecraft/entity/player/EntityPlayer";
    public static final String CL_ENTITY_HORSE = "net/minecraft/entity/passive/EntityHorse";
    public static final String CL_ITEM_STACK = "net/minecraft/item/ItemStack";
    public static final String CL_ITEM_HORSE_ARMOR = "de/sanandrew/core/manpack/item/AItemHorseArmor";
    public static final String CL_STRING_BUILDER = "java/lang/StringBuilder";
    public static final String CL_ANIMAL_CHEST = "net/minecraft/inventory/AnimalChest";
    public static final String CL_ENTITY = "net/minecraft/entity/Entity";

    public static final String CL_T_ENTITY = "Lnet/minecraft/entity/Entity;";
    public static final String CL_T_AXIS_ALIGNED_BB = "Lnet/minecraft/util/AxisAlignedBB;";
    public static final String CL_T_ENTITY_THROWABLE = "Lnet/minecraft/entity/projectile/EntityThrowable;";
    public static final String CL_T_ENTITY_PLAYER = "Lnet/minecraft/entity/player/EntityPlayer;";
    public static final String CL_T_ENTITY_HORSE = "Lnet/minecraft/entity/passive/EntityHorse;";
    public static final String CL_T_ITEM_STACK = "Lnet/minecraft/item/ItemStack;";

    public static final String MD_ENDER_FACING_EVENT_CTOR = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent/<init> (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V";
    public static final String MD_EVENT_BUS_POST = "cpw/mods/fml/common/eventhandler/EventBus/post (Lcpw/mods/fml/common/eventhandler/Event;)Z";
    public static final String MD_LIST_GET = "java/util/List/get (I)Ljava/lang/Object;";
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

    public static final String MD_SAP_ENTITY_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_ENDERMAN_SHOULD_ATTACK_PLAYER = "net/minecraft/entity/monster/EntityEnderman/shouldAttackPlayer (Lnet/minecraft/entity/player/EntityPlayer;)Z";

    static {
        MAPPINGS.put(MD_ENDERMAN_SHOULD_ATTACK_PLAYER, "func_70821_d");
        MAPPINGS.put(MD_WORLD_GET_COLLIDING_BB, "func_72945_a");
        MAPPINGS.put(MD_AABB_EXPAND, "func_72314_b");
        MAPPINGS.put(MD_WORLD_GET_ENTITIES_EXCLUDE, "func_72839_b");
        MAPPINGS.put(MD_ENTITY_GET_BOUNDING_BOX, "func_70046_E");
        MAPPINGS.put(MD_THROWABLE_ON_UPDATE, "func_70071_h_");
        MAPPINGS.put(MD_VEC3_CREATE_VECTOR_HELPER, "func_72443_a");
        MAPPINGS.put(MD_WORLD_RAY_TRACE_BLOCKS, "func_72933_a");
        MAPPINGS.put(MD_WORLD_RAY_TRACE_BLOCKS_Z, "func_72901_a");
        MAPPINGS.put(MD_PLAYER_IS_SNEAKING, "func_70093_af");
        MAPPINGS.put(MD_PLAYER_UPDATE_RIDDEN, "func_70098_U");

        MAPPINGS.put(FD_PLAYER_INVENTORY, "field_71071_by");
        MAPPINGS.put(FD_INVPLAYER_ARMOR_INVENTORY, "field_70460_b");
        MAPPINGS.put(FD_THROWABLE_MOTION_Z, "field_70179_y");
        MAPPINGS.put(FD_THROWABLE_WORLD_OBJ, "field_70170_p");
        MAPPINGS.put(FD_PLAYER_WORLD_OBJ, "field_70170_p");
        MAPPINGS.put(FD_WORLD_IS_REMOTE, "field_72995_K");
        MAPPINGS.put(FD_PLAYER_RIDING_ENTITY, "field_70154_o");
    }

    static final Pattern OWNERNAME = Pattern.compile("(\\S*)/(.*)");
    public static Triplet<String, String, String[]> getSrgNameMd(String method) {
        Matcher mtch = OWNERNAME.matcher(method);
        if( !mtch.find() ) {
            ModCntManPack.MOD_LOG.log(Level.FATAL, "Method string does not match pattern!");
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
            ModCntManPack.MOD_LOG.log(Level.FATAL, "Field string does not match pattern!");
            throw new RuntimeException("SRG-Name not found!");
        }

        String srgName = ASMHelper.isMCP ? null : MAPPINGS.get(field);

        String owner = mtch.group(1);
        String[] splitFd = mtch.group(2).split(" ");

        String name = srgName == null ? splitFd[0] : srgName;
        String desc = splitFd[1];

        return Triplet.with(owner, name, desc);
    }

    private static boolean isInitialized = false;

    @Deprecated public static String F_isBadEffect;
    @Deprecated public static String F_attackingPlayer;
    @Deprecated public static String F_recentlyHit;
    @Deprecated public static String F_motionZ;
    @Deprecated public static String F_worldObj;
    @Deprecated public static String F_dataWatcher;
    @Deprecated public static String F_horseChest;
    @Deprecated public static String F_field110280bR;
    @Deprecated public static String F_field110286bQ;
    @Deprecated public static String F_ironShovel;
    @Deprecated public static String F_iron_horse_armor;
    @Deprecated public static String F_diamondHorseArmor;
    @Deprecated public static String F_armorValues;
    @Deprecated public static String F_isRemote;
    @Deprecated public static String F_ridingEntity;
    @Deprecated public static String F_inventory;
    @Deprecated public static String F_armorInventory;
    @Deprecated public static String F_posX;
    @Deprecated public static String F_posY;
    @Deprecated public static String F_posZ;
    @Deprecated public static String F_rand;
    @Deprecated public static String F_width;
    @Deprecated public static String F_height;
    @Deprecated public static String F_selectedButton;

    @Deprecated public static String M_isBadEffect;
    @Deprecated public static String M_createVectorHelper;
    @Deprecated public static String M_onUpdate;
    @Deprecated public static String M_rayTraceBlocks;
    @Deprecated public static String M_rayTraceBlocksB;
    @Deprecated public static String M_updateObject;
    @Deprecated public static String M_getStackInSlot;
    @Deprecated public static String M_addObject;
    @Deprecated public static String M_getItem;
    @Deprecated public static String M_getWatchableObjectIS;
    @Deprecated public static String M_isItemEqual;
    @Deprecated public static String M_playSound;
    @Deprecated public static String M_getUnlocalizedName;
    @Deprecated public static String M_interact;
    @Deprecated public static String M_func146085a;
    @Deprecated public static String M_entityInit;
    @Deprecated public static String M_func110232cE;
    @Deprecated public static String M_setHorseTexturePaths;
    @Deprecated public static String M_isHorseSaddled;
    @Deprecated public static String M_onInventoryChanged;
    @Deprecated public static String M_func110241cb;
    @Deprecated public static String M_getTotalArmorValue;
    @Deprecated public static String M_isSneaking;
    @Deprecated public static String M_updateRidden;
    @Deprecated public static String M_shouldAttackPlayer;
    @Deprecated public static String M_onLivingUpdate;
    @Deprecated public static String M_teleportTo;
    @Deprecated public static String M_spawnParticle;
    @Deprecated public static String M_getEntityRenderObj;
    @Deprecated public static String M_getEntityClsRenderObj;
    @Deprecated public static String M_renderHand;
    @Deprecated public static String M_getBoundingBox;
    @Deprecated public static String M_getCollidingBBoxes;
    @Deprecated public static String M_aabbExpand;
    @Deprecated public static String M_getEntitiesExclude;

    public static void initialize() {
        if( isInitialized ) {
            return;
        }

        F_isBadEffect =       ASMHelper.getRemappedMF("isBadEffect",         "field_76418_K");
        F_attackingPlayer =   ASMHelper.getRemappedMF("attackingPlayer",     "field_70717_bb");
        F_recentlyHit =       ASMHelper.getRemappedMF("recentlyHit",         "field_70718_bc");
        F_motionZ =           ASMHelper.getRemappedMF("motionZ",             "field_70179_y");
        F_worldObj =          ASMHelper.getRemappedMF("worldObj",            "field_70170_p");
        F_dataWatcher =       ASMHelper.getRemappedMF("dataWatcher",         "field_70180_af");
        F_horseChest =        ASMHelper.getRemappedMF("horseChest",          "field_110296_bG");
        F_field110280bR =     ASMHelper.getRemappedMF("field_110280_bR",     "field_110280_bR");
        F_field110286bQ =     ASMHelper.getRemappedMF("field_110286_bQ",     "field_110286_bQ");
        F_ironShovel =        ASMHelper.getRemappedMF("iron_shovel",         "field_151037_a");
        F_iron_horse_armor =  ASMHelper.getRemappedMF("iron_horse_armor",    "field_151138_bX");
        F_diamondHorseArmor = ASMHelper.getRemappedMF("diamond_horse_armor", "field_151125_bZ");
        F_armorValues =       ASMHelper.getRemappedMF("armorValues",         "field_110272_by");
        F_isRemote =          ASMHelper.getRemappedMF("isRemote",            "field_72995_K");
        F_ridingEntity =      ASMHelper.getRemappedMF("ridingEntity",        "field_70154_o");
        F_inventory =         ASMHelper.getRemappedMF("inventory",           "field_71071_by");
        F_armorInventory =    ASMHelper.getRemappedMF("armorInventory",      "field_70460_b");
        F_posX =              ASMHelper.getRemappedMF("posX",                "field_70165_t");
        F_posY =              ASMHelper.getRemappedMF("posY",                "field_70163_u");
        F_posZ =              ASMHelper.getRemappedMF("posZ",                "field_70161_v");
        F_rand =              ASMHelper.getRemappedMF("rand",                "field_70146_Z");
        F_width =             ASMHelper.getRemappedMF("width",               "field_70130_N");
        F_height =            ASMHelper.getRemappedMF("height",              "field_70131_O");
        F_selectedButton =    ASMHelper.getRemappedMF("selectedButton",      "field_73883_a");

        M_isBadEffect =           ASMHelper.getRemappedMF("isBadEffect",                 "func_76398_f");
        M_createVectorHelper =    ASMHelper.getRemappedMF("createVectorHelper",          "func_72443_a");
        M_onUpdate =              ASMHelper.getRemappedMF("onUpdate",                    "func_70071_h_");
        M_rayTraceBlocks =        ASMHelper.getRemappedMF("rayTraceBlocks",              "func_72933_a");
        M_rayTraceBlocksB =       ASMHelper.getRemappedMF("rayTraceBlocks",              "func_72901_a");
        M_updateObject =          ASMHelper.getRemappedMF("updateObject",                "func_75692_b");
        M_getStackInSlot =        ASMHelper.getRemappedMF("getStackInSlot",              "func_70301_a");
        M_addObject =             ASMHelper.getRemappedMF("addObject",                   "func_75682_a");
        M_getItem =               ASMHelper.getRemappedMF("getItem",                     "func_77973_b");
        M_getWatchableObjectIS =  ASMHelper.getRemappedMF("getWatchableObjectItemStack", "func_82710_f");
        M_isItemEqual =           ASMHelper.getRemappedMF("isItemEqual",                 "func_77969_a");
        M_playSound =             ASMHelper.getRemappedMF("playSound",                   "func_85030_a");
        M_getUnlocalizedName =    ASMHelper.getRemappedMF("getUnlocalizedName",          "func_77977_a");
        M_interact =              ASMHelper.getRemappedMF("interact",                    "func_70085_c");
        M_func146085a =           ASMHelper.getRemappedMF("func_146085_a",               "func_146085_a");
        M_entityInit =            ASMHelper.getRemappedMF("entityInit",                  "func_70088_a");
        M_func110232cE =          ASMHelper.getRemappedMF("func_110232_cE",              "func_110232_cE");
        M_setHorseTexturePaths =  ASMHelper.getRemappedMF("setHorseTexturePaths",        "func_110247_cG");
        M_isHorseSaddled =        ASMHelper.getRemappedMF("isHorseSaddled",              "func_110257_ck");
        M_onInventoryChanged =    ASMHelper.getRemappedMF("onInventoryChanged",          "func_76316_a");
        M_func110241cb =          ASMHelper.getRemappedMF("func_110241_cb",              "func_110241_cb");
        M_getTotalArmorValue =    ASMHelper.getRemappedMF("getTotalArmorValue",          "func_70658_aO");
        M_isSneaking =            ASMHelper.getRemappedMF("isSneaking",                  "func_70093_af");
        M_updateRidden =          ASMHelper.getRemappedMF("updateRidden",                "func_70098_U");
        M_shouldAttackPlayer =    ASMHelper.getRemappedMF("shouldAttackPlayer",          "func_70821_d");
        M_onLivingUpdate =        ASMHelper.getRemappedMF("onLivingUpdate",              "func_70636_d");
        M_teleportTo =            ASMHelper.getRemappedMF("teleportTo",                  "func_70825_j");
        M_spawnParticle =         ASMHelper.getRemappedMF("spawnParticle",               "func_72869_a");
        M_getEntityRenderObj =    ASMHelper.getRemappedMF("getEntityRenderObject",       "func_78713_a");
        M_getEntityClsRenderObj = ASMHelper.getRemappedMF("getEntityClassRenderObject",  "func_78715_a");
        M_renderHand =            ASMHelper.getRemappedMF("renderHand",                  "func_78476_b");
        M_getBoundingBox =        ASMHelper.getRemappedMF("getBoundingBox",              "func_70046_E");
        M_getCollidingBBoxes =    ASMHelper.getRemappedMF("getCollidingBoundingBoxes",   "func_72945_a");
        M_aabbExpand =            ASMHelper.getRemappedMF("expand",                      "func_72314_b");
        M_getEntitiesExclude =    ASMHelper.getRemappedMF("getEntitiesWithinAABBExcludingEntity", "func_94576_a");

        isInitialized = true;
    }
}
