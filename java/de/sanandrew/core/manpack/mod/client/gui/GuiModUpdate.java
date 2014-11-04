package de.sanandrew.core.manpack.mod.client.gui;

import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.Tessellator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiModUpdate
    extends GuiScreen
    implements GuiYesNoCallback
{
    private GuiButton restartMC;
    private GuiButton back2Menu;
    private final GuiScreen mainMenu;

    public static final List<SAPUpdateManager> MANAGERS = new ArrayList<>();
    private GuiModUpdate.GuiModSlots modList;

    public GuiModUpdate(GuiScreen menu) {
        this.mainMenu = menu;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initGui() {
        super.initGui();

        this.buttonList.add(this.restartMC = new GuiButton(this.buttonList.size(), (this.width - 200) / 2, this.height - 52, "Restart Minecraft"));
        this.buttonList.add(this.back2Menu = new GuiButton(this.buttonList.size(), (this.width - 200) / 2, this.height - 32, "Back to main menu"));

        this.modList = new GuiModSlots();
        this.modList.registerScrollButtons(this.buttonList.size(), this.buttonList.size() + 1);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partTicks) {
        this.modList.drawScreen(mouseX, mouseY, partTicks);
        super.drawScreen(mouseX, mouseY, partTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if( button == this.restartMC ) {
            GuiYesNo confirmGui = new GuiYesNo(this, "you really wanna restart MC?", "Yes?", 0);
            this.mc.displayGuiScreen(confirmGui);
        } else if( button == this.back2Menu ) {
            this.mc.displayGuiScreen(this.mainMenu);
        } else {
            this.modList.actionPerformed(button);
        }
    }

    @Override
    public void confirmClicked(boolean isConfirmed, int guiId) {
        if( isConfirmed && guiId == 0 ) {
            try {
                SAPUtils.restartApp();
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }

        super.confirmClicked(isConfirmed, guiId);
    }

    class GuiModSlots extends GuiSlot
    {
        public GuiModSlots() {
            super(GuiModUpdate.this.mc, GuiModUpdate.this.width, GuiModUpdate.this.height, 32, GuiModUpdate.this.height - 64, 36);
        }

        @Override
        protected int getSize() {
            return GuiModUpdate.MANAGERS.size();
        }

        @Override
        protected void elementClicked(int var1, boolean var2, int var3, int var4) {
            // TODO Auto-generated method stub

        }

        @Override
        protected boolean isSelected(int var1) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        protected void drawBackground() {
            GuiModUpdate.this.drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5, int var6, int var7) {
            // TODO Auto-generated method stub

        }

    }
}
