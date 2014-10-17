package de.sanandrew.core.manpack.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockNamedMeta extends ItemBlockWithMetadata
{
	public ItemBlockNamedMeta(Block block) {
		super(block, block);
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + '_' + par1ItemStack.getItemDamage();
	}
}
