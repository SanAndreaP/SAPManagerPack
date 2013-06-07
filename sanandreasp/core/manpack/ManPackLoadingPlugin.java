package sanandreasp.core.manpack;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"sanandreasp.core"})
@MCVersion("1.5.2")
public class ManPackLoadingPlugin implements IFMLLoadingPlugin {

	public ManPackLoadingPlugin() {
	}

	@Override
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			"sanandreasp.core.manpack.transformer.TransformFOVMultiplier",
			"sanandreasp.core.manpack.transformer.TransformBadPotionsATN",
			"sanandreasp.core.manpack.transformer.TransformMiscStuff"
		};
	}

	@Override
	public String getModContainerClass() {
		return "sanandreasp.core.manpack.mod.ModContainerManPack";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}
	
	public static boolean isServer() {
		try {
			Class clazz = Class.forName("net.minecraft.client.Minecraft");
			if(clazz != null) {
				clazz = null;
				return false;
			}
		} catch (ClassNotFoundException e) {
			return true;
		}
		return true;
	}

}
