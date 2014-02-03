package sanandreasp.core.manpack.helpers;

import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IGlowBlockOverlay
{
    @SideOnly(Side.CLIENT)
    public Icon getOverlayTexture(IBlockAccess world, int x, int y, int z, int side);
    
    @SideOnly(Side.CLIENT)
    public Icon getOverlayInvTexture(int side, int meta);
}
