package sanandreasp.core.manpack.mod;

import java.util.Arrays;
import java.util.logging.Level;

import sanandreasp.core.manpack.managers.TickHandlerUpdMgr;
import sanandreasp.core.manpack.mod.client.ClientPacketHandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkMod.VersionCheckHandler;
import cpw.mods.fml.common.network.NetworkModHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@NetworkMod(clientSideRequired=true, serverSideRequired=false,
	clientPacketHandlerSpec = @SidedPacketHandler(channels={ModContainerManPack.channel}, packetHandler=ClientPacketHandler.class))
public class ModContainerManPack extends DummyModContainer {
	public static final String version = "1.9.9";
	public static final String channel = "SAPManPack";
	
	@SidedProxy(clientSide="sanandreasp.core.manpack.mod.client.ClientProxy", serverSide="sanandreasp.core.manpack.mod.CommonProxy")
	public static CommonProxy proxy;
	
	public ModContainerManPack() {
        super(new ModMetadata());
//        /* ModMetadata is the same as mcmod.info */
        ModMetadata myMeta = super.getMetadata();
        myMeta.authorList = Arrays.asList(new String[] { "SanAndreasP" });
        myMeta.description = "A helper coremod which is needed for all my mods.";
        myMeta.modId = "SAPManPackCore";
        myMeta.version = this.version;
        myMeta.name = "SanAndreasPs Manager Pack CORE edition";
        myMeta.url = "http://www.minecraftforge.net/forum/index.php/topic,2828.0.html";
	}
	
	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent evt) {
		FMLLog.makeLog("SAP-ManagerPack");
		FMLLog.makeLog("SAP-ConfigManager");
		FMLLog.makeLog("SAP-LanguageManager");
		FMLLog.makeLog("SAP-UpdateManager");
	}
	
	@Subscribe
	public void init(FMLInitializationEvent evt) {
		TickRegistry.registerTickHandler(new TickHandlerUpdMgr(), Side.SERVER);
		TickRegistry.registerScheduledTickHandler(new SchedTickHandlerWld(), Side.SERVER);
		
		this.proxy.registerRenderStuff();
	}
	
	@Subscribe
	public void playerJoin(FMLServerAboutToStartEvent evt) {
		SchedTickHandlerWld.prevThunderState = false;
	}
	
	@Subscribe
	public void modConstruction(FMLConstructionEvent evt)
	{
		try
		{
			FMLNetworkHandler.instance().registerNetworkMod(new ManPackNetworkModHandler(this));
			FMLLog.info("Succeeded registering SAPManPack Packet Handler");
		}
		catch (Exception e)
		{
			FMLLog.log(Level.SEVERE, e, "Failed to register packet handler for SAPManPack");
		}
	}
}
