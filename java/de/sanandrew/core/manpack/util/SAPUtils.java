package de.sanandrew.core.manpack.util;

import cpw.mods.fml.common.registry.GameRegistry;
import de.sanandrew.core.manpack.util.javatuples.Quartet;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A helper class for common used stuff, which is not found somewhere else and
 * not as easy and short to write. It's goal is to shorten and cleanse the
 * sourcecode with unnecessary stuff.
 *
 * @author SanAndreasP
 */
public final class SAPUtils
{
    /** My personal RNG Deity, to be used whenever a global RNG is needed */
    public static final Random RNG = new Random();

    /**
     * decreases the ItemStack stackSize by 1
     *
     * @param stack ItemStack, whose stackSize will be decreased
     * @return The stackSize-decreased ItemStack or null if stackSize <= 0
     **/
    public static ItemStack decrStackSize(ItemStack stack) {
        return decrStackSize(stack, 1);
    }

    /**
     * decreases the ItemStack stackSize by the amount value
     *
     * @param stack ItemStack, whose stackSize will be decreased
     * @param amount The amount which will be subtracted from the stackSize
     * @return The stackSize-decreased ItemStack or null if stackSize <= 0
     **/
    public static ItemStack decrStackSize(ItemStack stack, int amount) {
        return ItemHelper.decrStackSize(stack, amount);
    }

    /**
     * Compares two ItemStacks if they are equal. If one of the ItemStacks has
     * the metadata wildcard as damage value, only the item instances will be
     * compared.
     *
     * @param stack1 The first ItemStack
     * @param stack2 the second ItemStack
     * @return true, if stacks are equal, false otherwise.
     */
    public static boolean areStacksEqualWithWCV(ItemStack stack1, ItemStack stack2) {
        return ItemHelper.areStacksEqualWithWCV(stack1, stack2);
    }

    /**
     * Splits the ItemStack into multiple ("good") ones if stackSize >
     * getMaxStackSize().
     *
     * @param stack The ItemStack which shall be splitted into "good" ItemStacks
     * @return An Array of "good" ItemStacks. If stackSize was smaller than the
     *         maxStackSize, the original ItemStack is in Field 0 inside the
     *         Array.
     */
    public static ItemStack[] getGoodItemStacks(ItemStack stack) {
        return ItemHelper.getGoodItemStacks(stack);
    }

    /**
     * Gets a value in the middle of 2 values, for example val1 is 1 and val2 is 5, the value returned would be 3.
     * Note: val1 doesn't have to be smaller than val2
     * @param val1 the first value
     * @param val2 the second value
     * @return the value in between val1 and val2
     */
    public static int getInBetweenVal(int val1, int val2) {
        int maxVal = Math.max(val1, val2);
        int minVal = Math.min(val1, val2);
        return Math.round((maxVal + minVal) / 2F);
    }

    /**
     * public reflection getter for {@link net.minecraft.block.Block#createStackedBlock(int)}. Look at it's javadoc for more info.
     * @param block The block which will invoke the method
     * @param meta the metadata of the invoking block
     * @return the return value from the invoked method
     */
    @SuppressWarnings("unused")
    public static ItemStack getSilkBlock(Block block, int meta) {
        Class<?>[] methodPT = new Class[] { int.class };
        Object[] methodPV = new Object[] { meta };
        return SAPReflectionHelper.invokeCachedMethod(Block.class, block, "createStackedBlock", "func_71880_c_", methodPT, methodPV);
    }

    public static void dropBlockAsItem(Block block, World world, int x, int y, int z, ItemStack stack) {
        EntityItem item = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, stack);
        world.spawnEntityInWorld(item);
    }

    public static void dropBlockXP(Block block, World world, int x, int y, int z, int meta, int fortune) {
        block.dropXpOnBlockBreak(world, x, y, z, block.getExpDrop(world, meta, fortune));
    }

    public static boolean isItemInStackArray(ItemStack base, ItemStack... stackArray) {
        return ItemHelper.isItemInStackArray(base, stackArray);
    }

    public static boolean isItemInStackArray(ItemStack base, List<ItemStack> stackArray) {
        return ItemHelper.isItemInStackArray(base, stackArray.toArray(new ItemStack[stackArray.size()]));
    }

    @SuppressWarnings("unchecked")
    public static Block[] getToolBlocks(ItemTool tool) {
        Set set = SAPReflectionHelper.getCachedFieldValue(ItemTool.class, tool, "field_150914_c", "field_150914_c");
        Set<Block> blockSet = (Set<Block>) set;
        return getArrayFromCollection(blockSet, Block.class);
    }

    public static boolean isToolEffective(Block[] effectives, Block block) {
        for( Block currBlock : effectives ) {
            if( block == currBlock ) {
                return true;
            }
        }
        return false;
    }

    public static boolean areItemInstEqual(Object instance1, Object instance2) {
        return ItemHelper.areItemInstEqual(instance1, instance2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T[] getArrayFromCollection(Collection<T> collection, Class clazz) {
        if( collection.size() == 0 ) {
            return null;
        }
        T[] myArray = (T[]) Array.newInstance(clazz, collection.size());
        collection.toArray(myArray);
        return myArray;
    }

    public static void registerBlocks(Block... blocks) {
        for( Block block : blocks ) {
            String blockName = block.getUnlocalizedName();
            blockName = blockName.substring(blockName.lastIndexOf(':')+1);
            GameRegistry.registerBlock(block, blockName.toLowerCase());
        }
    }

    public static <A> void registerBlockWithItem(Block block, Class<? extends ItemBlock> itemClass) {
        String blockName = block.getUnlocalizedName();
        blockName = blockName.substring(blockName.lastIndexOf(':') + 1);
        GameRegistry.registerBlock(block, itemClass, blockName.toLowerCase());
    }

    public static void registerItems(Item... items) {
        for( Item item : items ) {
            String itemName = item.getUnlocalizedName();
            itemName = itemName.substring(itemName.lastIndexOf(':')+1);
            GameRegistry.registerItem(item, itemName.toLowerCase());
        }
    }

    public static ItemStack addItemStackToInventory(ItemStack is, IInventory inv) {
        return ItemHelper.addItemStackToInventory(is, inv);
    }

    public static RGBAValues getRgbaFromColorInt(int rgba) {
        return new RGBAValues(((rgba) >> 16) & 255, ((rgba) >> 8) & 255, rgba & 255, ((rgba) >> 24) & 255);
    }

    public static File getMcDir(String path) {
        return AppHelper.getMcDir(path);
    }

    public static void restartApp() throws IOException {
        AppHelper.restartApp();
    }

    public static void shutdownApp() {
        AppHelper.shutdownApp();
    }

    public static boolean isIndexInRange(Object[] array, int index) {
        return index >= 0 && index < array.length;
    }

    @Deprecated
    public static DamageSource getNewDamageSource(String type) {
        try {
            Constructor<DamageSource> dmgsrcConst = DamageSource.class.getDeclaredConstructor(String.class);
            dmgsrcConst.setAccessible(true);
            return dmgsrcConst.newInstance(type);
        } catch( Throwable e ) {
            e.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static int getMaxDmgFactorAM(ItemArmor.ArmorMaterial aMaterial) {
        return SAPReflectionHelper.getCachedFieldValue(ItemArmor.ArmorMaterial.class, aMaterial, "maxDamageFactor", "field_78048_f");
    }

    public static class RGBAValues {
        private final Quartet<Integer, Integer, Integer, Integer> value;

        private RGBAValues(int r, int g, int b, int a) {
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
