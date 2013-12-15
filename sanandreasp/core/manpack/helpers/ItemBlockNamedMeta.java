package sanandreasp.core.manpack.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockNamedMeta extends ItemBlockWithMetadata {

	public ItemBlockNamedMeta(int par1, Block par2Block) {
		super(par1, par2Block);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return super.getUnlocalizedName(par1ItemStack) + "_" + par1ItemStack.getItemDamage() ;
	}

}
