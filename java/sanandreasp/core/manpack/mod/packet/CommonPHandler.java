package sanandreasp.core.manpack.mod.packet;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.HashBasedTable;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class CommonPHandler implements IPacketHandler
{
	private HashBasedTable<UUID, Integer, byte[]> buffer = HashBasedTable.create();

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		String modid = "[UNKNOWN]";
		String name = "[UNKNOWN]";

		try( ByteArrayInputStream bis = new ByteArrayInputStream(packet.data);
		     DataInputStream dis = new DataInputStream(bis) )
		{
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

					try( ByteArrayInputStream packetBis = new ByteArrayInputStream(ArrayUtils.toPrimitive(lst.toArray(new Byte[0])));
					     DataInputStream packetDis = new DataInputStream(packetBis) )
					{
					    handler.processData(manager, player, packetDis);
	                    for( int i = 0; i < this.buffer.row(uuid).size(); i++ ) {
	                        this.buffer.remove(uuid, i);
	                    }
					}
				}
			}
		} catch( Throwable ex ) {
			FMLLog.warning("Cannot process packet data! Packet %s in mod %s", name, modid);
			ex.printStackTrace();
		}
	}
}
