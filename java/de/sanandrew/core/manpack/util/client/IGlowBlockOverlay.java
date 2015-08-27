package de.sanandrew.core.manpack.util.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

@SideOnly( Side.CLIENT )
public interface IGlowBlockOverlay
{
    @SideOnly( Side.CLIENT )
    IIcon getOverlayTexture(IBlockAccess world, int x, int y, int z, int side);

    @SideOnly( Side.CLIENT )
    IIcon getOverlayInvTexture(int side, int meta);
}
