/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class EntityParticle
    extends EntityFX
{

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
}
