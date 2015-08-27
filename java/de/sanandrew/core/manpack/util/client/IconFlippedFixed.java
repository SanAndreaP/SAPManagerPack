package de.sanandrew.core.manpack.util.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

@SideOnly( Side.CLIENT )
public class IconFlippedFixed
        implements IIcon
{
    private final IIcon baseIcon;
    private final boolean flipU;
    private final boolean flipV;

    public IconFlippedFixed(IIcon icon, boolean doFlipU, boolean doFlipV) {
        this.baseIcon = icon;
        this.flipU = doFlipU;
        this.flipV = doFlipV;
    }

    /**
     * Returns the width of the icon, in pixels.
     */
    @Override
    public int getIconWidth() {
        return this.baseIcon.getIconWidth();
    }

    /**
     * Returns the height of the icon, in pixels.
     */
    @Override
    public int getIconHeight() {
        return this.baseIcon.getIconHeight();
    }

    /**
     * Returns the minimum U coordinate to use when rendering with this icon.
     */
    @Override
    public float getMinU() {
        return this.flipU ? this.baseIcon.getMaxU() : this.baseIcon.getMinU();
    }

    /**
     * Returns the maximum U coordinate to use when rendering with this icon.
     */
    @Override
    public float getMaxU() {
        return this.flipU ? this.baseIcon.getMinU() : this.baseIcon.getMaxU();
    }

    /**
     * Gets a U coordinate on the icon. 0 returns uMin and 16 returns uMax. Other arguments return in-between values.
     */
    @Override
    public float getInterpolatedU(double par1) {
        float diffU = this.getMaxU() - this.getMinU();
        return this.getMinU() + diffU * ((float) par1 / 16.0F);
    }

    /**
     * Returns the minimum V coordinate to use when rendering with this icon.
     * <p/>
     * - This method is fixed to correctly flip V, since vanilla returns always
     * this.baseIcon.getMinV() which causes texture errors - ~SanAndreasP
     */
    @Override
    public float getMinV() {
        return this.flipV ? this.baseIcon.getMaxV() : this.baseIcon.getMinV();
    }

    /**
     * Returns the maximum V coordinate to use when rendering with this icon.
     */
    @Override
    public float getMaxV() {
        return this.flipV ? this.baseIcon.getMinV() : this.baseIcon.getMaxV();
    }

    @Override
    public float getInterpolatedV(double par1) {
        float diffV = this.getMaxV() - this.getMinV();
        return this.getMinV() + diffV * ((float) par1 / 16.0F);
    }

    @Override
    public String getIconName() {
        return this.baseIcon.getIconName();
    }
}
