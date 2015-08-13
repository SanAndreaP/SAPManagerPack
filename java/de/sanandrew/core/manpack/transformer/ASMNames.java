/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ASMNames
{
    /*
     * OBFUSCATED NAMES
     */
    public static final String MDO_ENDERMAN_SHOULD_ATK_PLAYER = "net/minecraft/entity/monster/EntityEnderman/shouldAttackPlayer (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MDO_GET_COLLIDING_BB = "net/minecraft/world/World/getCollidingBoundingBoxes (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MDO_AABB_EXPAND = "net/minecraft/util/AxisAlignedBB/expand (DDD)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MDO_GET_ENTITIES_AABB_EXCLUDE = "net/minecraft/world/World/getEntitiesWithinAABBExcludingEntity (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Ljava/util/List;";
    public static final String MDO_GET_ENTITY_BOUNDING_BOX = "net/minecraft/entity/Entity/getBoundingBox ()Lnet/minecraft/util/AxisAlignedBB;";

    public static final String FDO_ENTPLAYER_INVENTORY = "net/minecraft/entity/player/EntityPlayer/inventory Lnet/minecraft/entity/player/InventoryPlayer;";
    public static final String FDO_INVPLAYER_ARMORINVENTORY = "net/minecraft/entity/player/InventoryPlayer/armorInventory [Lnet/minecraft/item/ItemStack;";

    /*
     * NON-OBFUSCATED NAMES
     */
    public static final String CL_ENDER_FACING_EVENT = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent";
    public static final String CL_COLLIDING_ENTITY_CHECK_EVENT = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent";

    public static final String CL_T_ENTITY = "Lnet/minecraft/entity/Entity;";
    public static final String CL_T_AXIS_ALIGNED_BB = "Lnet/minecraft/util/AxisAlignedBB;";

    public static final String MD_ENDER_FACING_EVENT_CTOR = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent/<init> (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V";
    public static final String MD_EVENT_BUS_POST = "cpw/mods/fml/common/eventhandler/EventBus/post (Lcpw/mods/fml/common/eventhandler/Event;)Z";
    public static final String MD_SAP_GET_BOUNDIN_GBOX = "_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_SAP_COLLENTITY_CHKEVT_CTOR = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent/<init> (Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V";
    public static final String MD_LIST_GET = "java/util/List/get (I)Ljava/lang/Object;";
    public static final String MD_SAP_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";

    public static final String FD_FORGE_EVENT_BUS = "net/minecraftforge/common/MinecraftForge/EVENT_BUS Lcpw/mods/fml/common/eventhandler/EventBus;";
    public static final String FD_SAPUTILS_EVENT_BUS = "de/sanandrew/core/manpack/util/helpers/SAPUtils/EVENT_BUS Lcpw/mods/fml/common/eventhandler/EventBus;";


    private static final Pattern OWNER_NAME_DESC_PATTERN = Pattern.compile("(.*)/(.*?) (.*)");

    public static MethodNode getNewMethod(int access, String method) {
        String[] methodSplit = method.split(" ");

        String name = methodSplit[0];
        String desc = methodSplit[1];
        String signature = methodSplit.length > 2 ? methodSplit[2] : null;
        String[] throwing = methodSplit.length > 3 ? methodSplit[3].split(";") : null;

        return new MethodNode(access, name, desc, signature, throwing);
    }

    public static MethodNode findObfMethod(ClassNode cn, String mcpMapping) {
        return ASMObfuscationHelper.getInstance().findMethod(cn, mcpMapping);
    }

    public static MethodInsnNode getObfMethodInsnNode(int opcode, String method, boolean intf) {
        return ASMObfuscationHelper.getInstance().getMethodInsnNode(opcode, method, intf);
    }

    public static FieldInsnNode getObfFieldInsnNode(int opcode, String field) {
        return ASMObfuscationHelper.getInstance().getFieldInsnNode(opcode, field);
    }

    public static MethodInsnNode getMethodInsnNode(int opcode, String method, boolean intf) {
        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(method);
        return new MethodInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3), intf);
    }

    public static FieldInsnNode getFieldInsnNode(int opcode, String field) {
        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(field);
        return new FieldInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3));
    }
//        F_isBadEffect =       ASMHelper.getRemappedMF("isBadEffect",         "field_76418_K");
//        F_attackingPlayer =   ASMHelper.getRemappedMF("attackingPlayer",     "field_70717_bb");
//        F_recentlyHit =       ASMHelper.getRemappedMF("recentlyHit",         "field_70718_bc");
//        F_motionZ =           ASMHelper.getRemappedMF("motionZ",             "field_70179_y");
//        F_worldObj =          ASMHelper.getRemappedMF("worldObj",            "field_70170_p");
//        F_dataWatcher =       ASMHelper.getRemappedMF("dataWatcher",         "field_70180_af");
//        F_horseChest =        ASMHelper.getRemappedMF("horseChest",          "field_110296_bG");
//        F_field110280bR =     ASMHelper.getRemappedMF("field_110280_bR",     "field_110280_bR");
//        F_field110286bQ =     ASMHelper.getRemappedMF("field_110286_bQ",     "field_110286_bQ");
//        F_ironShovel =        ASMHelper.getRemappedMF("iron_shovel",         "field_151037_a");
//        F_iron_horse_armor =  ASMHelper.getRemappedMF("iron_horse_armor",    "field_151138_bX");
//        F_diamondHorseArmor = ASMHelper.getRemappedMF("diamond_horse_armor", "field_151125_bZ");
//        F_armorValues =       ASMHelper.getRemappedMF("armorValues",         "field_110272_by");
//        F_isRemote =          ASMHelper.getRemappedMF("isRemote",            "field_72995_K");
//        F_ridingEntity =      ASMHelper.getRemappedMF("ridingEntity",        "field_70154_o");
//        F_inventory =         ASMHelper.getRemappedMF("inventory",           "field_71071_by");
//        F_armorInventory =    ASMHelper.getRemappedMF("armorInventory",      "field_70460_b");
//        F_posX =              ASMHelper.getRemappedMF("posX",                "field_70165_t");
//        F_posY =              ASMHelper.getRemappedMF("posY",                "field_70163_u");
//        F_posZ =              ASMHelper.getRemappedMF("posZ",                "field_70161_v");
//        F_rand =              ASMHelper.getRemappedMF("rand",                "field_70146_Z");
//        F_width =             ASMHelper.getRemappedMF("width",               "field_70130_N");
//        F_height =            ASMHelper.getRemappedMF("height",              "field_70131_O");
//        F_selectedButton =    ASMHelper.getRemappedMF("selectedButton",      "field_73883_a");
//
//        M_isBadEffect =           ASMHelper.getRemappedMF("isBadEffect",                 "func_76398_f");
//        M_createVectorHelper =    ASMHelper.getRemappedMF("createVectorHelper",          "func_72443_a");
//        M_onUpdate =              ASMHelper.getRemappedMF("onUpdate",                    "func_70071_h_");
//        M_rayTraceBlocks =        ASMHelper.getRemappedMF("rayTraceBlocks",              "func_72933_a");
//        M_rayTraceBlocksB =       ASMHelper.getRemappedMF("rayTraceBlocks",              "func_72901_a");
//        M_updateObject =          ASMHelper.getRemappedMF("updateObject",                "func_75692_b");
//        M_getStackInSlot =        ASMHelper.getRemappedMF("getStackInSlot",              "func_70301_a");
//        M_addObject =             ASMHelper.getRemappedMF("addObject",                   "func_75682_a");
//        M_getItem =               ASMHelper.getRemappedMF("getItem",                     "func_77973_b");
//        M_getWatchableObjectIS =  ASMHelper.getRemappedMF("getWatchableObjectItemStack", "func_82710_f");
//        M_isItemEqual =           ASMHelper.getRemappedMF("isItemEqual",                 "func_77969_a");
//        M_playSound =             ASMHelper.getRemappedMF("playSound",                   "func_85030_a");
//        M_getUnlocalizedName =    ASMHelper.getRemappedMF("getUnlocalizedName",          "func_77977_a");
//        M_interact =              ASMHelper.getRemappedMF("interact",                    "func_70085_c");
//        M_func146085a =           ASMHelper.getRemappedMF("func_146085_a",               "func_146085_a");
//        M_entityInit =            ASMHelper.getRemappedMF("entityInit",                  "func_70088_a");
//        M_func110232cE =          ASMHelper.getRemappedMF("func_110232_cE",              "func_110232_cE");
//        M_setHorseTexturePaths =  ASMHelper.getRemappedMF("setHorseTexturePaths",        "func_110247_cG");
//        M_isHorseSaddled =        ASMHelper.getRemappedMF("isHorseSaddled",              "func_110257_ck");
//        M_onInventoryChanged =    ASMHelper.getRemappedMF("onInventoryChanged",          "func_76316_a");
//        M_func110241cb =          ASMHelper.getRemappedMF("func_110241_cb",              "func_110241_cb");
//        M_getTotalArmorValue =    ASMHelper.getRemappedMF("getTotalArmorValue",          "func_70658_aO");
//        M_isSneaking =            ASMHelper.getRemappedMF("isSneaking",                  "func_70093_af");
//        M_updateRidden =          ASMHelper.getRemappedMF("updateRidden",                "func_70098_U");
//        M_shouldAttackPlayer =    ASMHelper.getRemappedMF("shouldAttackPlayer",          "func_70821_d");
//        M_onLivingUpdate =        ASMHelper.getRemappedMF("onLivingUpdate",              "func_70636_d");
//        M_teleportTo =            ASMHelper.getRemappedMF("teleportTo",                  "func_70825_j");
//        M_spawnParticle =         ASMHelper.getRemappedMF("spawnParticle",               "func_72869_a");
//        M_getEntityRenderObj =    ASMHelper.getRemappedMF("getEntityRenderObject",       "func_78713_a");
//        M_getEntityClsRenderObj = ASMHelper.getRemappedMF("getEntityClassRenderObject",  "func_78715_a");
//        M_renderHand =            ASMHelper.getRemappedMF("renderHand",                  "func_78476_b");
//        M_getBoundingBox =        ASMHelper.getRemappedMF("getBoundingBox",              "func_70046_E");
//        M_getCollidingBBoxes =    ASMHelper.getRemappedMF("getCollidingBoundingBoxes",   "func_72945_a");
//        M_aabbExpand =            ASMHelper.getRemappedMF("expand",                      "func_72314_b");
//        M_getEntitiesExclude =    ASMHelper.getRemappedMF("getEntitiesWithinAABBExcludingEntity", "func_94576_a");
}
