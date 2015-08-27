/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.particle;

import com.google.common.base.Predicate;
import com.google.common.collect.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.client.EntityParticle;
import de.sanandrew.core.manpack.util.client.event.SAPFxLayerRenderEvent;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import de.sanandrew.core.manpack.util.javatuples.Pair;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.*;
import java.util.Map.Entry;

@SideOnly( Side.CLIENT )
public class SAPEffectRenderer
{
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private int defaultFxLayer = 0;
    private Map<Integer, Pair<ResourceLocation, Boolean>> fxLayers = new HashMap<>();
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
                        throw new RuntimeException("Couldn't render particle!", throwable);
                    }
                }

                this.textureManager.bindTexture(layerData.getValue0());
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                if( layerData.getValue1() ) {
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                }

                SAPUtils.EVENT_BUS.post(new SAPFxLayerRenderEvent.Pre(layer.getKey(), tessellator));
                tessellator.draw();
                SAPUtils.EVENT_BUS.post(new SAPFxLayerRenderEvent.Post(layer.getKey()));

                if( layerData.getValue1() ) {
                    GL11.glDisable(GL11.GL_BLEND);
                }
            }
        }
    }

    public static void initialize(TextureManager texManager) {
        if( isInitialized ) {
            return;
        }

        isInitialized = true;

        INSTANCE.textureManager = texManager;

        INSTANCE.defaultFxLayer = INSTANCE.registerFxLayer(PARTICLE_TEXTURES, false);
    }

    private static class SortingFilter
            implements Predicate<Entry<Integer, Pair<ResourceLocation, Boolean>>>
    {
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
