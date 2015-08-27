/**
 * ****************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 * *****************************************************************************************************************
 */
package de.sanandrew.core.manpack.util.helpers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * A helper class for inventory stuff
 */
public final class InventoryUtils
{
    /**
     * Decreases the player's held stack size by the amount.
     * Sends the changes to the client as well.
     *
     * @param player the player whose held stack size should be decreased
     * @param amount the amount the size is decreased by
     * @return the held stack with decreased size, or null if the stack empties out
     */
    public static ItemStack decrPlayerHeldStackSize(EntityPlayer player, int amount) {
        ItemStack stack = player.getHeldItem();
        if( stack == null ) {
            return null;
        }

        if( amount < 1 ) {
            return stack;
        }

        stack.stackSize -= amount;
        if( stack.stackSize <= 0 ) {
            player.setCurrentItemOrArmor(0, null);
            stack = null;
        } else {
            player.setCurrentItemOrArmor(0, stack.copy()); // resetting the stack with a copy prevents the creative inventory from f***ing over...
        }
        player.inventoryContainer.detectAndSendChanges();

        return stack;
    }

    /**
     * Adds the stack to the given inventory.<br>
     * <b>Note</b>: if you have a {@link net.minecraft.inventory.Container} instance available,
     * please call {@link Container#detectAndSendChanges()} to send the changes to the client!
     *
     * @param is  the ItemStack to be added
     * @param inv the Inventory the stack should be added to
     * @return the remaining stack if the inventory is full / fills out or null if it added the complete stack
     */
    public static ItemStack addStackToInventory(ItemStack is, IInventory inv) {
        return addStackToInventory(is, inv, true);
    }

    public static ItemStack addStackToInventory(ItemStack is, IInventory inv, boolean checkNBT) {
        int invSize = inv.getSizeInventory() - (inv instanceof InventoryPlayer ? 4 : 0);

        ItemStack invIS;
        int rest;
        for( int i = 0; i < invSize && is != null; ++i ) {
            invIS = inv.getStackInSlot(i);
            if( invIS != null && ItemUtils.areStacksEqual(is, invIS, checkNBT) ) {
                rest = is.stackSize + invIS.stackSize;
                int maxStack = Math.min(invIS.getMaxStackSize(), inv.getInventoryStackLimit());
                if( rest <= maxStack ) {
                    invIS.stackSize = rest;
                    inv.setInventorySlotContents(i, invIS.copy());
                    is = null;
                    break;
                }

                int rest1 = rest - maxStack;
                invIS.stackSize = maxStack;
                inv.setInventorySlotContents(i, invIS.copy());
                is.stackSize = rest1;
            } else if( invIS == null && inv.isItemValidForSlot(i, is) ) {
                if( is.stackSize <= inv.getInventoryStackLimit() ) {
                    inv.setInventorySlotContents(i, is.copy());
                    is = null;
                    break;
                }

                rest = is.stackSize - inv.getInventoryStackLimit();
                is.stackSize = inv.getInventoryStackLimit();
                inv.setInventorySlotContents(i, is.copy());
                is.stackSize = rest;
            }
        }

        return is;
    }
}
