package sanandreasp.core.manpack.helpers;

import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IGlowBlockOverlay
{
    @SideOnly(Side.CLIENT)
    public IIcon getOverlayTexture(IBlockAccess world, int x, int y, int z, int side);

    @SideOnly(Side.CLIENT)
    public IIcon getOverlayInvTexture(int side, int meta);
}
