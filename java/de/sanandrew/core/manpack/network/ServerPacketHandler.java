/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraft.network.NetHandlerPlayServer;

public final class ServerPacketHandler
{
    private final String channel;
    private final String modId;

    public ServerPacketHandler(String modId, String modChannel) {
        this.channel = modChannel;
        this.modId = modId;
    }

    @SubscribeEvent
    public void onServerPacket(ServerCustomPacketEvent event) {
        NetHandlerPlayServer netHandlerPlayServer = (NetHandlerPlayServer) event.handler;

        if( event.packet.channel().equals(this.channel) ) {
            NetworkManager.getPacketProcessor(this.modId).processPacket(event.packet.payload(), netHandlerPlayServer);
        }
    }
}
