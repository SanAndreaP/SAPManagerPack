/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.mod.client.gui.GuiModUpdate;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.KeyBinding;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

@SideOnly( Side.CLIENT )
public class KeyHandler
{
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if( Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ) {
            while( Keyboard.next() ) {
                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if( Keyboard.getEventKeyState() ) {
                    KeyBinding.onTick(Keyboard.getEventKey());

                    if( ClientProxy.KEY_UPDATE_GUI.isPressed() ) {
                        Minecraft.getMinecraft().displayGuiScreen(new GuiModUpdate(Minecraft.getMinecraft().currentScreen));
                    }
                }
            }
        }
    }
}
