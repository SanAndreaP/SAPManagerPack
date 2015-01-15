/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.mod.CommonProxy;
import de.sanandrew.core.manpack.mod.client.event.EventWorldRenderLast;
import de.sanandrew.core.manpack.mod.client.event.RenderPlayerEventHandler;
import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import de.sanandrew.core.manpack.util.client.RenderBlockGlowOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public class ClientProxy
        extends CommonProxy
{
    private static final UpdateOverlayManager UPDATE_OVERLY_MGR = new UpdateOverlayManager();
    public static final KeyBinding KEY_UPDATE_GUI = new KeyBinding("key.sapmp.updateKey", Keyboard.KEY_U, "key.categories.misc");

	@Override
    public void registerRenderStuff() {
        RenderBlockGlowOverlay.renderID = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(RenderBlockGlowOverlay.renderID, new RenderBlockGlowOverlay());

        MinecraftForge.EVENT_BUS.register(UPDATE_OVERLY_MGR);
        FMLCommonHandler.instance().bus().register(UPDATE_OVERLY_MGR);

        ClientRegistry.registerKeyBinding(KEY_UPDATE_GUI);
        KeyHandler kHandler = new KeyHandler();
        FMLCommonHandler.instance().bus().register(kHandler);
        MinecraftForge.EVENT_BUS.register(kHandler);

        SAPEffectRenderer.initialize(Minecraft.getMinecraft().getTextureManager());

        EventWorldRenderLast worldRenderLastEventHandler = new EventWorldRenderLast();
        MinecraftForge.EVENT_BUS.register(worldRenderLastEventHandler);
        FMLCommonHandler.instance().bus().register(worldRenderLastEventHandler);

        MinecraftForge.EVENT_BUS.register(new RenderPlayerEventHandler());
	}

	@Override
	public void updOverlayAddUpdMgr(SAPUpdateManager mgr, String version) {
	    UPDATE_OVERLY_MGR.addUpdate(mgr, version);
	}

	@Override
	public void registerPackets() {
		super.registerPackets();
	}
}
