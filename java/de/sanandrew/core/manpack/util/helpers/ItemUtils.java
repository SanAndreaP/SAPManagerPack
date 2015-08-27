/**
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.core.manpack.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A helper class for Item manipulations
 */
public final class ItemUtils
{
    private static final Comparator<NBTTagCompound> STD_NBT_COMPARATOR = new Comparator<NBTTagCompound>()
    {
        @Override
        public int compare(NBTTagCompound o1, NBTTagCompound o2) {
            if( o1 != null ) {
                if( !o1.equals(o2) ) {
                    return 1;
                }
            } else if( o2 != null ) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Decreases the size of the given stack by the amount.
     *
     * @param is     the ItemStack whose size should be decreased
     * @param amount the amount the size will be decreased
     * @return the stack with decreased size or null, if the size is <= 0
     */
    public static ItemStack decrStackSize(ItemStack is, int amount) {
        is.stackSize -= amount;

        if( is.stackSize <= 0 ) {
            return null;
        }

        return is;
    }

    /**
     * Compares the two given stacks and checks for their equality.<br>
     * <p>They are considered equal if either:
     * <ul>
     * <li>They are both null: Either the instances themselves or their items are both {@code null}</li>
     * <li>They have the same item instance and:
     * <ul>
     * <li>If {@code checkNbt == true}, the NBT of those 2 items is either null or {@code is1.nbt.equals(is2.nbt) == true}, otherwise NBT isn't checked</li>
     * <li>Their damage value is the same <i>or</i> either of their damage value has the value of {@link OreDictionary#WILDCARD_VALUE}</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     *
     * @param is1      the first stack
     * @param is2      the second stack
     * @param checkNbt true, if the NBT should be checked as well
     * @return true, if the stacks are equal, false otherwise.
     */
    public static boolean areStacksEqual(ItemStack is1, ItemStack is2, boolean checkNbt) {
        return areStacksEqual(is1, is2, checkNbt ? STD_NBT_COMPARATOR : null);
    }

    /**
     * Compares the two given stacks and checks for their equality.<br>
     * <p>They are considered equal if either:
     * <ul>
     * <li>They are both null: Either the instances themselves or their items are both {@code null}</li>
     * <li>They have the same item instance and:
     * <ul>
     * <li>If the given comparator is either null or its {@code .compare(nbt1, nbt2)} method returns 0</li>
     * <li>Their damage value is the same <i>or</i> either of their damage value has the value of {@link OreDictionary#WILDCARD_VALUE}</li>
     * </ul>
     * </li>
     * </ul>
     * </p>
     *
     * @param is1      the first stack
     * @param is2      the second stack
     * @param nbtCheck A comparator for the NBTs of those 2 stacks, can be null to avoid checking for NBT
     * @return true, if the stacks are equal, false otherwise.
     */
    public static boolean areStacksEqual(ItemStack is1, ItemStack is2, Comparator<NBTTagCompound> nbtCheck) {
        if( is1 == null || is2 == null ) {
            return is1 == is2;
        }

        if( is1.getItem() == null || is2.getItem() == null ) {
            return is1.getItem() == is2.getItem();
        }

        if( is1.getItem() == is2.getItem() ) {
            if( nbtCheck != null ) {
                if( nbtCheck.compare(is1.getTagCompound(), is2.getTagCompound()) != 0 ) {
                    return false;
                }
            }

            return is1.getItemDamage() == OreDictionary.WILDCARD_VALUE || is2.getItemDamage() == OreDictionary.WILDCARD_VALUE
                    || is1.getItemDamage() == is2.getItemDamage();
        }

        return false;
    }

    /**
     * Splits the given stack, which has a size > it's max. allowed, into "good" stacks with sizes <= max. allowed.
     *
     * @param is the stack which should be split
     * @return an Array of all "good" stacks. If {@code is.stackSize <= 0}, then a zero-length array is returned.
     */
    public static ItemStack[] getGoodItemStacks(ItemStack is) {
        int maxStackSize = is.getMaxStackSize();
        if( is.stackSize <= maxStackSize && is.stackSize > 0 ) {
            return new ItemStack[] { is };
        } else if( is.stackSize > 0 ) {
            int maxFullStackCnt = MathHelper.floor_float(is.stackSize / (float) maxStackSize);
            List<ItemStack> isMap = new ArrayList<>(MathHelper.ceiling_float_int(is.stackSize / (float) maxStackSize));
            ItemStack isNew;

            for( int i = 0; i < maxFullStackCnt; i++ ) {
                isNew = is.copy();
                isNew.stackSize = maxStackSize;
                isMap.add(isNew);
            }

            isNew = is.copy();

            if( (isNew.stackSize -= maxStackSize * maxFullStackCnt) > 0 ) {
                isMap.add(isNew);
            }

            return isMap.toArray(new ItemStack[isMap.size()]);
        }

        return new ItemStack[0];
    }

    /**
     * Checks if the ItemStack given can be found inside the array.<br>
     * Comparison rules are like {@link ItemUtils#areStacksEqual(ItemStack, ItemStack, boolean)}.
     *
     * @param stack      the stack to be searched inside the array
     * @param checkNbt   true, if the NBT should be checked as well
     * @param stackArray the array to be searched
     * @return true, if the stack was found, false otherwise
     */
    public static boolean isItemStackInArray(ItemStack stack, boolean checkNbt, ItemStack... stackArray) {
        for( ItemStack stackElem : stackArray ) {
            if( areStacksEqual(stack, stackElem, checkNbt) ) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the ItemStack given can be found inside the array.<br>
     * Comparison rules are like {@link ItemUtils#areStacksEqual(ItemStack, ItemStack, Comparator)}.
     *
     * @param stack      the stack to be searched inside the array
     * @param nbtCheck   A comparator for the NBTs of those 2 stacks, can be null to avoid checking for NBT
     * @param stackArray the array to be searched
     * @return true, if the stack was found, false otherwise
     */
    public static boolean isItemStackInArray(ItemStack stack, Comparator<NBTTagCompound> nbtCheck, ItemStack... stackArray) {
        for( ItemStack stackElem : stackArray ) {
            if( areStacksEqual(stack, stackElem, nbtCheck) ) {
                return true;
            }
        }

        return false;
    }
}
