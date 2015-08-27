/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod.client.gui;

import com.google.common.primitives.Floats;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import de.sanandrew.core.manpack.util.client.helpers.GuiUtils;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;

@SideOnly( Side.CLIENT )
public class GuiColorPickerCtrl
        extends Gui
{
    private int hue = 360;      // from 360 to 0
    private int sat = 100;      // from 0 to 100
    private int bright = 100;   // from 0 to 100

    private int resColorHued = 0xFFFF0000;
    private int resColor = 0xFFFFFFFF;

    public int xPos = 0;
    public int yPos = 0;

    public GuiColorPickerCtrl(int posX, int posY) {
        this.xPos = posX;
        this.yPos = posY;
        this.calcResultColor();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseKey) {
        mouseX -= this.xPos;
        mouseY -= this.yPos;

        if( mouseKey == 0 ) {
            if( mouseX >= 105 && mouseX < 115 && mouseY >= 0 && mouseY < 90 ) {
                this.hue = 359 - mouseY * 4;
                this.calcResultColor();
            } else if( mouseX >= 0 && mouseX < 90 && mouseY >= 0 && mouseY < 90 ) {
                this.sat = mouseX * 100 / 90;
                this.bright = 100 - mouseY * 100 / 90;
                this.calcResultColor();
            }
        }
    }

    public int getOutputColor() {
        return this.resColor;
    }

    public void setHsb(int hue, int saturation, int brightness) {
        this.hue = hue;
        this.sat = saturation;
        this.bright = brightness;

        this.calcResultColor();
    }

    public int getHue() {
        return this.hue;
    }

    public int getSaturation() {
        return this.sat;
    }

    public int getBrightness() {
        return this.bright;
    }

    @SuppressWarnings( "FloatingPointEquality" )
    public void setHsbFromRgb(int rgb) {
        float[] splitColors = ArrayUtils.remove(SAPUtils.getRgbaFromColorInt(rgb).getColorFloatArray(), 3);  // don't need the alpha value (3)

        float max = Floats.max(splitColors);
        float min = Floats.min(splitColors);

        if( max == min ) {
            this.hue = 0;
        } else if( max == splitColors[0] ) {
            this.hue = Math.round(60.0F * ((splitColors[1] - splitColors[2]) / (max - min)));
        } else if( max == splitColors[1] ) {
            this.hue = Math.round(60.0F * (2.0F + (splitColors[2] - splitColors[0]) / (max - min)));
        } else if( max == splitColors[2] ) {
            this.hue = Math.round(60.0F * (4.0F + (splitColors[0] - splitColors[1]) / (max - min)));
        }

        if( this.hue < 0 ) {
            this.hue += 360;
        }

        if( max <= 0.01F ) {
            this.sat = 0;
        } else {
            this.sat = Math.round((max - min) / max * 100.0F);
        }

        this.bright = Math.round(max * 100.0F);

        this.calcResultColor();
    }

    public void drawControl() {
        GuiUtils.drawGradientRect(xPos, yPos, 90 + xPos, 90 + yPos, 0xFFFFFFFF, this.resColorHued, this.zLevel);
        drawGradientRect(xPos, yPos, 90 + xPos, 90 + yPos, 0x0, 0xFF000000);

        int x1 = 105 + xPos;
        int x2 = 115 + xPos;

        drawGradientRect(x1, yPos, x2, 15 + yPos, 0xFFFF0000, 0xFFFF00FF);
        drawGradientRect(x1, 15 + yPos, x2, 30 + yPos, 0xFFFF00FF, 0xFF0000FF);
        drawGradientRect(x1, 30 + yPos, x2, 45 + yPos, 0xFF0000FF, 0xFF00FFFF);
        drawGradientRect(x1, 45 + yPos, x2, 60 + yPos, 0xFF00FFFF, 0xFF00FF00);
        drawGradientRect(x1, 60 + yPos, x2, 75 + yPos, 0xFF00FF00, 0xFFFFFF00);
        drawGradientRect(x1, 75 + yPos, x2, 90 + yPos, 0xFFFFFF00, 0xFFFF0000);

        drawRect(x1, this.getHueForGUI() + yPos, x2, this.getHueForGUI() + yPos + 1, 0xFF000000);

        drawRect(this.getSatForGUI() + xPos - 4, this.getBrightForGUI() + yPos - 4, this.getSatForGUI() + xPos + 4, this.getBrightForGUI() + yPos + 4, 0xFF000000);
        drawRect(this.getSatForGUI() + xPos - 3, this.getBrightForGUI() + yPos - 3, this.getSatForGUI() + xPos + 3, this.getBrightForGUI() + yPos + 3, 0xFFFFFFFF);

        drawRect(this.getSatForGUI() + xPos - 2, this.getBrightForGUI() + yPos - 2, this.getSatForGUI() + xPos + 2, this.getBrightForGUI() + yPos + 2, resColor);
    }

    private int getHueForGUI() {
        return (359 - this.hue) / 4;
    }

    private int getSatForGUI() {
        return this.sat * 90 / 100;
    }

    private int getBrightForGUI() {
        return 90 - this.bright * 90 / 100;
    }

    private void calcResultColor() {
        int pickedColor = 0xFF000000;

        int invHue = 360 - this.hue;

        if( invHue >= 0 && invHue < 60 ) {
            pickedColor = 0xFFFF0000 | ((int) (0xFF * invHue / 60.0F));
        } else if( invHue >= 60 && invHue < 120 ) {
            pickedColor = 0xFF0000FF | ((int) (0xFF - 0xFF * (invHue - 60) / 60.0F) << 16);
        } else if( invHue >= 120 && invHue < 180 ) {
            pickedColor = 0xFF0000FF | ((int) (0xFF * (invHue - 120) / 60.0F) << 8);
        } else if( invHue >= 180 && invHue < 240 ) {
            pickedColor = 0xFF00FF00 | ((int) (0xFF - 0xFF * (invHue - 180) / 60.0F));
        } else if( invHue >= 240 && invHue < 300 ) {
            pickedColor = 0xFF00FF00 | ((int) (0xFF * (invHue - 240) / 15.0F) << 16);
        } else if( invHue >= 300 && invHue < 360 ) {
            pickedColor = 0xFFFF0000 | ((int) (0xFF - 0xFF * (invHue - 300) / 60.0F) << 8);
        }

        float satFloat = 1.0F - this.sat / 100.0F;
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
        float brightFloat = this.bright / 100.0F;
        brightColor = 0xFF000000
                | (int) (((brightColor >> 16) & 0xFF) * brightFloat) << 16
                | (int) (((brightColor >> 8) & 0xFF) * brightFloat) << 8
                | (int) ((brightColor & 0xFF) * brightFloat);

        this.resColor = brightColor;
        this.resColorHued = pickedColor;
    }
}
