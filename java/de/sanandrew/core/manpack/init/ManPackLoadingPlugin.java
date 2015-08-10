/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.init;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.transformer.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

@SortingIndex(1001)
@MCVersion("1.7.10")
@DependsOn("forge")
@TransformerExclusions({ "de.sanandrew.core.manpack.transformer", "de.sanandrew.core.manpack.init" })
public class ManPackLoadingPlugin
        implements IFMLLoadingPlugin
{
    public static File source;

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                         TransformBadPotionsATN.class.getName(),
                         TransformEntityThrowable.class.getName(),
                         TransformELBAttackingPlayer.class.getName(),
                         TransformPlayerDismountCtrl.class.getName(),
                         TransformHorseArmor.class.getName(),
                         TransformEnderman.class.getName(),
                         TransformEntityCollision.class.getName()
               };
    }

    @Override
    public String getModContainerClass() {
        return ModCntManPack.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    /**
     * Code from diesieben07 in here.
     * Thanks ~SanAndreasP
     */
    @Override
    public void injectData(Map<String, Object> data) {
        ASMHelper.isMCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");
        source = (File) data.get("coremodLocation");
        if (source == null) { // this is usually in a dev env
            try {
                source = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed to acquire source location for SAPManPack!", e);
            }
        }
        ASMNames.initialize();

        ASMNameHelper.getInstance().readMcpSrgFile();
    }
}
