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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;

@SideOnly( Side.CLIENT )
public class EventWorldRenderLast
{
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if( mc.entityRenderer.debugViewDirection == 0 ) {
            mc.entityRenderer.enableLightmap(event.partialTicks);
            SAPEffectRenderer.INSTANCE.renderParticles(Minecraft.getMinecraft().renderViewEntity, event.partialTicks, false);
            SAPEffectRenderer.INSTANCE.renderParticles(Minecraft.getMinecraft().renderViewEntity, event.partialTicks, true);
            mc.entityRenderer.disableLightmap(event.partialTicks);
        }
    }

    @SubscribeEvent
    public void onClientPostTick(ClientTickEvent event) {
        if( event.phase == Phase.END && !Minecraft.getMinecraft().isGamePaused() ) {
            SAPEffectRenderer.INSTANCE.updateEffects();
        }
    }
}
