/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.gui;

import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiUpdateDetails
        extends GuiScreen
{
    private final GuiScreen updateList;
    private final SAPUpdateManager manager;

    private GuiButton backToList;

    private float scrollAmount = 0;
    private float scrollMax = 0;

    public GuiUpdateDetails(GuiScreen updateListGui, SAPUpdateManager mgr) {
        this.updateList = updateListGui;
        this.manager = mgr;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();

        this.buttonList.add(this.backToList = new GuiButton(this.buttonList.size(), (this.width - 200) / 2, (this.height - 240) / 2 + 215, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        this.drawDefaultBackground();

        int xPos = (this.width - 256) / 2;
        int yPos = (this.height - 240) / 2;


        //FIXME: make this its own method!
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        Gui.drawRect(xPos, yPos, xPos + 256, yPos + 240, 0xFF000000);


        GL11.glPushMatrix();
        GL11.glScalef(1.5F, 1.5F, 1.0F);
        GL11.glTranslatef(xPos / 1.5F - xPos, yPos / 1.5F - yPos, 0.0F);
        this.fontRendererObj.drawString(this.manager.getModName(), xPos + 4, yPos + 4, 0xFFFFFFFF);
        GL11.glPopMatrix();

        this.fontRendererObj.drawString("Currently installed version", xPos + 6, yPos + 22, 0xFFA0A0A0);
        Gui.drawRect(xPos + this.fontRendererObj.getStringWidth("Currently installed version") + 8, yPos + 26, xPos + 150, yPos + 27, 0xFF606060);
        this.fontRendererObj.drawString("Updated version", xPos + 6, yPos + 32, 0xFFA0A0A0);
        Gui.drawRect(xPos + this.fontRendererObj.getStringWidth("Updated version") + 8, yPos + 36, xPos + 150, yPos + 37, 0xFF606060);

        this.fontRendererObj.drawString(this.manager.getVersion().toString(), xPos + 152, yPos + 22, 0xFFFFFFFF);
        this.fontRendererObj.drawString(this.manager.getUpdateInfo().version, xPos + 152, yPos + 32, 0xFFFFFFFF);

        this.fontRendererObj.drawString("Details and Changelog:", xPos + 6, yPos + 48, 0xFFA0A0A0);

        int scissorX = 6;
        int scissorY = 60;
        int scissorW = 244;
        int scissorH = 150;

        GL11.glScissor((xPos + scissorX) * scaleFactor, mc.displayHeight - (yPos + scissorH + scissorY) * scaleFactor, scissorW * scaleFactor, scissorH * scaleFactor);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        Gui.drawRect(xPos + 6, yPos + 60, xPos + 250, yPos + 210, 0xFFFFFFFF);
        Gui.drawRect(xPos + 246, yPos + 60, xPos + 250, yPos + 210, 0xFFC0A0FF);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -this.scrollAmount, 0.0F);
        int shiftY = 5;
        String s = this.manager.getUpdateInfo().description;
        this.fontRendererObj.drawSplitString(s, xPos + 10, yPos + 62 + shiftY, 230, 0xFFA00060);
        shiftY += this.fontRendererObj.splitStringWidth(s, 230) + 4;
        if( this.manager.getUpdateInfo().changelog != null ) {
            for( int i = 0; i < this.manager.getUpdateInfo().changelog.length; i++ ) {
                s = this.manager.getUpdateInfo().changelog[i];
                Gui.drawRect(xPos + 10, yPos + 64 + shiftY, xPos + 14, yPos + 68 + shiftY, i % 2 == 0 ? 0xFF000000 : 0xFF606060);
                this.fontRendererObj.drawSplitString(s, xPos + 20, yPos + 62 + shiftY, 228, i % 2 == 0 ? 0xFF000000 : 0xFF606060);
                shiftY += this.fontRendererObj.splitStringWidth(s, 228) + 2;
            }
        }

        this.scrollMax = shiftY + 3 - 146;
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        int scrollPos = (int) (scrollAmount / scrollMax * 140.0F);
        Gui.drawRect(xPos + 246, yPos + 60 + scrollPos, xPos + 250, yPos + 70 + scrollPos, 0xFF8000FF);

        super.drawScreen(mouseX, mouseY, partTicks);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        int mWheelDir = Mouse.getEventDWheel();

        if( mWheelDir != 0 && this.scrollMax > 0 ) {
            if( mWheelDir > 0 ) {
                this.scrollAmount = Math.max(0, this.scrollAmount - 5);
            } else {
                this.scrollAmount = Math.min(this.scrollMax, this.scrollAmount + 5);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if( button == this.backToList ) {
            this.mc.displayGuiScreen(this.updateList);
        }
    }
}
