package sanandreasp.core.manpack.mod.client.packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.storage.WorldInfo;
import sanandreasp.core.manpack.mod.ModContainerManPack;
import sanandreasp.core.manpack.mod.packet.ISAPPacketHandler;

public class PacketWeather implements ISAPPacketHandler {

	@Override
	public byte[] getDataForPacket(Object... data) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
	
		dos.writeShort((Short)data[0]);
		dos.writeInt((Integer)data[1]);
		
		byte[] bData = bos.toByteArray();
		
		dos.close();
		
		return bData;
	}

	@Override
	public void processData(INetworkManager manager, Player player, byte[] data) throws Exception {
		WorldClient worldObj = Minecraft.getMinecraft().theWorld;
		
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bis);
		
		int id = dis.readShort();
		int time = dis.readInt();
		WorldInfo wInfo = worldObj.getWorldInfo();

		wInfo.setRainTime(time);
		wInfo.setThunderTime(time);
		
		switch(id) {
		case 0:
			wInfo.setThundering(false);
			break;
		case 1:
			wInfo.setThundering(true);
			break;
		}
		
		dis.close();
	}

}
