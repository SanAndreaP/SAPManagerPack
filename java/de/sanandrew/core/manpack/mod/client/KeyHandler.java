package de.sanandrew.core.manpack.mod.client;

import de.sanandrew.core.manpack.mod.client.gui.GuiModUpdate;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class KeyHandler
{
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if( Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ) {
            while( Keyboard.next() ) {
                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if( Keyboard.getEventKeyState() ) {
                    KeyBinding.onTick(Keyboard.getEventKey());
                }

                if( Keyboard.getEventKeyState() ) {
                    if( ClientProxy.KEY_UPDATE_GUI.isPressed() ) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiModUpdate(Minecraft.getMinecraft().currentScreen));
                    }
                }
            }
        }
    }
}
