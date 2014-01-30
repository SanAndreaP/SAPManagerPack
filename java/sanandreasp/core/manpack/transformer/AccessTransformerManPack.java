package sanandreasp.core.manpack.transformer;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class AccessTransformerManPack extends AccessTransformer {
    @SuppressWarnings("unused")
	private static AccessTransformerManPack instance;

	public AccessTransformerManPack() throws IOException {
		super("sapmanpack_at.cfg");
        AccessTransformerManPack.instance = this;
	}

}
