/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.sanandrew.core.manpack.mod.client.render.RenderSanPlayer;
import de.sanandrew.core.manpack.transformer.ASMNames;
import de.sanandrew.core.manpack.util.SAPReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;

public class RenderPlayerEventHandler
{
    private RenderSanPlayer sanPlayerModel = null;

    private float playerPartTicks = 0.0F;

    private void lazyLoad() {
        if( this.sanPlayerModel == null ) {
            this.sanPlayerModel = new RenderSanPlayer();
            this.sanPlayerModel.setRenderManager(RenderManager.instance);

            if( Minecraft.getMinecraft().getResourceManager() instanceof SimpleReloadableResourceManager ) {
                ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this.sanPlayerModel);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) {
        this.lazyLoad();

        if( event.entityPlayer.getCommandSenderName().equals("sanandreasMC") ) {
            playerPartTicks = event.partialRenderTick;
        }
    }

    @SubscribeEvent
    public void onLivingRender(RenderLivingEvent.Pre event) {
        if( event.entity instanceof EntityPlayer && event.renderer != this.sanPlayerModel && event.entity.getCommandSenderName().equals("sanandreasMC") ) {
            this.sanPlayerModel.doRender(event.entity, event.x, event.y + event.entity.yOffset, event.z, 0.0F, this.playerPartTicks);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    public void onHandRender(RenderHandEvent event) {
        this.lazyLoad();

        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        if( mc.thePlayer.getCommandSenderName().equals("sanandreasMC") ) {
            event.setCanceled(true);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            RenderPlayer rend = (RenderPlayer)RenderManager.instance.getEntityRenderObject(mc.thePlayer);
            RenderManager.instance.entityRenderMap.put(mc.thePlayer.getClass(), this.sanPlayerModel);
            SAPReflectionHelper.invokeCachedMethod(EntityRenderer.class, mc.entityRenderer, "renderHand", ASMNames.M_renderHand, new Class[] {float.class, int.class},
                                                   new Object[] {event.partialTicks, event.renderPass});
            RenderManager.instance.entityRenderMap.put(mc.thePlayer.getClass(), rend);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
