package de.sanandrew.core.manpack.util;

import de.sanandrew.core.manpack.util.javatuples.Quartet;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter.Category;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * A dummy class for compatibility!
 *
 * @author SanAndreasP
 */
@Deprecated
public final class SAPUtils
{
    public static final Random RNG = de.sanandrew.core.manpack.util.helpers.SAPUtils.RNG;

    public static ItemStack decrStackSize(ItemStack stack) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.decrStackSize(stack);
    }

    public static ItemStack decrStackSize(ItemStack stack, int amount) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.decrStackSize(stack, amount);
    }

    public static ItemStack decrInvStackSize(ItemStack stack, int amount) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.decrInvStackSize(stack, amount);
    }

    public static boolean areStacksEqualWithWCV(ItemStack stack1, ItemStack stack2) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.areStacksEqual(stack1, stack2, false);
    }

    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, boolean checkNbt) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.areStacksEqual(stack1, stack2, checkNbt);
    }

    public static ItemStack[] getGoodItemStacks(ItemStack stack) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getGoodItemStacks(stack);
    }

    public static int getInBetweenVal(int val1, int val2) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getInBetweenVal(val1, val2);
    }

    public static ItemStack getSilkBlock(Block block, int meta) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getSilkBlock(block, meta);
    }

    public static void dropBlockAsItem(Block block, World world, int x, int y, int z, ItemStack stack) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.dropBlockAsItem(block, world, x, y, z, stack);
    }

    public static void dropBlockAsItem(World world, int x, int y, int z, ItemStack stack) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.dropBlockAsItem(world, x, y, z, stack);
    }

    public static void dropBlockXP(Block block, World world, int x, int y, int z, int meta, int fortune) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.dropBlockXP(block, world, x, y, z, meta, fortune);
    }

    public static boolean isItemInStackArray(ItemStack base, ItemStack... stackArray) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isItemInStackArray(base, false, stackArray);
    }

    public static boolean isItemInStackArray(ItemStack base, List<ItemStack> stackArray) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isItemInStackArray(base, false, stackArray.toArray(new ItemStack[stackArray.size()]));
    }

    public static boolean isItemInStackArray(ItemStack base, boolean checkSize, ItemStack... stackArray) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isItemInStackArray(base, checkSize, stackArray);
    }

    public static boolean isItemInStackArray(ItemStack base, boolean checkSize, List<ItemStack> stackArray) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isItemInStackArray(base, checkSize, stackArray.toArray(new ItemStack[stackArray.size()]));
    }

    public static Block[] getToolBlocks(ItemTool tool) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getToolBlocks(tool);
    }

    public static boolean isToolEffective(Block[] effectives, Block block) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isToolEffective(effectives, block);
    }

    public static boolean areItemInstEqual(Object instance1, Object instance2) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.areItemInstEqual(instance1, instance2);
    }

    public static <T> T[] getArrayFromCollection(Collection<T> collection, Class clazz) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getArrayFromCollection(collection, clazz);
    }

    public static void registerBlocks(Block... blocks) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.registerBlocks(blocks);
    }

    public static void registerBlockWithItem(Block block, Class<? extends ItemBlock> itemClass) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.registerBlockWithItem(block, itemClass);
    }

    public static void registerItems(Item... items) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.registerItems(items);
    }

    public static ItemStack addItemStackToInventory(ItemStack is, IInventory inv) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.addItemStackToInventory(is, inv);
    }

    public static RGBAValues getRgbaFromColorInt(int rgba) {
        return new RGBAValues(((rgba) >> 16) & 255, ((rgba) >> 8) & 255, rgba & 255, ((rgba) >> 24) & 255);
    }

    public static File getMcDir(String path) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getMcDir(path);
    }

    public static void restartApp() {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.restartApp();
    }

    public static void shutdownApp() {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.shutdownApp();
    }

    public static boolean isIndexInRange(Object[] array, int index) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.isIndexInRange(array, index);
    }

    public static DamageSource getNewDamageSource(String type) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getNewDamageSource(type);
    }

    public static int getMaxDmgFactorAM(ItemArmor.ArmorMaterial aMaterial) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.getMaxDmgFactorAM(aMaterial);
    }

    public static void registerSortedRecipe(IRecipe recipe, String name, Category category, String dependencies) {
        de.sanandrew.core.manpack.util.helpers.SAPUtils.registerSortedRecipe(recipe, name, category, dependencies);
    }

    public static String translate(String key) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.translate(key);
    }

    public static String translatePostFormat(String key, Object... data) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.translatePostFormat(key, data);
    }

    public static String translatePreFormat(String key, Object... data) {
        return de.sanandrew.core.manpack.util.helpers.SAPUtils.translatePreFormat(key, data);
    }

    public static class RGBAValues {
        private final Quartet<Integer, Integer, Integer, Integer> value;

        public RGBAValues(int r, int g, int b, int a) {
            this.value = Quartet.with(r, g, b, a);
        }

        public int getRed() {
            return this.value.getValue0();
        }

        public int getGreen() {
            return this.value.getValue1();
        }

        public int getBlue() {
            return this.value.getValue2();
        }

        public int getAlpha() {
            return this.value.getValue3();
        }

        public float[] getColorFloatArray() {
            return new float[] {this.getRed() / 255.0F, this.getGreen() / 255.0F, this.getBlue() / 255.0F, this.getAlpha() / 255.0F};
        }
    }
}
