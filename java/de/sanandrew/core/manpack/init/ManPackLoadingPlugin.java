package de.sanandrew.core.manpack.init;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import de.sanandrew.core.manpack.transformer.*;

import java.util.Map;

@SortingIndex(1001)
@MCVersion("1.7.10")
@DependsOn("forge")
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
                         TransformHorseArmor.class.getName(),
                         TransformEnderman.class.getName()
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
        ASMHelper.isMCP = !(Boolean) data.get("runtimeDeobfuscationEnabled");
//        ModCntManPack.modLocation = (File) data.get("coremodLocation");
        ASMNames.initialize();
    }
}
