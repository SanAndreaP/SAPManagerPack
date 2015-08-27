package de.sanandrew.core.manpack.util.event.entity;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EnderEvent
        extends LivingEvent
{
    public final EntityEnderman entityEnderman;

    public EnderEvent(EntityEnderman entity) {
        super(entity);
        this.entityEnderman = entity;
    }
}
