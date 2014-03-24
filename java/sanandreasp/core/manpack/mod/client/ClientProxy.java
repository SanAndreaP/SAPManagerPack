package sanandreasp.core.manpack.mod.client;

import sanandreasp.core.manpack.helpers.client.RenderBlockGlowOverlay;
import sanandreasp.core.manpack.mod.CommonProxy;
import sanandreasp.core.manpack.mod.ModContainerManPack;
import sanandreasp.core.manpack.mod.client.packet.PacketWeather;
import sanandreasp.core.manpack.mod.packet.PacketRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
    public void registerRenderStuff() {
        RenderBlockGlowOverlay.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderBlockGlowOverlay.renderID, new RenderBlockGlowOverlay());
        this.applyCapesToCertainPlayers();
	}

	@Override
	public void registerPackets() {
		super.registerPackets();

		PacketRegistry.registerPacketHandler(ModContainerManPack.modid, "weather", new PacketWeather());
	}

	private void applyCapesToCertainPlayers() {
	    String capeURL = "http://i.imgur.com/4SpmGSv.png";
        String[] owners = {"sanandreasMC", "SilverChiren"};

        ThreadDownloadImageData image = new ThreadDownloadImageData(capeURL, null, null);

        for(String username : owners)
        {
            Minecraft.getMinecraft().renderEngine.loadTexture(new ResourceLocation("cloaks/" + username), image);
        }
	}
}
