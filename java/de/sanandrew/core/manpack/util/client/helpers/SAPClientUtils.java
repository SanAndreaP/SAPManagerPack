/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client.helpers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.mod.client.particle.SAPEffectRenderer;
import de.sanandrew.core.manpack.util.client.EntityParticle;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly( Side.CLIENT )
public final class SAPClientUtils
{
    /**
     * Registers a new FX Layer for my own EffectRenderer.
     *
     * @param resource The texture sheet the particles will use
     * @param hasAlpha If the particles have alpha
     * @return The new FX-Layer-ID. Use this as return value in your {@link EntityParticle#getFXLayer()} method!
     */
    public static int registerNewFXLayer(ResourceLocation resource, boolean hasAlpha) {
        return SAPEffectRenderer.INSTANCE.registerFxLayer(resource, hasAlpha);
    }

    public static void spawnParticle(EntityParticle particle) {
        SAPEffectRenderer.INSTANCE.addEffect(particle);
    }

    public static void drawTexturedSquareYPos(double cornerBeginX, double cornerBeginZ, double cornerEndX, double cornerEndZ, double y, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);

        tess.addVertexWithUV(cornerBeginX, y, cornerBeginZ, uBegin, vBegin);
        tess.addVertexWithUV(cornerBeginX, y, cornerEndZ, uBegin, vEnd);
        tess.addVertexWithUV(cornerEndX, y, cornerEndZ, uEnd, vEnd);
        tess.addVertexWithUV(cornerEndX, y, cornerBeginZ, uEnd, vBegin);

        tess.draw();
    }

    public static void drawTexturedSquareYNeg(double cornerBeginX, double cornerBeginZ, double cornerEndX, double cornerEndZ, double y, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);

        tess.addVertexWithUV(cornerBeginX, y, cornerBeginZ, uBegin, vBegin);
        tess.addVertexWithUV(cornerEndX, y, cornerBeginZ, uEnd, vBegin);
        tess.addVertexWithUV(cornerEndX, y, cornerEndZ, uEnd, vEnd);
        tess.addVertexWithUV(cornerBeginX, y, cornerEndZ, uBegin, vEnd);

        tess.draw();
    }

    public static void drawTexturedSquareXPos(double cornerBeginY, double cornerBeginZ, double cornerEndY, double cornerEndZ, double x, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);

        tess.addVertexWithUV(x, cornerBeginY, cornerBeginZ, uEnd, vEnd);
        tess.addVertexWithUV(x, cornerEndY, cornerBeginZ, uEnd, vBegin);
        tess.addVertexWithUV(x, cornerEndY, cornerEndZ, uBegin, vBegin);
        tess.addVertexWithUV(x, cornerBeginY, cornerEndZ, uBegin, vEnd);

        tess.draw();
    }

    public static void drawTexturedSquareXNeg(double cornerBeginY, double cornerBeginZ, double cornerEndY, double cornerEndZ, double x, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);

        tess.addVertexWithUV(x, cornerBeginY, cornerBeginZ, uBegin, vEnd);
        tess.addVertexWithUV(x, cornerBeginY, cornerEndZ, uEnd, vEnd);
        tess.addVertexWithUV(x, cornerEndY, cornerEndZ, uEnd, vBegin);
        tess.addVertexWithUV(x, cornerEndY, cornerBeginZ, uBegin, vBegin);
        tess.draw();
    }

    public static void drawTexturedSquareZPos(double cornerBeginX, double cornerBeginY, double cornerEndX, double cornerEndY, double z, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);

        tess.addVertexWithUV(cornerBeginX, cornerBeginY, z, uBegin, vEnd);
        tess.addVertexWithUV(cornerEndX, cornerBeginY, z, uEnd, vEnd);
        tess.addVertexWithUV(cornerEndX, cornerEndY, z, uEnd, vBegin);
        tess.addVertexWithUV(cornerBeginX, cornerEndY, z, uBegin, vBegin);

        tess.draw();
    }

    public static void drawTexturedSquareZNeg(double cornerBeginX, double cornerBeginY, double cornerEndX, double cornerEndY, double z, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);

        tess.addVertexWithUV(cornerBeginX, cornerBeginY, z, uEnd, vEnd);
        tess.addVertexWithUV(cornerBeginX, cornerEndY, z, uEnd, vBegin);
        tess.addVertexWithUV(cornerEndX, cornerEndY, z, uBegin, vBegin);
        tess.addVertexWithUV(cornerEndX, cornerBeginY, z, uBegin, vEnd);
        tess.draw();
    }

    public static void drawSquareXNeg(double cornerBeginY, double cornerBeginZ, double cornerEndY, double cornerEndZ, double x) {
        Tessellator tess = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        tess.addVertex(x, cornerBeginY, cornerBeginZ);
        tess.addVertex(x, cornerBeginY, cornerEndZ);
        tess.addVertex(x, cornerEndY, cornerEndZ);
        tess.addVertex(x, cornerEndY, cornerBeginZ);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawSquareZPos(double cornerBeginX, double cornerBeginY, double cornerEndX, double cornerEndY, double z) {
        Tessellator tess = Tessellator.instance;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.addVertex(cornerBeginX, cornerBeginY, z);
        tess.addVertex(cornerEndX, cornerBeginY, z);
        tess.addVertex(cornerEndX, cornerEndY, z);
        tess.addVertex(cornerBeginX, cornerEndY, z);
        tess.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Deprecated! Use {@link de.sanandrew.core.manpack.util.client.helpers.ModelBoxBuilder} instead!
     *
     * Use
     *   SAPClientUtils\.createNewBox\(this, (-?\d+), (-?\d+), (true|false), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+), (-?\d+), (-?\d+), (.+?), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD]), (-?\d+\.?\d*[FD])\);
     * to replace it with
     *   ModelBoxBuilder.newBuilder(this).setTexture($1, $2, $3).setLocation($11, $12, $13).setRotation($14, $15, $16).getBox($4, $5, $6, $7, $8, $9, $10);
     */
    @Deprecated
    public static ModelRenderer createNewBox(ModelBase model, int texX, int texY, boolean mirror, float boxX, float boxY, float boxZ, int sizeX, int sizeY, int sizeZ,
                                             float scaleFactor, float rotPointX, float rotPointY, float rotPointZ, float rotX, float rotY, float rotZ) {
        return null;
    }
}
