package de.sanandrew.core.manpack.transformer;

public class ASMNames
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
    public static String F_func110241cb;
    public static String F_armorValues;
    public static String F_isRemote;
    public static String F_ridingEntity;

    public static String M_isBadEffect;
    public static String M_getVecFromPool;
    public static String M_onUpdate;
    public static String M_rayTraceBlocks;
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
        F_func110241cb =      ASMHelper.getRemappedMF("func_110241_cb",      "func_110241_cb");
        F_armorValues =       ASMHelper.getRemappedMF("armorValues",         "field_110272_by");
        F_isRemote =          ASMHelper.getRemappedMF("isRemote",            "field_72995_K");
        F_ridingEntity =      ASMHelper.getRemappedMF("ridingEntity",        "field_70154_o");

        M_isBadEffect =          ASMHelper.getRemappedMF("isBadEffect",                 "func_76398_f");
        M_getVecFromPool =       ASMHelper.getRemappedMF("getVecFromPool",              "func_72345_a");
        M_onUpdate =             ASMHelper.getRemappedMF("onUpdate",                    "func_70071_h_");
        M_rayTraceBlocks =       ASMHelper.getRemappedMF("rayTraceBlocks",              "func_72933_a");
        M_updateObject =         ASMHelper.getRemappedMF("updateObject",                "func_75692_b");
        M_getStackInSlot =       ASMHelper.getRemappedMF("getStackInSlot",              "func_70301_a");
        M_addObject =            ASMHelper.getRemappedMF("addObject",                   "func_75682_a");
        M_getItem =              ASMHelper.getRemappedMF("getItem",                     "func_77973_b");
        M_getWatchableObjectIS = ASMHelper.getRemappedMF("getWatchableObjectItemStack", "func_82710_f");
        M_isItemEqual =          ASMHelper.getRemappedMF("isItemEqual",                 "func_77969_a");
        M_playSound =            ASMHelper.getRemappedMF("playSound",                   "func_85030_a");
        M_getUnlocalizedName =   ASMHelper.getRemappedMF("getUnlocalizedName",          "func_77977_a");
        M_interact =             ASMHelper.getRemappedMF("interact",                    "func_70085_c");
        M_func146085a =          ASMHelper.getRemappedMF("func_146085_a",               "func_146085_a");
        M_entityInit =           ASMHelper.getRemappedMF("entityInit",                  "func_70088_a");
        M_func110232cE =         ASMHelper.getRemappedMF("func_110232_cE",              "func_110232_cE");
        M_setHorseTexturePaths = ASMHelper.getRemappedMF("setHorseTexturePaths",        "func_110247_cG");
        M_isHorseSaddled =       ASMHelper.getRemappedMF("isHorseSaddled",              "func_110257_ck");
        M_onInventoryChanged =   ASMHelper.getRemappedMF("onInventoryChanged",          "func_76316_a");
        M_func110241cb =         ASMHelper.getRemappedMF("func_110241_cb",              "func_110241_cb");
        M_getTotalArmorValue =   ASMHelper.getRemappedMF("getTotalArmorValue",          "func_70658_aO");
        M_isSneaking =           ASMHelper.getRemappedMF("isSneaking",                  "func_70093_af");
        M_updateRidden =         ASMHelper.getRemappedMF("updateRidden",                "func_70098_U");


        isInitialized = true;
    }
}
