/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.sanandrew.core.manpack.mod.client.RenderSanPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;

public class RenderPlayerEventHandler
{
    private RenderSanPlayer sanPlayerModel = new RenderSanPlayer();
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre evt) {
        if( evt.entity.getCommandSenderName().equals("sanandreasMC") && !(evt.renderer instanceof RenderSanPlayer) )
        {
            sanPlayerModel.setRenderManager(RenderManager.instance);
            sanPlayerModel.doRender(evt.entity, evt.x, evt.y + (evt.entity == Minecraft.getMinecraft().thePlayer ? 1.6F : 0.0F), evt.z, 0.0F, (float) renderPosX);
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onRenderModel(Pre evt) {
        renderPosX = evt.partialRenderTick;
    }
}
