/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationManager
{
    private static final String CFG_VERSION = "1.1";

    private static Configuration config;

    public static boolean subscribeToUnstable = false;
    public static boolean enableUpdater = true;
    public static boolean enableWindowTitleMsg = true;

    public static void load(File file) {
        config = new Configuration(file, CFG_VERSION);

        config.load();

        subscribeToUnstable = config.getBoolean("subscribeToUnstable", "updater", subscribeToUnstable, DESC_SUBUNSTABLE);
        enableUpdater = config.getBoolean("enableUpdater", "updater", enableUpdater, DESC_ENABLEUPDMGR);
        enableWindowTitleMsg = config.getBoolean("enableWindowTitleMsg", Configuration.CATEGORY_GENERAL, enableWindowTitleMsg, DESC_ENABLEWNDTITLE);

        config.save();
    }

    private static final String DESC_SUBUNSTABLE = "If set to true, the update manager checks for alpha/beta/release candidate updates of mods.\n" +
            "Note: installed mods within one of the before mentioned states\nreceive unstable updates " +
            "regardless of this setting!";
    public static final String DESC_ENABLEUPDMGR = "This will enable (true) or disable (false) the update checker.";
    public static final String DESC_ENABLEWNDTITLE = "This will enable (true) or disable (false) the window title easter egg.";
}
