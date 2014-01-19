package sanandreasp.core.manpack.mod.packet;

import cpw.mods.fml.common.network.Player;
import net.minecraft.network.INetworkManager;

public interface ISAPPacketHandler {
	public byte[] getDataForPacket(Object... data) throws Exception;
	public void processData(INetworkManager manager, Player player, byte[] data) throws Exception;
}
