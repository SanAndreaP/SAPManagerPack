/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.particle;

import net.minecraft.world.World;

//FIXME: REMOVE WHEN UPDATING TO NEW MC VERSION!
@Deprecated
public class EntityParticle
        extends de.sanandrew.core.manpack.util.client.EntityParticle
{
    public EntityParticle(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityParticle(World world, double x, double y, double z, double motX, double motY, double motZ) {
        super(world, x, y, z, motX, motY, motZ);
    }
}
