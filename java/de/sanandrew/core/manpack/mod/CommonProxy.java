package de.sanandrew.core.manpack.mod;

import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.network.NetworkManager;
import de.sanandrew.core.manpack.network.PacketProcessor;
import de.sanandrew.core.manpack.network.ServerPacketHandler;

public class CommonProxy
{
	public void registerRenderStuff() { }

    @Deprecated
	public void registerPackets() {
    }

	public void updOverlayAddUpdMgr(SAPUpdateManager mgr, String version) { }

	public void updOverlayShow() { }

    public void registerPacketHandler(String modId, String modChannel, PacketProcessor packetProcessor) {
        NetworkManager.getPacketChannel(modId).register(new ServerPacketHandler(modId, modChannel));
    }
}
