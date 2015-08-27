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
import de.sanandrew.core.manpack.managers.UpdateDownloader.EnumDlState;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import de.sanandrew.core.manpack.util.javatuples.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@SideOnly( Side.CLIENT )
public class GuiModUpdate
        extends GuiScreen
        implements GuiYesNoCallback
{
    private static final List<SAPUpdateManager> MANAGERS = new ArrayList<>(5);
    private static final List<Pair<GuiButtonUpdate, GuiButtonDetails>> SLOT_BUTTONS = new ArrayList<>(10);
    private static final ResourceLocation TEXTURE = new ResourceLocation(ManPackLoadingPlugin.MOD_ID, "textures/gui/updater/updater.png");

    public static void addManager(SAPUpdateManager mgr) {
        MANAGERS.add(mgr);

        int slotId = MANAGERS.indexOf(mgr);
        SLOT_BUTTONS.add(Pair.with(new GuiButtonUpdate(MANAGERS.size() * 2, slotId, "Update"), new GuiButtonDetails(MANAGERS.size() * 2 + 1, slotId, "Details")));
    }

    private GuiButton restartMC;
    private GuiButton back2Menu;
    private GuiButton updateAll;
    private final GuiScreen mainMenu;
    private int selectedItem;

    private GuiModUpdate.GuiModSlots modList;

    public GuiModUpdate(GuiScreen menu) {
        this.mainMenu = menu;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public void initGui() {
        super.initGui();

        this.selectedItem = -1;

        for( Pair<GuiButtonUpdate, GuiButtonDetails> slotBtns : SLOT_BUTTONS ) {
            this.buttonList.add(slotBtns.getValue0());
            this.buttonList.add(slotBtns.getValue1());
        }

        this.buttonList.add(this.restartMC = new GuiButton(this.buttonList.size(), (this.width - 300) / 2, this.height - 52, 150, 20, "Restart Minecraft"));
        this.buttonList.add(this.back2Menu = new GuiButton(this.buttonList.size(), (this.width - 300) / 2, this.height - 32, 150, 20, "Back to main menu"));
        this.buttonList.add(this.updateAll = new GuiButton(this.buttonList.size(), (this.width) / 2, this.height - 52, 150, 20, "Update all"));

        this.modList = new GuiModSlots();
        this.modList.registerScrollButtons(this.buttonList.size(), this.buttonList.size() + 1);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        this.modList.drawScreen(mouseX, mouseY, partTicks);

        this.restartMC.drawButton(this.mc, mouseX, mouseY);
        this.back2Menu.drawButton(this.mc, mouseX, mouseY);
        this.updateAll.drawButton(this.mc, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if( button == this.restartMC ) {
            GuiYesNo confirmGui = new GuiYesNo(this, "you really wanna restart MC?", "Yes?", 0);
            this.mc.displayGuiScreen(confirmGui);
        } else if( button == this.back2Menu ) {
            this.mc.displayGuiScreen(this.mainMenu);
        } else if( button instanceof GuiButtonUpdate ) {
            MANAGERS.get(((GuiButtonUpdate) button).slot).runUpdate();
        } else if( button instanceof GuiButtonDetails ) {
            this.mc.displayGuiScreen(new GuiUpdateDetails(this, MANAGERS.get(((GuiButtonDetails) button).slot)));
        } else if( button == this.updateAll ) {
            this.updateAll.enabled = false;
            new AllUpdater().run();
        } else {
            this.modList.actionPerformed(button);
        }
    }

    @Override
    public void confirmClicked(boolean isConfirmed, int guiId) {
        if( isConfirmed && guiId == 0 ) {
            try {
                SAPUtils.restartApp();
            } catch( RejectedExecutionException e ) {
                e.printStackTrace();
                this.mc.displayGuiScreen(this.mainMenu);
            }
            this.selectedItem = -1;
        }

        super.confirmClicked(isConfirmed, guiId);
    }

    static boolean checkIfMgrCanUpdate(SAPUpdateManager mgr) {
        return mgr.getUpdateInfo().getDownload() != null && mgr.getModJar() != null && mgr.getModJar().getName().endsWith(".jar")
                && (mgr.downloader == null || mgr.downloader.getStatus() == EnumDlState.ERROR);
    }

    class GuiModSlots
            extends GuiSlot
    {
        public GuiModSlots() {
            super(GuiModUpdate.this.mc, GuiModUpdate.this.width, GuiModUpdate.this.height, 32, GuiModUpdate.this.height - 64, 36);
        }

        @Override
        protected int getSize() {
            return GuiModUpdate.MANAGERS.size();
        }

        @Override
        protected void elementClicked(int elemIndex, boolean doubleClicked, int mouseX, int mouseY) {
            GuiModUpdate.this.selectedItem = elemIndex;
        }

        @Override
        protected boolean isSelected(int slotIndex) {
            return GuiModUpdate.this.selectedItem == slotIndex;
        }

        @Override
        protected void drawBackground() {
            GuiModUpdate.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int slotIndex, int xPos, int yPos, int yMin, Tessellator tessellator, int mouseX, int mouseY) {
            SAPUpdateManager mgr = GuiModUpdate.MANAGERS.get(slotIndex);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GuiModUpdate.this.mc.renderEngine.bindTexture(TEXTURE);
            if( this.isSelected(slotIndex) ) {
                GuiModUpdate.this.drawTexturedModalRect(xPos - 2, yPos - 2, 0, 0, this.getListWidth(), this.slotHeight);
            }

            if( mgr.downloader != null ) {
                String result = "Begin downloading...";
                int progBarClr = 0;
                int progBarLength = 0;

                if( mgr.downloader.getStatus() == EnumDlState.DOWNLOADING && mgr.downloader.getProgress() >= 0.0F ) {
                    result = String.format("Download Update: %s%s%%", EnumChatFormatting.WHITE, new DecimalFormat("0.00").format(mgr.downloader.getProgress()));
                    progBarClr = 0;
                    progBarLength = (int) (140.0F * mgr.downloader.getProgress() / 100.0F);
                } else if( mgr.downloader.getStatus() == EnumDlState.ERROR ) {
                    result = String.format("Download Update: %s%s", EnumChatFormatting.RED, "Failed!");
                    progBarClr = 2;
                    progBarLength = 140;
                } else if( mgr.downloader.getStatus() == EnumDlState.COMPLETE ) {
                    result = String.format("Download Update: %s%s", EnumChatFormatting.GREEN, "Complete!");
                    progBarClr = 1;
                    progBarLength = 140;
                }

                GuiModUpdate.this.drawTexturedModalRect(xPos + 4, yPos + 23, 0, 36, 140, 5);
                GuiModUpdate.this.drawTexturedModalRect(xPos + 4, yPos + 23, 0, 41 + progBarClr * 5, progBarLength, 5);

                GuiModUpdate.this.fontRendererObj.drawStringWithShadow(result, xPos, yPos + 10, 0xFF808080);
            } else {
                GuiModUpdate.this.fontRendererObj.drawStringWithShadow("Installed: " + mgr.getVersion(), xPos, yPos + 10, 0xFF808080);
                GuiModUpdate.this.fontRendererObj.drawStringWithShadow("Latest: " + mgr.getVersionDiffSeverity().format + mgr.getUpdateInfo().version, xPos, yPos + 20, 0xFF808080);
            }

            GuiModUpdate.this.fontRendererObj.drawStringWithShadow(mgr.getModName(), xPos, yPos, 0xFFFFFFFF);

            GuiButtonUpdate btnUpdate = SLOT_BUTTONS.get(slotIndex).getValue0();
            GuiButtonDetails btnDetails = SLOT_BUTTONS.get(slotIndex).getValue1();

            btnUpdate.xPosition = xPos + 151;
            btnUpdate.yPosition = yPos;
            if( btnUpdate.yPosition + 15 > this.top && btnUpdate.yPosition < this.bottom ) {
                btnUpdate.enabled = checkIfMgrCanUpdate(mgr);
                btnUpdate.drawButton(GuiModUpdate.this.mc, mouseX, mouseY);
            } else {
                btnUpdate.enabled = false;
            }

            btnDetails.xPosition = xPos + 151;
            btnDetails.yPosition = yPos + 17;
            if( btnDetails.yPosition + 15 > this.top && btnDetails.yPosition < this.bottom ) {
                btnDetails.enabled = true;
                btnDetails.drawButton(GuiModUpdate.this.mc, mouseX, mouseY);
            } else {
                btnDetails.enabled = false;
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    static class GuiButtonSlot
            extends GuiButton
    {
        public final int slot;

        public GuiButtonSlot(int id, int slotId, String name) {
            super(id, 0, 0, 65, 15, name);

            this.slot = slotId;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if( this.visible ) {
                FontRenderer fontrenderer = mc.fontRenderer;

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int hoverState = this.getHoverState(this.field_146123_n);

                // frame
                Gui.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width - 1, this.yPosition + 1,
                             hoverState == 2 ? 0xFFFFFF80 : hoverState == 1 ? 0xFFFFFFFF : 0x80808080
                            );
                Gui.drawRect(this.xPosition + width - 1, this.yPosition, this.xPosition + this.width, this.yPosition + this.height - 1,
                             hoverState == 2 ? 0xFF606030 : hoverState == 1 ? 0xFF606060 : 0x80303030
                            );
                Gui.drawRect(this.xPosition + 1, this.yPosition + height - 1, this.xPosition + this.width, this.yPosition + this.height,
                             hoverState == 2 ? 0xFF606030 : hoverState == 1 ? 0xFF606060 : 0x80303030
                            );
                Gui.drawRect(this.xPosition, this.yPosition + 1, this.xPosition + 1, this.yPosition + this.height,
                             hoverState == 2 ? 0xFFFFFF80 : hoverState == 1 ? 0xFFFFFFFF : 0x80808080
                            );

                // background
//                GuiModUpdate.drawGlossEffect(this.xPosition + 45, this.yPosition + 1, this.yPosition + this.height - 1, 1.0F, 2);
//                GuiModUpdate.drawGlossEffect(this.xPosition + 49, this.yPosition + 1, this.yPosition + this.height - 1, 1.0F, 1);
                Gui.drawRect(this.xPosition + 1, this.yPosition + 1, this.xPosition + this.width - 1, this.yPosition + this.height - 1,
                             hoverState == 2 ? 0x80404020 : hoverState == 1 ? 0x80404040 : 0x80000000
                            );

                this.mouseDragged(mc, mouseX, mouseY);
                int l = 0xE0E0E0;

                if( !this.enabled ) {
                    l = 0x808080;
                } else if( this.field_146123_n ) {
                    l = 0xFFFFA0;
                }

                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
            }
        }
    }

    static class GuiButtonUpdate
            extends GuiButtonSlot
    {
        GuiButtonUpdate(int id, int slotId, String name) {
            super(id, slotId, name);
        }
    }

    static class GuiButtonDetails
            extends GuiButtonSlot
    {
        GuiButtonDetails(int id, int slotId, String name) {
            super(id, slotId, name);
        }
    }

    static class AllUpdater
            implements Runnable
    {
        static int currMgrIndex = 0;

        @Override
        public void run() {
            if( currMgrIndex >= MANAGERS.size() ) {
                return;
            }

            SAPUpdateManager mgr = MANAGERS.get(currMgrIndex);
            if( checkIfMgrCanUpdate(mgr) ) {
                mgr.runUpdate();
                mgr.downloader.setSucceedRunnable(new AllUpdater());
            }
            currMgrIndex++;
        }
    }
}
