/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class GuiUtils
{
    private static RenderItem itemRenderer = new RenderItem();

    public static void drawGradientRect(int x1, int y1, int x2, int y2, int color1, int color2, float zLevel) {
        float a1 = (color1 >> 24 & 255) / 255.0F;
        float r1 = (color1 >> 16 & 255) / 255.0F;
        float g1 = (color1 >> 8 & 255) / 255.0F;
        float b1 = (color1 & 255) / 255.0F;
        float a2 = (color2 >> 24 & 255) / 255.0F;
        float r2 = (color2 >> 16 & 255) / 255.0F;
        float g2 = (color2 >> 8 & 255) / 255.0F;
        float b2 = (color2 & 255) / 255.0F;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r2, g2, b2, a2);
        tessellator.addVertex(x2, y1, zLevel);
        tessellator.setColorRGBA_F(r1, g1, b1, a1);
        tessellator.addVertex(x1, y1, zLevel);
        tessellator.addVertex(x1, y2, zLevel);
        tessellator.setColorRGBA_F(r2, g2, b2, a2);
        tessellator.addVertex(x2, y2, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawOutlinedString(FontRenderer renderer, String s, int x, int y, int foreColor, int frameColor) {
        if( renderer.getUnicodeFlag() ) {
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            renderer.drawString(s, x, y, frameColor);
            GL11.glTranslatef(0.0F, -1.0F, 0.0F);
            renderer.drawString(s, x, y, frameColor);
            GL11.glTranslatef(0.5F, 0.5F, 0.0F);
            renderer.drawString(s, x, y, frameColor);
            GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
            renderer.drawString(s, x, y, frameColor);
            GL11.glTranslatef(0.5F, 0.0F, 0.0F);
        } else {
            renderer.drawString(s, x - 1, y, frameColor);
            renderer.drawString(s, x + 1, y, frameColor);
            renderer.drawString(s, x, y - 1, frameColor);
            renderer.drawString(s, x, y + 1, frameColor);
        }

        renderer.drawString(s, x, y, foreColor);
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;

        if( k == 0 ) {
            k = 1000;
        }

        while( scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240 ) {
            ++scaleFactor;
        }

        GL11.glScissor(x * scaleFactor, mc.displayHeight - (y + height) * scaleFactor, width * scaleFactor, height * scaleFactor);
    }

    public static void drawGuiIcon(IIcon icon, int posX, int posY) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
        itemRenderer.zLevel = 200.0F;

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        ResourceLocation resourcelocation = Minecraft.getMinecraft().getTextureManager().getResourceLocation(1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);

        if( icon == null ) {
            icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        itemRenderer.renderIcon(posX, posY, icon, 16, 16);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);

        itemRenderer.zLevel = 0.0F;
        GL11.glPopMatrix();
    }

    public static boolean isMouseInRect(int mouseX, int mouseY, int rectX, int rectY, int width, int height) {
        return mouseX >= rectX && mouseX < rectX + width && mouseY >= rectY && mouseY < rectY + height;
    }
}
