package sanandreasp.core.manpack;

import java.io.File;
import java.util.Map;

import net.minecraft.launchwrapper.LaunchClassLoader;
import sanandreasp.core.manpack.transformer.ASMHelper;
import cpw.mods.fml.relauncher.IFMLCallHook;

public class ManPackSetupClass implements IFMLCallHook {

    private LaunchClassLoader cl;

	@Override
	public Void call() throws Exception {
        return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		cl = (LaunchClassLoader) data.get("classLoader");
		ASMHelper.setup((File)data.get("mcLocation"), cl, (String) data.get("deobfuscationFileName"));
	}

}
