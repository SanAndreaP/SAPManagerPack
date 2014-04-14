package sanandreasp.core.manpack.mod.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import net.minecraft.network.INetworkManager;

import cpw.mods.fml.common.network.Player;

public interface ISAPPacketHandler
{
	public void getDataForPacket(DataOutputStream doStream, Object... data) throws Exception;
	public void processData(INetworkManager manager, Player player, DataInputStream diStream) throws Exception;
}
