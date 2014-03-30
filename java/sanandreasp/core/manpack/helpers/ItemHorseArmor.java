package sanandreasp.core.manpack.helpers;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class ItemHorseArmor
    extends Item
{

    public ItemHorseArmor(int par1) {
        super(par1);
    }

    public abstract int getArmorValue(EntityHorse horse, ItemStack stack);

    public abstract String getArmorTexture(EntityHorse horse, ItemStack stack);

}
