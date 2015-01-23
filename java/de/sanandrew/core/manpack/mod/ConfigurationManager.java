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
    private static final String CFG_VERSION = "1.0";

    private static Configuration config;

    public static void load(File file) {
        config = new Configuration(file, CFG_VERSION);

        config.load();

        config.save();
    }
}
