package de.sanandrew.core.manpack.util.entity;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;

import de.sanandrew.core.manpack.util.event.entity.EnderFacingEvent;

public class EntitySAPEnderman
    extends EntityEnderman
{
    public EntitySAPEnderman(World par1World) {
        super(par1World);
    }
    
    public enum EnumParticleType {
        TELEPORT, LIVING_UPDATE
    }
}
