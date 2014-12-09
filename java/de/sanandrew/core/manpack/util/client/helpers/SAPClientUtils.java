/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client.helpers;

import de.sanandrew.core.manpack.util.SAPReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;

import java.lang.reflect.InvocationTargetException;

public final class SAPClientUtils
{
    public static void setSelectedBtn(GuiScreen inst, GuiButton btn) {
        SAPReflectionHelper.setCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a", btn);
    }

    public static GuiButton getSelectedBtn(GuiScreen inst) {
        return SAPReflectionHelper.getCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a");
    }

    public static ModelRenderer createNewBox(ModelBase model, int texX, int texY, boolean mirror, float boxX, float boxY, float boxZ, int sizeX, int sizeY, int sizeZ,
                                             float rotPointX, float rotPointY, float rotPointZ, float rotX, float rotY, float rotZ) {
        return createNewBox(model, texX, texY, mirror, boxX, boxY, boxZ, sizeX, sizeY, sizeZ, 0.0F, rotPointX, rotPointY, rotPointZ, rotX, rotY, rotZ);
    }

    public static ModelRenderer createNewBox(ModelBase model, int texX, int texY, boolean mirror, float boxX, float boxY, float boxZ, int sizeX, int sizeY, int sizeZ,
                                             float scaleFactor, float rotPointX, float rotPointY, float rotPointZ, float rotX, float rotY, float rotZ) {
        ModelRenderer box = new ModelRenderer(model, texX, texY);
        box.addBox(boxX, boxY, boxZ, sizeX, sizeY, sizeZ, scaleFactor);
        box.setRotationPoint(rotPointX, rotPointY, rotPointZ);
        box.textureWidth = model.textureWidth;
        box.textureHeight = model.textureHeight;
        box.rotateAngleX = rotX;
        box.rotateAngleY = rotY;
        box.rotateAngleZ = rotZ;
        box.mirror = mirror;

        return box;
    }

    public static <T extends ModelRenderer> T createNewBox(Class<T> boxClass, ModelBase model, int texX, int texY, boolean mirror, float boxX, float boxY,
                                                           float boxZ, int sizeX, int sizeY, int sizeZ, float scaleFactor, float rotPointX, float rotPointY,
                                                           float rotPointZ, float rotX, float rotY, float rotZ) {
        try {
            T box = boxClass.getConstructor(ModelBase.class, int.class, int.class).newInstance(model, texX, texY);
            box.addBox(boxX, boxY, boxZ, sizeX, sizeY, sizeZ, scaleFactor);
            box.setRotationPoint(rotPointX, rotPointY, rotPointZ);
            box.textureWidth = model.textureWidth;
            box.textureHeight = model.textureHeight;
            box.rotateAngleX = rotX;
            box.rotateAngleY = rotY;
            box.rotateAngleZ = rotZ;
            box.mirror = mirror;

            return box;
        } catch( NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public static void drawTexturedSquareYPos(double cornerBeginX, double cornerBeginZ, double cornerEndX, double cornerEndZ, double y, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 1.0F, 0.0F);

        tess.addVertexWithUV(cornerBeginX, y, cornerBeginZ, uBegin, vBegin);
        tess.addVertexWithUV(cornerBeginX, y, cornerEndZ,   uBegin, vEnd);
        tess.addVertexWithUV(cornerEndX,   y, cornerEndZ,   uEnd,   vEnd);
        tess.addVertexWithUV(cornerEndX,   y, cornerBeginZ, uEnd,   vBegin);

        tess.draw();
    }

    public static void drawTexturedSquareYNeg(double cornerBeginX, double cornerBeginZ, double cornerEndX, double cornerEndZ, double y, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, -1.0F, 0.0F);

        tess.addVertexWithUV(cornerBeginX, y, cornerBeginZ, uBegin, vBegin);
        tess.addVertexWithUV(cornerEndX,   y, cornerBeginZ, uEnd,   vBegin);
        tess.addVertexWithUV(cornerEndX,   y, cornerEndZ,   uEnd,   vEnd);
        tess.addVertexWithUV(cornerBeginX, y, cornerEndZ,   uBegin, vEnd);

        tess.draw();
    }

    public static void drawTexturedSquareXPos(double cornerBeginY, double cornerBeginZ, double cornerEndY, double cornerEndZ, double x, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(1.0F, 0.0F, 0.0F);

        tess.addVertexWithUV(x, cornerBeginY, cornerBeginZ, uEnd,   vEnd);
        tess.addVertexWithUV(x, cornerEndY,   cornerBeginZ, uEnd,   vBegin);
        tess.addVertexWithUV(x, cornerEndY,   cornerEndZ,   uBegin, vBegin);
        tess.addVertexWithUV(x, cornerBeginY, cornerEndZ,   uBegin, vEnd);

        tess.draw();
    }

    public static void drawTexturedSquareXNeg(double cornerBeginY, double cornerBeginZ, double cornerEndY, double cornerEndZ, double x, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(-1.0F, 0.0F, 0.0F);

        tess.addVertexWithUV(x, cornerBeginY, cornerBeginZ, uBegin, vEnd);
        tess.addVertexWithUV(x, cornerBeginY, cornerEndZ,   uEnd,   vEnd);
        tess.addVertexWithUV(x, cornerEndY,   cornerEndZ,   uEnd,   vBegin);
        tess.addVertexWithUV(x, cornerEndY,   cornerBeginZ, uBegin, vBegin);
        tess.draw();
    }

    public static void drawTexturedSquareZPos(double cornerBeginX, double cornerBeginY, double cornerEndX, double cornerEndY, double z, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, 1.0F);

        tess.addVertexWithUV(cornerBeginX, cornerBeginY, z, uBegin, vEnd);
        tess.addVertexWithUV(cornerEndX,   cornerBeginY, z, uEnd,   vEnd);
        tess.addVertexWithUV(cornerEndX,   cornerEndY,   z, uEnd,   vBegin);
        tess.addVertexWithUV(cornerBeginX, cornerEndY,   z, uBegin, vBegin);

        tess.draw();
    }

    public static void drawTexturedSquareZNeg(double cornerBeginX, double cornerBeginY, double cornerEndX, double cornerEndY, double z, double uBegin, double vBegin,
                                              double uEnd, double vEnd) {
        Tessellator tess = Tessellator.instance;

        tess.startDrawingQuads();
        tess.setNormal(0.0F, 0.0F, -1.0F);

        tess.addVertexWithUV(cornerBeginX, cornerBeginY, z, uEnd,   vEnd);
        tess.addVertexWithUV(cornerBeginX, cornerEndY,   z, uEnd,   vBegin);
        tess.addVertexWithUV(cornerEndX,   cornerEndY,   z, uBegin, vBegin);
        tess.addVertexWithUV(cornerEndX,   cornerBeginY, z, uBegin, vEnd);
        tess.draw();
    }
}
