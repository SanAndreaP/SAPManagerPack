package de.sanandrew.core.manpack.init;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.transformer.ASMHelper;
import de.sanandrew.core.manpack.transformer.ASMNames;
import de.sanandrew.core.manpack.transformer.TransformBadPotionsATN;
import de.sanandrew.core.manpack.transformer.TransformELBAttackingPlayer;
import de.sanandrew.core.manpack.transformer.TransformEntityThrowable;
import de.sanandrew.core.manpack.transformer.TransformHorseArmor;
import de.sanandrew.core.manpack.transformer.TransformPlayerDismountCtrl;

@SortingIndex(1001)
@MCVersion("1.7.2")
@TransformerExclusions({ "de.sanandrew.core.manpack.transformer", "de.sanandrew.core.manpack.init" })
public class ManPackLoadingPlugin
    implements IFMLLoadingPlugin
{
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
                         TransformHorseArmor.class.getName()
               };
    }

    @Override
    public String getModContainerClass() {
        return ModCntManPack.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;//ManPackSetupClass.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        ASMHelper.isMCP = !((Boolean) data.get("runtimeDeobfuscationEnabled")).booleanValue();
        ModCntManPack.modLocation = (File) data.get("coremodLocation");
        ASMNames.initialize();
    }
}
