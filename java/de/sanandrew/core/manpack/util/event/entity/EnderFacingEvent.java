package de.sanandrew.core.manpack.util.event.entity;

import cpw.mods.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
public class EnderFacingEvent
        extends PlayerEvent
{
    public final EntityEnderman entityEnderman;

    public EnderFacingEvent(EntityPlayer player, EntityEnderman enderman) {
        super(player);
        this.entityEnderman = enderman;
    }
}
