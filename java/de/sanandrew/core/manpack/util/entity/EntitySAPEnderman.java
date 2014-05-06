package de.sanandrew.core.manpack.util.entity;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;

import de.sanandrew.core.manpack.util.event.entity.EnderFacingEvent;
import de.sanandrew.core.manpack.util.event.entity.EnderSpawnParticleEvent;

public class EntitySAPEnderman
    extends EntityEnderman
{
    public EntitySAPEnderman(World par1World) {
        super(par1World);
        
        int k;
        
        if(MinecraftForge.EVENT_BUS.post(new EnderSpawnParticleEvent(this, 0, 0, 0, 0, 0, 0, EnderSpawnParticleEvent.EnderParticleType.IDLE_FX)))
        for (k = 0; k < 2; ++k)
        {
            this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }
        
        for (k = 0; k < 2; ++k)
        {
            this.worldObj.spawnParticle("portal", this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
        }
    }
    
    public enum EnumParticleType {
        TELEPORT, LIVING_UPDATE
    }
}
