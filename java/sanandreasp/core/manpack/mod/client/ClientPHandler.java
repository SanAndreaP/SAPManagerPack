package sanandreasp.core.manpack.mod.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

import sanandreasp.core.manpack.mod.packet.ISAPPacketHandler;
import sanandreasp.core.manpack.mod.packet.PacketRegistry;

import com.google.common.collect.HashBasedTable;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class ClientPHandler implements IPacketHandler {
	private HashBasedTable<UUID, Integer, byte[]> buffer = HashBasedTable.create();
	
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream dis;
		ByteArrayInputStream bis;
		
		String modid = "";
		String name = "";
		
		try {
			bis = new ByteArrayInputStream(packet.data);
			dis = new DataInputStream(bis);
			
			modid = dis.readUTF();
			name = dis.readUTF();
			ISAPPacketHandler handler;
			if( (handler = PacketRegistry.getPacketHandler(modid, name)) != null ) {
				UUID uuid = UUID.fromString(dis.readUTF());
				int len = dis.readInt();
				byte[] b = new byte[len];
				dis.read(b, 0, len);
				
				this.buffer.put(uuid, this.buffer.row(uuid).size(), b);
				
				if( dis.readUTF().equals("EOD") ) {
					List<Byte> lst = new ArrayList<Byte>();
					for( int i = 0; i < this.buffer.row(uuid).size(); i++ ) {
						lst.addAll(Arrays.<Byte>asList(ArrayUtils.toObject(this.buffer.get(uuid, i))));
					}
					handler.processData(manager, player, ArrayUtils.toPrimitive(lst.toArray(new Byte[0])));
				}
			}
		} catch( Exception ex ) {
			FMLLog.warning("Cannot process packet data! Packet %s in mod %s", name, modid);
			ex.printStackTrace();
		}
	}

}
