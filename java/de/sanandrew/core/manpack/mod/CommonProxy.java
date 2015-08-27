/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod;

import de.sanandrew.core.manpack.network.NetworkManager;
import de.sanandrew.core.manpack.network.PacketProcessor;
import de.sanandrew.core.manpack.network.ServerPacketHandler;

public class CommonProxy
{
    public void registerRenderStuff() { }

    public void registerPacketHandler(String modId, String modChannel, PacketProcessor packetProcessor) {
        NetworkManager.getPacketChannel(modId).register(new ServerPacketHandler(modId, modChannel));
    }
}
