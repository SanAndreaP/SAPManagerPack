/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.particle;

import de.sanandrew.core.manpack.util.javatuples.Pair;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class SAPEffectRenderer
{
    private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private int defaultFxLayer = 0;
    private List<Pair<ResourceLocation, ArrayList<EntityParticle>>> fxLayers = new ArrayList<>();
    private TextureManager textureManager;

    private static boolean isInitialized = false;
    public static final SAPEffectRenderer INSTANCE = new SAPEffectRenderer();

    public int getDefaultFxLayer() {
        return this.defaultFxLayer;
    }

    public void addEffect(EntityParticle particle) {
        fxLayers.get(particle.getFXLayer()).getValue1().add(particle);
    }

    public int registerFxLayer(ResourceLocation resource) {
        Pair<ResourceLocation, ArrayList<EntityParticle>> newEntry = Pair.with(resource, new ArrayList<EntityParticle>());
        this.fxLayers.add(newEntry);
        return this.fxLayers.indexOf(newEntry);
    }

    public void updateEffects() {
        for( Pair<ResourceLocation, ArrayList<EntityParticle>> fxLayer : this.fxLayers ) {
            List<EntityParticle> particles = fxLayer.getValue1();
            for( int i = 0; i < particles.size(); i++ ) {
                final EntityParticle particle = particles.get(i);

                try {
                    if( particle != null ) {
                        particle.onUpdate();
                    }
                } catch( Throwable throwable ) {
                    throw new RuntimeException("Error in ticking particle!");
                }

                if( particle == null || particle.isDead ) {
                    particles.remove(i--);
                }
            }
        }
    }

    public void renderParticles(Entity viewingEntity, float partTicks) {
        float rotX = ActiveRenderInfo.rotationX;
        float rotZ = ActiveRenderInfo.rotationZ;
        float rotYZ = ActiveRenderInfo.rotationYZ;
        float rotXY = ActiveRenderInfo.rotationXY;
        float rotXZ = ActiveRenderInfo.rotationXZ;
        EntityParticle.interpPosX = viewingEntity.lastTickPosX + (viewingEntity.posX - viewingEntity.lastTickPosX) * (double)partTicks;
        EntityParticle.interpPosY = viewingEntity.lastTickPosY + (viewingEntity.posY - viewingEntity.lastTickPosY) * (double)partTicks;
        EntityParticle.interpPosZ = viewingEntity.lastTickPosZ + (viewingEntity.posZ - viewingEntity.lastTickPosZ) * (double)partTicks;

        for( Pair<ResourceLocation, ArrayList<EntityParticle>> layer : this.fxLayers ) {
            List<EntityParticle> particles = layer.getValue1();
            if( !particles.isEmpty() ) {
                this.textureManager.bindTexture(layer.getValue0());

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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

        INSTANCE.defaultFxLayer = INSTANCE.registerFxLayer(PARTICLE_TEXTURES);
    }
}
