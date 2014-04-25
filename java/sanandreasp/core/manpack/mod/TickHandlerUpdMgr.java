package sanandreasp.core.manpack.mod;

import sanandreasp.core.manpack.managers.SAPUpdateManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class TickHandlerUpdMgr
{
    @SubscribeEvent
    public void onWorldTickStart(TickEvent.WorldTickEvent event) {
        for( SAPUpdateManager udm : SAPUpdateManager.updMgrs ) {
//            if( (!ManPackLoadingPlugin.isServer() && Minecraft.getMinecraft().thePlayer != null) || ManPackLoadingPlugin.isServer() ) {
                udm.checkForUpdate();
//            }
        }
    }
//	@Override
//	public void tickStart(EnumSet<TickType> type, Object... tickData) {
//		for( SAPUpdateManager udm : SAPUpdateManager.updMgrs ) {
//			if( (!ManPackLoadingPlugin.isServer() && Minecraft.getMinecraft().thePlayer != null)
//			    || ManPackLoadingPlugin.isServer() )
//			{
//				udm.checkForUpdate();
//			}
//		}
//	}
//
//	@Override
//	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }
//
//	@Override
//	public EnumSet<TickType> ticks() {
//		return EnumSet.of(TickType.SERVER);
//	}
//
//	@Override
//	public String getLabel() {
//		return "SAPManUdTicks";
//	}

}
