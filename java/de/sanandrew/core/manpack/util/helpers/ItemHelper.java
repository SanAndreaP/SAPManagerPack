/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

//FIXME: REMOVE WHEN UPDATING TO NEW MC VERSION!
@Deprecated
final class ItemHelper
{
    @Deprecated
    static ItemStack decrInvStackSize(ItemStack is, int amount) {
        ItemStack returnedStack = is.copy();

        returnedStack.stackSize = amount;
        is.stackSize -= amount;

        return returnedStack;
    }

    @Deprecated
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
                return SAPUtils.areStacksEqual((ItemStack) instance1, (ItemStack) instance2, false);
            }
        }
        return false;
    }
}
