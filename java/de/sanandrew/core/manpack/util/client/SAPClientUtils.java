/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.client;

import de.sanandrew.core.manpack.util.SAPReflectionHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class SAPClientUtils
{
    public static void setSelectedBtn(GuiScreen inst, GuiButton btn) {
        SAPReflectionHelper.setCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a", btn);
    }

    public static GuiButton getSelectedBtn(GuiScreen inst) {
        return SAPReflectionHelper.getCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a");
    }

    public static ModelRenderer createNewBox(ModelBase model, int texX, int texY, boolean mirror, float boxX, float boxY, float boxZ, int sizeX, int sizeY, int sizeZ,
                                             float rotPointX, float rotPointY, float rotPointZ, float rotX, float rotY, float rotZ) {
        ModelRenderer box = new ModelRenderer(model, texX, texY);
        box.addBox(boxX, boxY, boxZ, sizeX, sizeY, sizeZ);
        box.setRotationPoint(rotPointX, rotPointY, rotPointZ);
        box.rotateAngleX = rotX;
        box.rotateAngleY = rotY;
        box.rotateAngleZ = rotZ;
        box.mirror = mirror;

        return box;
    }
}
