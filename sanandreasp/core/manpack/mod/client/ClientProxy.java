package sanandreasp.core.manpack.mod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import sanandreasp.core.manpack.helpers.client.RenderBlockGlowOverlay;
import sanandreasp.core.manpack.mod.CommonProxy;
import sanandreasp.core.manpack.mod.ModContainerManPack;
import sanandreasp.core.manpack.mod.client.packet.PacketWeather;
import sanandreasp.core.manpack.mod.packet.PacketRegistry;
import sanandreasp.mods.EnderStuffPlus.registry.ESPModRegistry;

public class ClientProxy extends CommonProxy {
	public void registerRenderStuff() {
        RenderBlockGlowOverlay.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderBlockGlowOverlay.renderID, new RenderBlockGlowOverlay());
	}
	
	@Override
	public void registerPackets() {
		super.registerPackets();
		
		PacketRegistry.registerPacketHandler(ModContainerManPack.modid, "weather", new PacketWeather());
	}
}
