package de.sanandrew.core.manpack.util.event.entity;

import net.minecraft.entity.monster.EntityEnderman;

import cpw.mods.fml.common.eventhandler.Cancelable;

import de.sanandrew.core.manpack.util.javatuples.Triplet;

@Cancelable
@Deprecated
public class EnderSpawnParticleEvent
    extends EnderEvent
{
    public Triplet<Double, Double, Double> particlePosition;
    public Triplet<Double, Double, Double> particleData;
    public final EnderParticleType particleType;

    public EnderSpawnParticleEvent(EntityEnderman enderman, double x, double y, double z, double data1, double data2, double data3, EnderParticleType type) {
        super(enderman);
        this.particlePosition = Triplet.with(x, y, z);
        this.particleData = Triplet.with(data1, data2, data3);
        this.particleType = type;
    }

    public static enum EnderParticleType {
        IDLE_FX,
        TELEPORT_FX
    }

    public double getX() {
        return this.particlePosition.getValue0();
    }

    public double getY() {
        return this.particlePosition.getValue1();
    }

    public double getZ() {
        return this.particlePosition.getValue2();
    }

    public double getD1() {
        return this.particleData.getValue0();
    }

    public double getD2() {
        return this.particleData.getValue1();
    }

    public double getD3() {
        return this.particleData.getValue2();
    }
}
