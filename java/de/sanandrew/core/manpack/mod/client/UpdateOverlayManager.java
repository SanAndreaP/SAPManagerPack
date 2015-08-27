/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.mod.client.gui.GuiModUpdate;
import de.sanandrew.core.manpack.util.MutableString;
import de.sanandrew.core.manpack.util.SAPReflectionHelper;
import de.sanandrew.core.manpack.util.javatuples.Pair;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly( Side.CLIENT )
public class UpdateOverlayManager
        extends Gui
{
    private boolean hasEverythingChecked = false;

    private ResourceLocation frameTex = null;
    private FontRenderer fontRenderer = null;
    private Minecraft mc = null;
    private Timer timer = new Timer(1);

    private int counter = 0;

    private float scale = 0.0F;
    private float txtFade = 1.0F;

    private final List<Pair<SAPUpdateManager, String>> updateMods = new ArrayList<>();
    private int currMod = 0;

    @SubscribeEvent
    public void onStitchPost(TextureStitchEvent.Post event) {
        if( event.map.getTextureType() == 0 ) {
            String str = SAPReflectionHelper.invokeCachedMethod(Block.class, Blocks.cobblestone, "getTextureName", "func_149641_N", null, null);
            TextureAtlasSprite sprite = event.map.getTextureExtry(str);
            ResourceLocation resLoc = new ResourceLocation(sprite.getIconName());
            this.frameTex = new ResourceLocation(resLoc.getResourceDomain(), "textures/blocks/" + resLoc.getResourcePath() + ".png");
        }
    }

    @SubscribeEvent
    public void renderMinecraft(TickEvent.RenderTickEvent event) {
        if( event.phase == Phase.END ) {
            if( !this.hasEverythingChecked ) {
                for( Triplet<SAPUpdateManager, MutableBoolean, MutableString> udm : SAPUpdateManager.UPD_MANAGERS ) {
                    if( udm.getValue1().booleanValue() ) {
                        if( udm.getValue2() != null && udm.getValue2().length() > 0 ) {
                            this.addUpdate(udm.getValue0(), udm.getValue2().stringValue());
                            GuiModUpdate.addManager(udm.getValue0());
                        }
                        SAPUpdateManager.setInRenderQueue(udm.getValue0().getId());
                    }
                }

                if( !SAPUpdateManager.IS_IN_RENDER_QUEUE.containsValue(false) ) {
                    this.showUpdates();
                    this.hasEverythingChecked = true;
                }
            }
        }
        this.renderInfobox();
        if( !(this.mc.currentScreen instanceof GuiMainMenu) ) {
            this.hideUpdates();
        }
    }

    public void renderInfobox() {
        if( this.mc == null || this.fontRenderer == null ) {
            this.mc = Minecraft.getMinecraft();
            this.fontRenderer = new FontRenderer(this.mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.mc.renderEngine, true);
            this.timer.elapsedTicks = -1;
        }

        if( this.counter == 0 && this.scale <= 0.00F ) {
            return;
        }

        this.fontRenderer.setBidiFlag(this.mc.fontRenderer.getBidiFlag());
        String update = this.updateMods.get(this.currMod).getValue0().getModName() + " update out!";
        String version = "new version: " + this.updateMods.get(this.currMod).getValue1();
        String details = "press \247f[" + Keyboard.getKeyName(ClientProxy.KEY_UPDATE_GUI.getKeyCode()) + "]\247r for details";

        this.mc.renderEngine.bindTexture(this.frameTex);
        int width = Math.max(this.fontRenderer.getStringWidth(update), Math.max(this.fontRenderer.getStringWidth(version),
                                                                                this.fontRenderer.getStringWidth(details)
                                                                               )
                            ) + 12;
        int height = 45;

        GL11.glPushMatrix();
        GL11.glScalef(this.scale, this.scale, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F / 256.0F * 16, 1.0F / 256.0F * 16, 1.0F);
        this.drawTexturedModalRect(0, 0, 0, 0, width * 16, 4 * 16);
        this.drawTexturedModalRect(0, 4 * 16, 0, 4 * 16, 4 * 16, (height - 8) * 16);
        this.drawTexturedModalRect(0, (height - 4) * 16, 0, (height - 4) * 16, width * 16, 4 * 16);
        this.drawTexturedModalRect((width - 4) * 16, 4 * 16, 0, 4 * 16, 4 * 16, (height - 8) * 16);
        GL11.glPopMatrix();
        drawRect(1, 1, width - 1, height - 1, 0x80000000);
        GL11.glPopMatrix();

        if( this.scale >= 1.00F && this.counter > 0 ) {
            if( !this.mc.isGamePaused() ) {
                this.timer.updateTimer();
            }

            int alpha = ((int) (Math.abs(this.txtFade) * 255.0F) & 255) << 24;
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if( alpha != 0 ) {
                this.drawCenteredString(this.fontRenderer, update, width / 2, 5, 0xFFFF00 | alpha);
                this.drawCenteredString(this.fontRenderer, version, width / 2, 12, 0xA0A0A0 | alpha);
            }
            GL11.glDisable(GL11.GL_BLEND);
            this.drawCenteredString(this.fontRenderer, details, width / 2, 30, 0xA0A0A0);

            this.drawCenteredString(this.fontRenderer, Integer.toString(this.counter), width / 2, height - 4, 0xFFFFFF);
        } else if( this.counter > 0 ) {
            this.scale += 0.1F;
        } else if( this.scale > 0.00F ) {
            this.scale -= 0.1F;
        }

        if( Math.abs(this.txtFade) < 0.01F ) {
            this.currMod++;
            this.txtFade -= 0.1F;
        } else if( Math.abs(this.txtFade) < 1.00F ) {
            this.txtFade -= 0.1F;
        } else if( this.txtFade <= -1.00F ) {
            this.txtFade = 1.00F;
        }

        if( this.timer.elapsedTicks > 0 && this.counter > 0 ) {
            this.counter--;
            if( this.counter > 0 && this.counter % 5 == 0 ) {
                this.txtFade -= 0.1F;
            }
            this.timer.elapsedTicks = 0;
        }
    }

    public void addUpdate(SAPUpdateManager mgr, String version) {
        this.updateMods.add(Pair.with(mgr, version));
    }

    public void showUpdates() {
        this.currMod = 0;
        this.counter = (5 * this.updateMods.size());
    }

    public void hideUpdates() {
        this.counter = 0;
    }
}
