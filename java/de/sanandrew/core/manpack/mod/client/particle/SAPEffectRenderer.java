/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.particle;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import de.sanandrew.core.manpack.util.javatuples.Pair;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

public class SAPEffectRenderer
{
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private int defaultFxLayer = 0;
    private Map<Integer, Pair<ResourceLocation, Boolean>> fxLayers = Maps.newHashMap();
    private Multimap<Integer, EntityParticle> particles = ArrayListMultimap.create();
    private TextureManager textureManager;

    private static boolean isInitialized = false;
    public static final SAPEffectRenderer INSTANCE = new SAPEffectRenderer();

    public int getDefaultFxLayer() {
        return this.defaultFxLayer;
    }

    public void addEffect(EntityParticle particle) {
        particles.put(particle.getFXLayer(), particle);
    }

    public int registerFxLayer(ResourceLocation resource, boolean hasAlpha) {
        Pair<ResourceLocation, Boolean> newEntry = Pair.with(resource, hasAlpha);
        int newIndex = this.fxLayers.size();
        this.fxLayers.put(newIndex, newEntry);
        return newIndex;
    }

    public void updateEffects() {
        Iterator<EntityParticle> particleIt = this.particles.values().iterator();
        while( particleIt.hasNext() ) {
            EntityParticle particle = particleIt.next();
            try {
                if( particle != null ) {
                    particle.onUpdate();
                }
            } catch( Throwable throwable ) {
                throw new RuntimeException("Error in ticking particle!");
            }

            if( particle == null || particle.isDead ) {
                particleIt.remove();
            }
        }
    }

    public void renderParticles(Entity viewingEntity, float partTicks, boolean alpha) {
        float rotX = ActiveRenderInfo.rotationX;
        float rotZ = ActiveRenderInfo.rotationZ;
        float rotYZ = ActiveRenderInfo.rotationYZ;
        float rotXY = ActiveRenderInfo.rotationXY;
        float rotXZ = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = viewingEntity.lastTickPosX + (viewingEntity.posX - viewingEntity.lastTickPosX) * partTicks;
        EntityFX.interpPosY = viewingEntity.lastTickPosY + (viewingEntity.posY - viewingEntity.lastTickPosY) * partTicks;
        EntityFX.interpPosZ = viewingEntity.lastTickPosZ + (viewingEntity.posZ - viewingEntity.lastTickPosZ) * partTicks;

        Collection<Entry<Integer, Pair<ResourceLocation, Boolean>>> currLayers = Collections2.filter(this.fxLayers.entrySet(), new SortingFilter(alpha));

        for( Entry<Integer, Pair<ResourceLocation, Boolean>> layer : currLayers ) {
            Pair<ResourceLocation, Boolean> layerData = layer.getValue();
            Collection<EntityParticle> particles = this.particles.get(layer.getKey());
            if( !particles.isEmpty() ) {
                this.textureManager.bindTexture(layerData.getValue0());

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if( layerData.getValue1() ) {
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                }
//                GL11.glDepthMask(false);
//                GL11.glEnable(GL11.GL_BLEND);
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();

                for( final EntityParticle particle : particles ) {
                    if( particle == null ) {
                        continue;
                    }

                    tessellator.setBrightness(particle.getBrightnessForRender(partTicks));

                    try {
                        particle.renderParticle(tessellator, partTicks, rotX, rotXZ, rotZ, rotYZ, rotXY);
                    } catch( Throwable throwable ) {
                        throw new RuntimeException("Couldn't render particle!");
                    }
                }

                tessellator.draw();
                if( layerData.getValue1() ) {
                    GL11.glDisable(GL11.GL_BLEND);
//                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                }
//                GL11.glDisable(GL11.GL_BLEND);
//                GL11.glDepthMask(true);
//                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            }
        }
    }

    public static void initialize(TextureManager texManager) {
        if( isInitialized ) {
//            FMLLog.log(ModCntManPack.MOD_LOG, Level.ERROR, "Cannot reinitialize SAP-EffectRenderer!");
            return;
        }

        isInitialized = true;

        INSTANCE.textureManager = texManager;

        INSTANCE.defaultFxLayer = INSTANCE.registerFxLayer(PARTICLE_TEXTURES, false);
    }

    private static class SortingFilter implements Predicate<Entry<Integer, Pair<ResourceLocation, Boolean>>> {
        private final boolean hasAlpha;

        public SortingFilter(boolean alpha) {
            this.hasAlpha = alpha;
        }

        @Override
        public boolean apply(@Nullable Entry<Integer, Pair<ResourceLocation, Boolean>> input) {
            return input != null && input.getValue().getValue1() == this.hasAlpha;
        }
    }
}
