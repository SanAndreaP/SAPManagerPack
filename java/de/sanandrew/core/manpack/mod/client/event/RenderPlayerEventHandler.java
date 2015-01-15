/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.sanandrew.core.manpack.mod.client.render.RenderSanPlayer;
import de.sanandrew.core.manpack.util.client.event.GetRenderObjEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class RenderPlayerEventHandler
{
    private RenderSanPlayer sanPlayerModel = null;

    @SubscribeEvent
    public void onGetRenderObj(GetRenderObjEvent evt) {
        if( this.sanPlayerModel == null ) {
            this.sanPlayerModel = new RenderSanPlayer();
            this.sanPlayerModel.setRenderManager(RenderManager.instance);
        }

        if( evt.entity instanceof EntityPlayer && evt.entity.getCommandSenderName().equals("sanandreasMC") ) {
            evt.newReturn = this.sanPlayerModel;
        }
    }
}
