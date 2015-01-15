/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import de.sanandrew.core.manpack.mod.client.render.RenderSanPlayer;
import de.sanandrew.core.manpack.util.client.event.GetRenderObjEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class RenderPlayerEventHandler
{
    private RenderSanPlayer sanPlayerModel = null;

    @SubscribeEvent
    public void onGetRenderObj(GetRenderObjEvent evt) {
        if( sanPlayerModel == null ) {
            sanPlayerModel = new RenderSanPlayer();
            sanPlayerModel.setRenderManager(RenderManager.instance);
        }

        if( evt.entity instanceof EntityPlayer && evt.entity.getCommandSenderName().equals("sanandreasMC") ) {
            evt.newReturn = sanPlayerModel;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if( event.phase == Phase.END && event.side == Side.CLIENT ) {
            event.player.worldObj.spawnParticle("reddust", event.player.posX, event.player.posY, event.player.posZ, 1F, 1F, 1F);
        }
    }
}
