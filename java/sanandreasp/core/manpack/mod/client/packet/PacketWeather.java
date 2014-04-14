package sanandreasp.core.manpack.mod.client.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import sanandreasp.core.manpack.mod.packet.ISAPPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.INetworkManager;
import net.minecraft.world.storage.WorldInfo;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PacketWeather implements ISAPPacketHandler
{
    @Override
    public void getDataForPacket(DataOutputStream doStream, Object... data) throws Exception {
        doStream.writeShort((Short)data[0]);
        doStream.writeInt((Integer)data[1]);
    }

    @Override
    public void processData(INetworkManager manager, Player player, DataInputStream diStream) throws Exception {
        WorldClient worldObj = Minecraft.getMinecraft().theWorld;

        int id = diStream.readShort();
        int time = diStream.readInt();
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
    }
}
