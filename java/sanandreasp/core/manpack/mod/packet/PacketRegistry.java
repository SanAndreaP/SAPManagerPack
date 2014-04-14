package sanandreasp.core.manpack.mod.packet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import sanandreasp.core.manpack.helpers.javatuples.Quintet;
import sanandreasp.core.manpack.helpers.javatuples.Tuple;
import sanandreasp.core.manpack.helpers.javatuples.Unit;
import sanandreasp.core.manpack.mod.ModContainerManPack;

import com.google.common.collect.HashBasedTable;

import net.minecraft.network.packet.Packet250CustomPayload;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

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

	private static void sendPacket(String modID, String name, EnumPacketType packetType, Tuple packetData, Object... data) {
	    try( ByteArrayOutputStream bos = new ByteArrayOutputStream();
	         DataOutputStream dos = new DataOutputStream(bos) )
	    {
	        packetHandlers.get(modID, name).getDataForPacket(dos, data);
	        byte[] pData = bos.toByteArray();
            int part = 0;

            UUID uuid = UUID.randomUUID();

            do {
                try( ByteArrayOutputStream singleBos = new ByteArrayOutputStream();
                     DataOutputStream singleDos = new DataOutputStream(singleBos) )
                {
                    singleDos.writeUTF(modID);
                    singleDos.writeUTF(name);
                    singleDos.writeUTF(uuid.toString());
                    singleDos.writeInt(Math.min(30000, pData.length - part*30000));
                    singleDos.write(pData, part * 30000, Math.min(30000, pData.length - part*30000));
                    if( ++part * 30000 >= pData.length ) {
                        singleDos.writeUTF("EOD");
                    } else {
                        singleDos.writeUTF("EOP");
                    }

                    Packet250CustomPayload packet = new Packet250CustomPayload(ModContainerManPack.channel, singleBos.toByteArray());

                    switch( packetType ) {
                        case TO_ALL_AROUND :
                            @SuppressWarnings("unchecked")
                            Quintet<Double, Double, Double, Integer, Integer> pdq = Quintet.class.cast(packetData);
                            PacketDispatcher.sendPacketToAllAround(pdq.getValue0(), pdq.getValue1(), pdq.getValue2(), pdq.getValue3(),
                                                                   pdq.getValue4(), packet);
                            break;
                        case TO_ALL_PLAYERS :
                            PacketDispatcher.sendPacketToAllPlayers(packet);
                            break;
                        case TO_PLAYER :
                            @SuppressWarnings("unchecked")
                            Unit<Player> pdu = Unit.class.cast(packetData);
                            PacketDispatcher.sendPacketToPlayer(packet, pdu.getValue0());
                            break;
                        case TO_SERVER :
                            PacketDispatcher.sendPacketToServer(packet);
                            break;
                        default :
                            break;
                    }
                }
            } while( part * 30000 < pData.length );
	    } catch( Throwable e ) {
            switch( packetType ) {
                case TO_ALL_AROUND :
                    FMLLog.warning("Cannot send packet to all around! Packet %s in mod %s", name, modID);
                    break;
                case TO_ALL_PLAYERS :
                    FMLLog.warning("Cannot send packet to all players! Packet %s in mod %s", name, modID);
                    break;
                case TO_PLAYER :
                    FMLLog.warning("Cannot send packet to player! Packet %s in mod %s", name, modID);
                    break;
                case TO_SERVER :
                    FMLLog.warning("Cannot send packet to server! Packet %s in mod %s", name, modID);
                    break;
                default :
                    break;
            }
            e.printStackTrace();
        }
	}

	public static void sendPacketToServer(String modID, String name, Object... data) {
	    sendPacket(modID, name, EnumPacketType.TO_SERVER, null, data);
	}

	public static void sendPacketToAllAround(String modID, String name, double x, double y, double z, double range, int dimensionId, Object... data) {
	    sendPacket(modID, name, EnumPacketType.TO_ALL_AROUND, Quintet.with(x, y, z, range, dimensionId), data);
	}

	public static void sendPacketToPlayer(String modID, String name, Player player, Object... data) {
	    sendPacket(modID, name, EnumPacketType.TO_PLAYER, Unit.with(player), data);
	}

	public static void sendPacketToAllPlayers(String modID, String name, Object... data) {
	    sendPacket(modID, name, EnumPacketType.TO_ALL_PLAYERS, null, data);
	}

	private static enum EnumPacketType
	{
	    TO_SERVER,
	    TO_ALL_AROUND,
	    TO_PLAYER,
	    TO_ALL_PLAYERS;
	}
}
