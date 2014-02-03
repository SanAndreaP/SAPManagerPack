package sanandreasp.core.manpack.managers;

import java.util.EnumSet;
import sanandreasp.core.manpack.ManPackLoadingPlugin;
import net.minecraft.client.Minecraft;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandlerUpdMgr implements ITickHandler
{
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		for( SAPUpdateManager udm : SAPUpdateManager.updMgrs ) {
			if( (!ManPackLoadingPlugin.isServer() && Minecraft.getMinecraft().thePlayer != null)
			    || ManPackLoadingPlugin.isServer() )
			{
				udm.checkForUpdate();
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "SAPManUdTicks";
	}

}
