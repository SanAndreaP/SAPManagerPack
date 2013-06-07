package sanandreasp.core.manpack.transformer;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class AccessTransformerManPack extends AccessTransformer {
    private static AccessTransformerManPack instance;

	public AccessTransformerManPack() throws IOException {
		super("sapmanpack_at.cfg");
        instance = this;
	}

}
