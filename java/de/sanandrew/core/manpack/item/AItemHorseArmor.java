package de.sanandrew.core.manpack.item;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class AItemHorseArmor
    extends Item
{
    public abstract int getArmorValue(EntityHorse horse, ItemStack stack);

    public abstract String getArmorTexture(EntityHorse horse, ItemStack stack);

}
