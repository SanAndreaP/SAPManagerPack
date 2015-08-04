/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

//TODO: get the SRG name from the mcp_srg.srg provided by the manager pack!
public final class ASMNames
{
    private static boolean isInitialized = false;

    public static String F_isBadEffect;
    public static String F_attackingPlayer;
    public static String F_recentlyHit;
    public static String F_motionZ;
    public static String F_worldObj;
    public static String F_dataWatcher;
    public static String F_horseChest;
    public static String F_field110280bR;
    public static String F_field110286bQ;
    public static String F_ironShovel;
    public static String F_iron_horse_armor;
    public static String F_diamondHorseArmor;
    public static String F_armorValues;
    public static String F_isRemote;
    public static String F_ridingEntity;
    public static String F_inventory;
    public static String F_armorInventory;
    public static String F_posX;
    public static String F_posY;
    public static String F_posZ;
    public static String F_rand;
    public static String F_width;
    public static String F_height;
    public static String F_selectedButton;

    public static String M_isBadEffect;
    public static String M_createVectorHelper;
    public static String M_onUpdate;
    public static String M_rayTraceBlocks;
    public static String M_rayTraceBlocksB;
    public static String M_updateObject;
    public static String M_getStackInSlot;
    public static String M_addObject;
    public static String M_getItem;
    public static String M_getWatchableObjectIS;
    public static String M_isItemEqual;
    public static String M_playSound;
    public static String M_getUnlocalizedName;
    public static String M_interact;
    public static String M_func146085a;
    public static String M_entityInit;
    public static String M_func110232cE;
    public static String M_setHorseTexturePaths;
    public static String M_isHorseSaddled;
    public static String M_onInventoryChanged;
    public static String M_func110241cb;
    public static String M_getTotalArmorValue;
    public static String M_isSneaking;
    public static String M_updateRidden;
    public static String M_shouldAttackPlayer;
    public static String M_onLivingUpdate;
    public static String M_teleportTo;
    public static String M_spawnParticle;
    public static String M_getEntityRenderObj;
    public static String M_getEntityClsRenderObj;
    public static String M_renderHand;
    public static String M_getBoundingBox;
    public static String M_getCollidingBBoxes;

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

        isInitialized = true;
    }
}
