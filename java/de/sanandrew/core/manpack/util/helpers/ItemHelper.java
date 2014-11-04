/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.helpers;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

final class ItemHelper
{
    static ItemStack decrStackSize(ItemStack is, int amount) {
        is.stackSize -= amount;
        if( is.stackSize <= 0 ) {
            return null;
        }
        return is;
    }

    static boolean areStacksEqualWithWCV(ItemStack is1, ItemStack is2) {
        if( is1 == null || is2 == null ) {
            return is1 == is2;
        } else if( is1.getItem() == null || is2.getItem() == null ) {
            return is1.getItem() == is2.getItem();
        } else if( is1.isItemEqual(is2) ) {
            return true;
        } else if( is1.getItemDamage() == OreDictionary.WILDCARD_VALUE || is2.getItemDamage() == OreDictionary.WILDCARD_VALUE ) {
            return is1.getItem() == is2.getItem();
        }

        return false;
    }

    static ItemStack[] getGoodItemStacks(ItemStack is) {
        List<ItemStack> isMap = new ArrayList<>();
        if( is.stackSize <= is.getMaxStackSize() && is.stackSize > 0 ) {
            isMap.add(is);
        } else if( is.stackSize > 0 ) {
            int stk = is.stackSize;
            for( int i = 0; i < MathHelper.ceiling_float_int((float) is.stackSize / (float) is.getMaxStackSize()); i++ ) {
                ItemStack is1 = is.copy();
                if( stk > is.getMaxStackSize() ) {
                    stk -= is1.stackSize = is.getMaxStackSize();
                } else {
                    is1.stackSize = stk;
                }
                isMap.add(is1);
            }
        }
        return isMap.toArray(new ItemStack[isMap.size()]);
    }

    static boolean isItemInStackArray(ItemStack base, ItemStack... stackArray) {
        for( ItemStack stack : stackArray ) {
            if( base != null && stack != null && areStacksEqualWithWCV(base, stack) ) {
                return true;
            }
        }
        return false;
    }

    static boolean areItemInstEqual(Object instance1, Object instance2) {
        if( instance1 instanceof Item ) {
            if( instance2 instanceof Item ) {
                return instance1 == instance2;
            } else if( instance2 instanceof ItemStack ) {
                return instance1 == ((ItemStack) instance2).getItem();
            }
        } else if( instance1 instanceof Block ) {
            if( instance2 instanceof Block ) {
                return instance1 == instance2;
            } else if( instance2 instanceof ItemStack && ((ItemStack) instance2).getItem() instanceof ItemBlock ) {
                return instance1 == Block.getBlockFromItem(((ItemStack)instance2).getItem());
            }
        } else if( instance1 instanceof ItemStack ) {
            if( instance2 instanceof Block || instance2 instanceof Item ) {
                return SAPUtils.areItemInstEqual(instance2, instance1);
            } else if( instance2 instanceof ItemStack ) {
                return SAPUtils.areStacksEqualWithWCV((ItemStack) instance1, (ItemStack) instance2);
            }
        }
        return false;
    }

    static ItemStack addItemStackToInventory(ItemStack is, IInventory inv) {
        int invSize = inv.getSizeInventory() - (inv instanceof InventoryPlayer ? 4 : 0);
        for( int i1 = 0; i1 < invSize && is != null; i1++ ) {
            ItemStack invIS = inv.getStackInSlot(i1);
            if( invIS != null && is.isItemEqual(invIS) ) {
                int combinedCount = is.stackSize + invIS.stackSize;
                int maxStack = Math.min(invIS.getMaxStackSize(), inv.getInventoryStackLimit());
                if( combinedCount <= maxStack ) {
                    invIS.stackSize = combinedCount;
                    inv.setInventorySlotContents(i1, invIS.copy());
                    is = null;
                    break;
                } else {
                    int rest = combinedCount - maxStack;
                    invIS.stackSize = maxStack;
                    inv.setInventorySlotContents(i1, invIS.copy());
                    is.stackSize = rest;
                }
            }
        }

        // if the given stack is not empty yet, search for an empty slot and put it there
        for( int i2 = 0; i2 < invSize && is != null; i2++ ) {
            ItemStack invIS = inv.getStackInSlot(i2);
            if( invIS == null && inv.isItemValidForSlot(i2, is) ) {
                if( is.stackSize <= inv.getInventoryStackLimit() ) {
                    inv.setInventorySlotContents(i2, is.copy());
                    is = null;
                    break;
                } else {
                    int rest = is.stackSize - inv.getInventoryStackLimit();
                    is.stackSize = inv.getInventoryStackLimit();
                    inv.setInventorySlotContents(i2, is.copy());
                    is.stackSize = rest;
                }
            }
        }
        return is;
    }
}
