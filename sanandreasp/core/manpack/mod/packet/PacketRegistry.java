package sanandreasp.core.manpack.mod.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import sanandreasp.core.manpack.mod.ModContainerManPack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.collect.HashBasedTable;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.network.PacketDispatcher;

public final class PacketRegistry {
	private static HashBasedTable<String, String, ISAPPacketHandler> packetHandlers = HashBasedTable.create();
	
	public static void registerPacketHandler(String modID, String name, ISAPPacketHandler handler) {
		if( getPacketHandler(modID, name) != null ) {
			throw new IllegalArgumentException("Packet handler already registered!");
		} else if( handler == null ) {
			throw new IllegalArgumentException("Packet handler cannot be null");
		}
		
		packetHandlers.put(modID, name, handler);
	}
	
	public static ISAPPacketHandler getPacketHandler(String modID, String name) {
		if( name == null || name.length() <= 0 ) {
			throw new IllegalArgumentException("Packet handler name cannot be null or empty!");
		} else if( name.length() > 32 ) {
			throw new IllegalArgumentException("Cannot use a name for a packet handler which is bigger than 32 characters!");
		}
		
		return packetHandlers.get(modID, name);
	}
	
	public static void sendPacketToServer(String modID, String name, Object... data) {
		DataOutputStream dos = null;
		ByteArrayOutputStream bos = null;
		
		try {
			
			byte[] pData = packetHandlers.get(modID, name).getDataForPacket(data);
			int part = 0;
			
			UUID uuid = UUID.randomUUID();
			
			do {
				bos = new ByteArrayOutputStream();
				dos = new DataOutputStream(bos);
				
				dos.writeUTF(modID);
				dos.writeUTF(name);
				dos.writeUTF(uuid.toString());
				dos.writeInt(Math.min(30000, pData.length - part*30000));
				dos.write(pData, part * 30000, Math.min(30000, pData.length - part*30000));
				if( ++part * 30000 >= pData.length ) {
					dos.writeUTF("EOD");
				} else {
					dos.writeUTF("EOP");
				}
				PacketDispatcher.sendPacketToServer(new Packet250CustomPayload(ModContainerManPack.channel, bos.toByteArray()));
				
				dos.close();
			} while( part * 30000 < pData.length );
		} catch( Exception ex ) {
			try {
				if( dos != null )
					dos.close();
				if( bos != null )
					bos.close();
			} catch (IOException e) { }
		}
	}
	
	public static void sendPacketToAllAround(String modID, String name, double X, double Y, double Z, double range, int dimensionId, Object... data) {
		DataOutputStream dos = null;
		ByteArrayOutputStream bos = null;
		
		try {
			
			byte[] pData = packetHandlers.get(modID, name).getDataForPacket(data);
			int part = 0;
			
			UUID uuid = UUID.randomUUID();
			
			do {
				bos = new ByteArrayOutputStream();
				dos = new DataOutputStream(bos);
				
				dos.writeUTF(modID);
				dos.writeUTF(name);
				dos.writeUTF(uuid.toString());
				dos.writeInt(Math.min(30000, pData.length - part*30000));
				dos.write(pData, part * 30000, Math.min(30000, pData.length - part*30000));
				if( ++part * 30000 >= pData.length ) {
					dos.writeUTF("EOD");
				} else {
					dos.writeUTF("EOP");
				}
				PacketDispatcher.sendPacketToAllAround(X, Y, Z, range, dimensionId, new Packet250CustomPayload(ModContainerManPack.channel, bos.toByteArray()));
				
				dos.close();
			} while( part * 30000 < pData.length );
		} catch( Exception ex ) {
			try {
				if( dos != null )
					dos.close();
				if( bos != null )
					bos.close();
			} catch (IOException e) { }
		}
	}
	
	public static void sendPacketToAllPlayers(String modID, String name, Object... data) {
		DataOutputStream dos = null;
		ByteArrayOutputStream bos = null;
		
		try {
			
			byte[] pData = packetHandlers.get(modID, name).getDataForPacket(data);
			int part = 0;
			
			UUID uuid = UUID.randomUUID();
			
			do {
				bos = new ByteArrayOutputStream();
				dos = new DataOutputStream(bos);
				
				dos.writeUTF(modID);
				dos.writeUTF(name);
				dos.writeUTF(uuid.toString());
				dos.writeInt(Math.min(30000, pData.length - part*30000));
				dos.write(pData, part * 30000, Math.min(30000, pData.length - part*30000));
				if( ++part * 30000 >= pData.length ) {
					dos.writeUTF("EOD");
				} else {
					dos.writeUTF("EOP");
				}
				PacketDispatcher.sendPacketToAllPlayers(new Packet250CustomPayload(ModContainerManPack.channel, bos.toByteArray()));
				
				dos.close();
			} while( part * 30000 < pData.length );
		} catch( Exception ex ) {
			FMLLog.warning("An exception occurred while sending a packet");
			ex.printStackTrace();
			try {
				if( dos != null )
					dos.close();
				if( bos != null )
					bos.close();
			} catch (IOException e) { }
		}
	}
}
