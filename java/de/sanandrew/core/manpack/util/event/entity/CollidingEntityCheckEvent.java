/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import java.util.List;

public class CollidingEntityCheckEvent
        extends WorldEvent
{
    public final List entityList;
    public final Entity checkingEntity;
    public final AxisAlignedBB checkedBB;

    public CollidingEntityCheckEvent(World world, List entityList, Entity checkingEntity, AxisAlignedBB checkedBB) {
        super(world);

        this.entityList = entityList;
        this.checkingEntity = checkingEntity;
        this.checkedBB = checkedBB;
    }
}
