package de.sanandrew.core.manpack.helpers;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemHorseArmor
    extends Item
{

    public ItemHorseArmor() {
        super();
    }

    public abstract int getArmorValue(EntityHorse horse, ItemStack stack);

    public abstract String getArmorTexture(EntityHorse horse, ItemStack stack);

}
