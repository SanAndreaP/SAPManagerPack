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
    public static final String MDO_VEC3_CREATE_VECTOR_HELPER = "net/minecraft/util/Vec3/createVectorHelper (DDD)Lnet/minecraft/util/Vec3;";
    public static final String MDO_WORLD_RAY_TRACE_BLOCKS = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MDO_WORLD_RAY_TRACE_BLOCKS_Z = "net/minecraft/world/World/rayTraceBlocks (Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;Z)Lnet/minecraft/util/MovingObjectPosition;";
    public static final String MDO_THROWABLE_ON_UPDATE = "net/minecraft/entity/projectile/EntityThrowable/onUpdate ()V";
    public static final String MDO_PLAYER_UPDATE_RIDDEN = "net/minecraft/entity/player/EntityPlayer/updateRidden ()V";
    public static final String MDO_ENTITY_IS_SNEAKING = "net/minecraft/entity/Entity/isSneaking ()Z";
    public static final String MDO_DATAWATCHER_GET_OBJ_STACK = "net/minecraft/entity/DataWatcher/getWatchableObjectItemStack (I)Lnet/minecraft/item/ItemStack;";
    public static final String MDO_HORSE_INTERACT = "net/minecraft/entity/passive/EntityHorse/interact (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MDO_HORSE_FUNC146085A = "net/minecraft/entity/passive/EntityHorse/func_146085_a (Lnet/minecraft/item/Item;)Z";
    public static final String MDO_HORSE_ENTITY_INIT = "net/minecraft/entity/passive/EntityHorse/entityInit ()V";
    public static final String MDO_HORSE_FUNC110232CE = "net/minecraft/entity/passive/EntityHorse/func_110232_cE ()V";
    public static final String MDO_HORSE_ON_INV_CHANGED = "net/minecraft/entity/passive/EntityHorse/onInventoryChanged (Lnet/minecraft/inventory/InventoryBasic;)V";
    public static final String MDO_HORSE_GET_TOTAL_ARMOR_VAL = "net/minecraft/entity/passive/EntityHorse/getTotalArmorValue ()I";
    public static final String MDO_HORSE_SET_TEXTURE_PATH = "net/minecraft/entity/passive/EntityHorse/setHorseTexturePaths ()V";

    public static final String FDO_ENTPLAYER_INVENTORY = "net/minecraft/entity/player/EntityPlayer/inventory Lnet/minecraft/entity/player/InventoryPlayer;";
    public static final String FDO_INVPLAYER_ARMORINVENTORY = "net/minecraft/entity/player/InventoryPlayer/armorInventory [Lnet/minecraft/item/ItemStack;";
    public static final String FDO_ENTITY_MOTIONZ = "net/minecraft/entity/Entity/motionZ D";
    public static final String FDO_ENTITY_WORLDOBJ = "net/minecraft/entity/Entity/worldObj Lnet/minecraft/world/World;";
    public static final String FDO_WORLD_ISREMOTE = "net/minecraft/world/World/isRemote Z";
    public static final String FDO_ENTITY_RIDING_ENTITY = "net/minecraft/entity/Entity/ridingEntity Lnet/minecraft/entity/Entity;";
    public static final String FDO_ENTITY_DATAWATCHER = "net/minecraft/entity/Entity/dataWatcher Lnet/minecraft/entity/DataWatcher;";
    public static final String FDO_ITEMS_IRON_SHOVEL = "net/minecraft/init/Items/iron_shovel Lnet/minecraft/item/Item;";

    /*
     * NON-OBFUSCATED NAMES
     */
    public static final String CL_ENDER_FACING_EVENT = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent";
    public static final String CL_COLLIDING_ENTITY_CHECK_EVENT = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent";
    public static final String CL_ENTITY_THROWABLE = "net/minecraft/entity/projectile/EntityThrowable";
    public static final String CL_ENTITY_PLAYER = "net/minecraft/entity/player/EntityPlayer";
    public static final String CL_ENTITY_HORSE = "net/minecraft/entity/passive/EntityHorse";
    public static final String CL_ITEM_STACK = "net/minecraft/item/ItemStack";

    public static final String CL_T_ENTITY = "Lnet/minecraft/entity/Entity;";
    public static final String CL_T_AXIS_ALIGNED_BB = "Lnet/minecraft/util/AxisAlignedBB;";
    public static final String CL_T_ENTITY_THROWABLE = "Lnet/minecraft/entity/projectile/EntityThrowable;";
    public static final String CL_T_ENTITY_PLAYER = "Lnet/minecraft/entity/player/EntityPlayer;";
    public static final String CL_T_ENTITY_HORSE = "Lnet/minecraft/entity/passive/EntityHorse;";

    public static final String MD_ENDER_FACING_EVENT_CTOR = "de/sanandrew/core/manpack/util/event/entity/EnderFacingEvent/<init> (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/monster/EntityEnderman;)V";
    public static final String MD_EVENT_BUS_POST = "cpw/mods/fml/common/eventhandler/EventBus/post (Lcpw/mods/fml/common/eventhandler/Event;)Z";
    public static final String MD_LIST_GET = "java/util/List/get (I)Ljava/lang/Object;";
    public static final String MD_SAP_GET_BOUNDING_BOX_NEW = "_SAP_getBoundingBox (Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)Lnet/minecraft/util/AxisAlignedBB;";
    public static final String MD_SAP_GET_BOUNDING_BOX = "net/minecraft/entity/Entity/" + MD_SAP_GET_BOUNDING_BOX_NEW;
    public static final String MD_SAP_COLLENTITY_CHKEVT_CTOR = "de/sanandrew/core/manpack/util/event/entity/CollidingEntityCheckEvent/<init> (Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/AxisAlignedBB;)V";
    public static final String MD_SAP_CAN_IMPACT_ON_LIQUID_NEW = "_SAP_canImpactOnLiquid ()Z";
    public static final String MD_SAP_CAN_IMPACT_ON_LIQUID = "net/minecraft/entity/projectile/EntityThrowable/" + MD_SAP_CAN_IMPACT_ON_LIQUID_NEW;
    public static final String MD_SAP_CAN_DISMOUNT_ON_INPUT_NEW = "_SAP_canDismountOnInput (Lnet/minecraft/entity/player/EntityPlayer;)Z";
    public static final String MD_SAP_CAN_DISMOUNT_ON_INPUT = "net/minecraft/entity/Entity/" + MD_SAP_CAN_DISMOUNT_ON_INPUT_NEW;
    public static final String MD_SAP_GET_CUSTOM_ARMOR_ITEM_NEW = "_SAP_getCustomArmorItem ()Lnet/minecraft/item/ItemStack;";
    public static final String MD_SAP_SET_CUSTOM_ARMOR_ITEM_NEW = "_SAP_setCustomArmorItem (Lnet/minecraft/item/ItemStack;)V";
    public static final String MD_ITEMSTACK_INIT = "net/minecraft/item/ItemStack/<init> (Lnet/minecraft/item/Item;I)V";

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

    public static MethodInsnNode getObfMethodInsnNode(int opcode, String method, String owner, boolean intf) {
        MethodInsnNode node = ASMObfuscationHelper.getInstance().getMethodInsnNode(opcode, method, intf);
        node.owner = owner;
        return node;
    }

    public static MethodInsnNode getMethodInsnNode(int opcode, String method, boolean intf) {
        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(method);
        return new MethodInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3), intf);
    }

    public static void visitObfMethodInsn(MethodNode node, int opcode, String method, boolean intf) {
        MethodInsnNode methodNode = getObfMethodInsnNode(opcode, method, intf);
        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
    }

    public static void visitObfMethodInsn(MethodNode node, int opcode, String method, String owner, boolean intf) {
        MethodInsnNode methodNode = getObfMethodInsnNode(opcode, method, owner, intf);
        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
    }

    public static void visitMethodInsn(MethodNode node, int opcode, String method, boolean intf) {
        MethodInsnNode methodNode = getMethodInsnNode(opcode, method, intf);
        node.visitMethodInsn(opcode, methodNode.owner, methodNode.name, methodNode.desc, methodNode.itf);
    }

    public static FieldInsnNode getObfFieldInsnNode(int opcode, String field) {
        return ASMObfuscationHelper.getInstance().getFieldInsnNode(opcode, field);
    }

    public static FieldInsnNode getObfFieldInsnNode(int opcode, String field, String owner) {
        FieldInsnNode node = ASMObfuscationHelper.getInstance().getFieldInsnNode(opcode, field);
        node.owner = owner;
        return node;
    }

    public static FieldInsnNode getFieldInsnNode(int opcode, String field) {
        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(field);
        return new FieldInsnNode(opcode, matcher.group(1), matcher.group(2), matcher.group(3));
    }

    public static FieldInsnNode getFieldInsnNode(int opcode, String field, String owner) {
        Matcher matcher = OWNER_NAME_DESC_PATTERN.matcher(field);
        return new FieldInsnNode(opcode, owner, matcher.group(2), matcher.group(3));
    }

    public static void visitObfFieldInsn(MethodNode node, int opcode, String field) {
        FieldInsnNode fieldNode = getObfFieldInsnNode(opcode, field);
        node.visitFieldInsn(opcode, fieldNode.owner, fieldNode.name, fieldNode.desc);
    }

    public static void visitObfFieldInsn(MethodNode node, int opcode, String field, String owner) {
        FieldInsnNode fieldNode = getObfFieldInsnNode(opcode, field, owner);
        node.visitFieldInsn(opcode, fieldNode.owner, fieldNode.name, fieldNode.desc);
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
