package sanandreasp.core.manpack.mod.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.storage.WorldInfo;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPacketHandler implements IPacketHandler {

	public ClientPacketHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		WorldClient worldObj = Minecraft.getMinecraft().theWorld;
		
		DataInputStream dis;
		ByteArrayInputStream bis;
		
		bis = new ByteArrayInputStream(packet.data);
		dis = new DataInputStream(bis);
		
		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				dis.close();
			} catch (IOException e) {
				;
			}
		}
	}

}
