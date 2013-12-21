package sanandreasp.core.manpack.mod.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import sanandreasp.core.manpack.helpers.client.RenderBlockGlowOverlay;
import sanandreasp.core.manpack.mod.CommonProxy;
import sanandreasp.mods.EnderStuffPlus.registry.ESPModRegistry;

public class ClientProxy extends CommonProxy {
	public void registerRenderStuff() {
        RenderBlockGlowOverlay.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderBlockGlowOverlay.renderID, new RenderBlockGlowOverlay());
	}
}
