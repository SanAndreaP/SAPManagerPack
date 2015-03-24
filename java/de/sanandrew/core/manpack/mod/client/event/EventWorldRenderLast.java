/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class EventWorldRenderLast
{
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if( Minecraft.getMinecraft().entityRenderer.debugViewDirection == 0 ) {
            Minecraft.getMinecraft().entityRenderer.enableLightmap(event.partialTicks);
            SAPEffectRenderer.INSTANCE.renderParticles(Minecraft.getMinecraft().renderViewEntity, event.partialTicks, false);
            SAPEffectRenderer.INSTANCE.renderParticles(Minecraft.getMinecraft().renderViewEntity, event.partialTicks, true);
            Minecraft.getMinecraft().entityRenderer.disableLightmap(event.partialTicks);
        }
    }

    @SubscribeEvent
    public void onClientPostTick(ClientTickEvent event) {
        if( event.phase == Phase.END && !Minecraft.getMinecraft().isGamePaused() ) {
            SAPEffectRenderer.INSTANCE.updateEffects();
        }
    }
}
