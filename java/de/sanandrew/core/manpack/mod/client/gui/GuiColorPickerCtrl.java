/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.gui;

import de.sanandrew.core.manpack.util.client.GuiUtils;
import net.minecraft.client.gui.Gui;

public class GuiColorPickerCtrl
    extends Gui
{
    public int hue = 0; // from0 to 90
    public int sat = 90; // from 0 to 90;
    public int bright = 90; // from 0 to 90;
    private int resColorHued = 0xFFFF0000;
    private int resColor = 0xFFFFFFFF;

    public int xPos = 0;
    public int yPos = 0;

    public GuiColorPickerCtrl(int posX, int posY) {
        this.xPos = posX;
        this.yPos = posY;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseKey) {
        mouseX -= this.xPos;
        mouseY -= this.yPos;

        if( mouseKey == 0 ) {
            if( mouseX >= 105 && mouseX < 115 && mouseY >= 0 && mouseY < 90 ) {
                this.hue = mouseY;
                this.calcResultColor();
            } else if( mouseX >= 0 && mouseX < 90 && mouseY >= 0 && mouseY < 90 ) {
                this.sat = mouseX;
                this.bright = mouseY;
                this.calcResultColor();
            }
        }
    }

    public int getOutputColor() {
        return this.resColor;
    }

    public void drawControl() {
//        GL11.glTranslatef(0F, 90F + xPos*2, 0F);
//        GL11.glRotatef(-90F, 0F, 0F, 1F);
        GuiUtils.drawGradientRect(xPos, yPos, 90 + xPos, 90 + yPos, 0xFFFFFFFF, this.resColorHued, this.zLevel);
//        drawGradientRect(xPos, yPos, 90 + xPos, 90 + yPos, 0xFFFFFFFF, this.resColorHued);
//        GL11.glRotatef(90F, 0F, 0F, 1F);
//        GL11.glTranslatef(0F, -90F - xPos*2, 0F);
        drawGradientRect(xPos, yPos, 90 + xPos, 90 + yPos, 0x0, 0xFF000000);

        int x1 = 105 + xPos;
        int x2 = 115 + xPos;

        drawGradientRect(x1,      yPos, x2, 15 + yPos, 0xFFFF0000, 0xFFFF00FF);
        drawGradientRect(x1, 15 + yPos, x2, 30 + yPos, 0xFFFF00FF, 0xFF0000FF);
        drawGradientRect(x1, 30 + yPos, x2, 45 + yPos, 0xFF0000FF, 0xFF00FFFF);
        drawGradientRect(x1, 45 + yPos, x2, 60 + yPos, 0xFF00FFFF, 0xFF00FF00);
        drawGradientRect(x1, 60 + yPos, x2, 75 + yPos, 0xFF00FF00, 0xFFFFFF00);
        drawGradientRect(x1, 75 + yPos, x2, 90 + yPos, 0xFFFFFF00, 0xFFFF0000);

        drawRect(x1, this.hue + yPos, x2, this.hue + yPos + 1, 0xFF000000);

        drawRect(this.sat + xPos - 4, this.bright + yPos - 4, this.sat + xPos + 4, this.bright + yPos + 4, 0xFF000000);
        drawRect(this.sat + xPos - 3, this.bright + yPos - 3, this.sat + xPos + 3, this.bright + yPos + 3, 0xFFFFFFFF);

        drawRect(this.sat + xPos - 2, this.bright + yPos - 2, this.sat + xPos + 2, this.bright + yPos + 2, resColor);
    }

    private void calcResultColor() {
        int pickedColor = 0xFF000000;

        if( this.hue >= 0 && this.hue < 15 ) {
            pickedColor = 0xFFFF0000 | ((int) (0xFF * this.hue / 15.0F));
        } else if( this.hue >= 15 && this.hue < 30 ) {
            pickedColor = 0xFF0000FF | ((int) (0xFF - 0xFF * (this.hue - 15) / 15.0F) << 16);
        } else if( this.hue >= 30 && this.hue < 45 ) {
            pickedColor = 0xFF0000FF | ((int) (0xFF * (this.hue - 30) / 15.0F) << 8);
        } else if( this.hue >= 45 && this.hue < 60 ) {
            pickedColor = 0xFF00FF00 | ((int) (0xFF - 0xFF * (this.hue - 45) / 15.0F));
        } else if( this.hue >= 60 && this.hue < 75 ) {
            pickedColor = 0xFF00FF00 | ((int) (0xFF * (this.hue - 60) / 15.0F) << 16);
        } else if( this.hue >= 75 && this.hue < 90 ) {
            pickedColor = 0xFFFF0000 | ((int) (0xFF - 0xFF * (this.hue - 75) / 15.0F) << 8);
        }

        float satFloat = 1.0F - this.sat / 89.0F;
        int redPart = (pickedColor >> 16) & 0xFF;
        int redSat = (int) ((0xFF - redPart) * satFloat);
        redPart += redSat;

        int greenPart = (pickedColor >> 8) & 0xFF;
        int greenSat = (int) ((0xFF - greenPart) * satFloat);
        greenPart += greenSat;

        int bluePart = pickedColor & 0xFF;
        int blueSat = (int) ((0xFF - bluePart) * satFloat);
        bluePart += blueSat;

        int brightColor = redPart << 16 | greenPart << 8 | bluePart;
            float brightFloat = 1.0F - this.bright / 89.0F;
            brightColor = 0xFF000000
                    | (int) (((brightColor >> 16) & 0xFF) * brightFloat) << 16
                    | (int) (((brightColor >> 8) & 0xFF) * brightFloat) << 8
                    | (int) ((brightColor & 0xFF) * brightFloat);

        this.resColor = brightColor;
        this.resColorHued = pickedColor;
    }
}
