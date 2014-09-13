/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public final class GuiUtils
{
    public static void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, float zLevel) {
        float f = (float)(color1 >> 24 & 255) / 255.0F;
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color1 & 255) / 255.0F;
        float f4 = (float)(color2 >> 24 & 255) / 255.0F;
        float f5 = (float)(color2 >> 16 & 255) / 255.0F;
        float f6 = (float)(color2 >> 8 & 255) / 255.0F;
        float f7 = (float)(color2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)x2, (double)y1, (double)zLevel);
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex((double)x1, (double)y1, (double)zLevel);
        tessellator.addVertex((double)x1, (double)y2, (double)zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex((double)x2, (double)y2, (double)zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
