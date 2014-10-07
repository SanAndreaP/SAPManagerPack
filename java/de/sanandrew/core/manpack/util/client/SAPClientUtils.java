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
import net.minecraft.util.StatCollector;

public class SAPClientUtils
{
    public static void setSelectedBtn(GuiScreen inst, GuiButton btn) {
        SAPReflectionHelper.setCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a", btn);
    }

    public static GuiButton getSelectedBtn(GuiScreen inst) {
        return SAPReflectionHelper.getCachedFieldValue(GuiScreen.class, inst, "selectedButton", "field_73883_a");
    }

    public static String translate(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static String translatePostFormat(String key, Object... data) {
        return String.format(StatCollector.translateToLocal(key), data);
    }

    public static String translatePreFormat(String key, Object... data) {
        return StatCollector.translateToLocal(String.format(key, data));
    }
}
