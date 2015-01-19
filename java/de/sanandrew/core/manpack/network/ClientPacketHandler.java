/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraft.client.network.NetHandlerPlayClient;

public final class ClientPacketHandler
{
    private final String channel;
    private final String modId;

    public ClientPacketHandler(String modId, String modChannel) {
        this.channel = modChannel;
        this.modId = modId;
    }

    @SubscribeEvent
    public void onClientPacket(ClientCustomPacketEvent event) {
        NetHandlerPlayClient netHandlerPlayClient = (NetHandlerPlayClient) event.handler;

        if( event.packet.channel().equals(this.channel) ) {
            NetworkManager.getPacketProcessor(this.modId).processPacket(event.packet.payload(), netHandlerPlayClient);
        }
    }
}
