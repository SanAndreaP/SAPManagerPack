/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client;

import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class EntityParticle
        extends EntityFX
{
    protected int brightness = -1;

    public EntityParticle(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityParticle(World world, double x, double y, double z, double motX, double motY, double motZ) {
        super(world, x, y, z, motX, motY, motZ);
    }

    @Override
    public int getFXLayer() {
        return SAPEffectRenderer.INSTANCE.getDefaultFxLayer();
    }

    public void setParticleTextureIndex(int index) {
        this.particleTextureIndexX = index % 16;
        this.particleTextureIndexY = index / 16;
    }

    public void setParticleColor(float red, float green, float blue) {
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
    }

    public void setParticleColorRNG(float red, float green, float blue) {
        float shade = this.rand.nextFloat() * 0.2F;
        this.particleRed = Math.max(red - 0.2F, 0.0F) + shade;
        this.particleGreen = Math.max(green - 0.2F, 0.0F) + shade;
        this.particleBlue = Math.max(blue - 0.2F, 0.0F) + shade;
    }

    @Override
    public int getBrightnessForRender(float partTicks) {
        return this.brightness < 0 ? super.getBrightnessForRender(partTicks) : this.brightness;
    }

    public void setBrightness(int bright) {
        this.brightness = bright;
    }
}
