package de.sanandrew.core.manpack.mod;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

import de.sanandrew.core.manpack.managers.SAPUpdateManager;

public class TickEventUpdMgr
{
    @SubscribeEvent
    public void onWorldTickStart(TickEvent.WorldTickEvent event) {
        if(event.phase == Phase.END) {
            for( SAPUpdateManager udm : SAPUpdateManager.UPD_MANAGERS ) {
    //            if( (!ManPackLoadingPlugin.isServer() && Minecraft.getMinecraft().thePlayer != null) || ManPackLoadingPlugin.isServer() ) {
                    udm.checkForUpdate();
    //            }
            }
        }
    }
}
