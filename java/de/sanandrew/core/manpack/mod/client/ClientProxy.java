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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.mod.CommonProxy;
import de.sanandrew.core.manpack.mod.client.event.EventWorldRenderLast;
import de.sanandrew.core.manpack.mod.client.event.RenderPlayerEventHandler;
import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import de.sanandrew.core.manpack.network.ClientPacketHandler;
import de.sanandrew.core.manpack.network.NetworkManager;
import de.sanandrew.core.manpack.network.PacketProcessor;
import de.sanandrew.core.manpack.util.client.RenderBlockGlowOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

@SideOnly( Side.CLIENT )
public class ClientProxy
        extends CommonProxy
{
    private static final UpdateOverlayManager UPDATE_OVERLY_MGR = new UpdateOverlayManager();
    public static final KeyBinding KEY_UPDATE_GUI = new KeyBinding("key.sapmanpack.updateKey", Keyboard.KEY_U, "key.categories.sapmanpack");

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
    public void registerPacketHandler(String modId, String modChannel, PacketProcessor packetProcessor) {
        super.registerPacketHandler(modId, modChannel, packetProcessor);
        NetworkManager.getPacketChannel(modId).register(new ClientPacketHandler(modId, modChannel));
    }
}
