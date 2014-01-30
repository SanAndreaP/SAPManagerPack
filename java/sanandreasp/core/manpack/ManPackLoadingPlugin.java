package sanandreasp.core.manpack;

import java.io.File;
import java.util.Map;

import sanandreasp.core.manpack.mod.ModContainerManPack;
import sanandreasp.core.manpack.transformer.ASMHelper;
import sanandreasp.core.manpack.transformer.TransformBadPotionsATN;
import sanandreasp.core.manpack.transformer.TransformELBAttackingPlayer;
import sanandreasp.core.manpack.transformer.TransformEntityThrowable;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"sanandreasp.core.manpack.transformer"})
public class ManPackLoadingPlugin implements IFMLLoadingPlugin {
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			TransformBadPotionsATN.class.getName(),
			TransformEntityThrowable.class.getName(),
			TransformELBAttackingPlayer.class.getName()
		};
	}

	@Override
	public String getModContainerClass() {
		return ModContainerManPack.class.getName();
	}

	@Override
	public String getSetupClass() {
		return ManPackSetupClass.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		ASMHelper.isMCP = !((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
		ModContainerManPack.modLocation = (File)data.get("coremodLocation");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isServer() {
		try {
			Class clazz = Class.forName("net.minecraft.client.Minecraft");
			if( clazz != null ) {
				clazz = null;
				return false;
			}
		} catch (ClassNotFoundException e) {
			return true;
		}
		return true;
	}

}
