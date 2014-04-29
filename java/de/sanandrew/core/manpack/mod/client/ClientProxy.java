package de.sanandrew.core.manpack.mod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import de.sanandrew.core.manpack.mod.CommonProxy;
import de.sanandrew.core.manpack.util.client.RenderBlockGlowOverlay;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
    public void registerRenderStuff() {
        RenderBlockGlowOverlay.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderBlockGlowOverlay.renderID, new RenderBlockGlowOverlay());
        this.applyCapesToCertainPlayers();
        MinecraftForge.EVENT_BUS.register(new RenderUpdateOverlay());
	}

	@Override
	public void registerPackets() {
		super.registerPackets();
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
