/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.item;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AItemHorseArmor
        extends Item
{
    /**
     * Returns the armor value when equipped on a horse. Vanilla uses:<br>
     * - Iron: 5<br>
     * - Gold: 7<br>
     * - Diamond: 11<br>
     *
     * @param horse     The Horse, which has this item equipped
     * @param stack     The ItemStack instance of this item from the Horse
     * @return The armor value
     */
    public abstract int getArmorValue(EntityHorse horse, ItemStack stack);

    /**
     * Returns the location of the custom horse armor texture, similar to how player armor texture works.
     *
     * @param horse     The Horse, which has this item equipped
     * @param stack     The ItemStack instance of this item from the Horse
     * @return The location of the custom horse armor
     */
    public abstract String getArmorTexture(EntityHorse horse, ItemStack stack);
}
