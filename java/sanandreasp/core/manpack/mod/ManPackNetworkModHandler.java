package sanandreasp.core.manpack.mod;

import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkModHandler;

public class ManPackNetworkModHandler extends NetworkModHandler {

	public ManPackNetworkModHandler(ModContainerManPack container) {
		super(container, container.getClass().getAnnotation(NetworkMod.class));
		configureNetworkMod(container);
	}

}
