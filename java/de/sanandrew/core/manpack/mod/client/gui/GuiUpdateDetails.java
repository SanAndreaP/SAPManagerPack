/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.util.client.helpers.GuiUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

@SideOnly( Side.CLIENT )
public class GuiUpdateDetails
        extends GuiScreen
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(ManPackLoadingPlugin.MOD_ID, "textures/gui/updater/details_list.png");

    private final GuiScreen updateList;
    private final SAPUpdateManager manager;

    private GuiButton backToList;

    private float scrollAmount = 0;
    private float scrollMax = 0;
    private boolean isScrolling = false;

    public GuiUpdateDetails(GuiScreen updateListGui, SAPUpdateManager mgr) {
        this.updateList = updateListGui;
        this.manager = mgr;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void initGui() {
        super.initGui();

        this.buttonList.add(this.backToList = new GuiButton(this.buttonList.size(), (this.width - 200) / 2, (this.height - 240) / 2 + 215, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        int xPos = (this.width - 276) / 2;
        int yPos = (this.height - 240) / 2;
        int listX = 16;
        int listY = 60;
        int listWidth = 244;
        int listHeight = 150;
        int listTextY = 5;
        String s;
        boolean isLeftMBDown = Mouse.isButtonDown(0);

        int scrollX = xPos + listX + listWidth - 4;
        int scrollY = yPos + listY;
        if( !this.isScrolling && isLeftMBDown && mouseX >= scrollX && mouseY >= scrollY && mouseX < scrollX + 4 && mouseY < scrollY + listHeight ) {
            this.isScrolling = this.scrollMax > 0.0F;
        }

        if( !isLeftMBDown ) {
            this.isScrolling = false;
        }

        if( this.isScrolling ) {
            this.scrollAmount = Math.min(this.scrollMax, Math.max(0.0F, (mouseY - scrollY - 5) / (listHeight - 10.0F)) * scrollMax);
        }

        this.drawDefaultBackground();

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        Gui.drawRect(xPos, 0, xPos + 276, this.height, 0x80000000);
        GuiUtils.drawGradientRect(xPos, 0, xPos + 5, this.height, 0xFF000000, 0x00000000, this.zLevel);
        GuiUtils.drawGradientRect(xPos + 271, 0, xPos + 276, this.height, 0x00000000, 0xFF000000, this.zLevel);

        GL11.glPushMatrix();
        GL11.glTranslatef(16.0F, 4.0F, 0.0F);
        GL11.glScalef(1.5F, 1.5F, 1.0F);
        GL11.glTranslatef(xPos / 1.5F - xPos, yPos / 1.5F - yPos, 0.0F);
        this.fontRendererObj.drawString(this.manager.getModName(), xPos, yPos, 0xFFFFFFFF);
        GL11.glPopMatrix();

        s = "Currently installed version";
        this.fontRendererObj.drawString(s, xPos + 16, yPos + 22, 0xFFA0A0A0);
        Gui.drawRect(xPos + this.fontRendererObj.getStringWidth(s) + 18, yPos + 26, xPos + 160, yPos + 27, 0xFF606060);
        s = "Updated version";
        this.fontRendererObj.drawString(s, xPos + 16, yPos + 32, 0xFFA0A0A0);
        Gui.drawRect(xPos + this.fontRendererObj.getStringWidth(s) + 18, yPos + 36, xPos + 160, yPos + 37, 0xFF606060);

        this.fontRendererObj.drawString(this.manager.getVersion().toString(), xPos + 162, yPos + 22, 0xFFFFFFFF);
        this.fontRendererObj.drawString(this.manager.getUpdateInfo().version, xPos + 162, yPos + 32, 0xFFFFFFFF);

        this.fontRendererObj.drawString("Details and Changelog:", xPos + 16, yPos + 48, 0xFFA0A0A0);

        this.mc.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(xPos + listX - 1, yPos + listY - 1, 0, 0, listWidth + 2, listHeight + 2);

        GuiUtils.doGlScissor(xPos + listX, yPos + listY, listWidth, listHeight);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -this.scrollAmount, 0.0F);

        s = this.manager.getUpdateInfo().description;
        this.fontRendererObj.drawSplitString(s, xPos + listX + 4, yPos + listY + listTextY + 2, listWidth - 12, 0xFFA00060);
        listTextY += this.fontRendererObj.splitStringWidth(s, listWidth - 12) + 4;

        if( this.manager.getUpdateInfo().changelog != null ) {
            for( int i = 0; i < this.manager.getUpdateInfo().changelog.length; i++ ) {
                s = this.manager.getUpdateInfo().changelog[i];
                Gui.drawRect(xPos + listX + 4, yPos + listY + listTextY + 4, xPos + listX + 8, yPos + listY + listTextY + 8, i % 2 == 0 ? 0xFF000000 : 0xFF606060);
                this.fontRendererObj.drawSplitString(s, xPos + listX + 14, yPos + listY + listTextY + 2, listWidth - 22, i % 2 == 0 ? 0xFF000000 : 0xFF606060);
                listTextY += this.fontRendererObj.splitStringWidth(s, listWidth - 22) + 2;
            }
        }

        this.scrollMax = listTextY - listHeight + 4;

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        this.mc.renderEngine.bindTexture(TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int scrollPos = (int) (this.scrollAmount / this.scrollMax * (listHeight - 10.0F));
        this.drawTexturedModalRect(xPos + listX + listWidth - 4, yPos + listY + scrollPos, listWidth + 2, 0, 4, 10);

        GL11.glDisable(GL11.GL_BLEND);
        super.drawScreen(mouseX, mouseY, partTicks);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        int mWheelDir = Mouse.getEventDWheel();

        if( mWheelDir != 0 && this.scrollMax > 0.0F ) {
            if( mWheelDir > 0 ) {
                this.scrollAmount = Math.max(0.0F, this.scrollAmount - 5.0F);
            } else {
                this.scrollAmount = Math.min(this.scrollMax, this.scrollAmount + 5.0F);
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
