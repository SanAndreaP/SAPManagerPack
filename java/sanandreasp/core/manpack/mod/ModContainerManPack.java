package sanandreasp.core.manpack.mod;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.logging.Level;

import sanandreasp.core.manpack.mod.client.packet.ClientPHandler;
import sanandreasp.core.manpack.mod.packet.CommonPHandler;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@NetworkMod(clientSideRequired=true, serverSideRequired=false,
	clientPacketHandlerSpec = @SidedPacketHandler(channels={ModContainerManPack.channel}, packetHandler=ClientPHandler.class),
	serverPacketHandlerSpec = @SidedPacketHandler(channels={ModContainerManPack.channel}, packetHandler=CommonPHandler.class))
public class ModContainerManPack extends DummyModContainer
{
	public static final String version = "1.6.4-2.0.0";
	public static final String channel = "sapmanpack";
	public static final String modid = "sapmanpack";

	public static File modLocation;
    private ModMetadata md;

	@SidedProxy(clientSide="sanandreasp.core.manpack.mod.client.ClientProxy", serverSide="sanandreasp.core.manpack.mod.CommonProxy")
	public static CommonProxy proxy;

	public ModContainerManPack() {
        super(new ModMetadata());
        this.md = super.getMetadata();
        File mcmod = new File(ModContainerManPack.modLocation, "mcmod.info");
        MetadataCollection mc = null;
        try( FileInputStream fis = new FileInputStream(mcmod) ) {
            mc = MetadataCollection.from(fis, ModContainerManPack.modLocation.getName());
            System.out.println(String.format("Found an mcmod.info file in directory %s", ModContainerManPack.modLocation.getName()));
        } catch( Throwable e1 ) {
            System.out.println(String.format("No mcmod.info file found in directory %s", ModContainerManPack.modLocation.getName()));
        }

        if( mc != null ) {
        	this.md = mc.getMetadataForId(ModContainerManPack.modid, null);
        } else {
        	this.md.authorList = Arrays.asList(new String[] { "SanAndreasP" });
        	this.md.description = "A helper coremod which is needed for all my mods.";
        	this.md.modId = ModContainerManPack.modid;
        	this.md.version = ModContainerManPack.version;
        	this.md.name = "SanAndreasPs Manager Pack CORE edition";
        	this.md.url = "http://www.minecraftforge.net/forum/index.php/topic,2828.0.html";
        }
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

		ModContainerManPack.proxy.registerPackets();
	}

	@Subscribe
	public void init(FMLInitializationEvent evt) {
		TickRegistry.registerTickHandler(new TickHandlerUpdMgr(), Side.SERVER);
		TickRegistry.registerScheduledTickHandler(new SchedTickHandlerWld(), Side.SERVER);

		ModContainerManPack.proxy.registerRenderStuff();
	}

	@Subscribe
	public void playerJoin(FMLServerAboutToStartEvent evt) {
		SchedTickHandlerWld.prevThunderState = false;
	}

	@Subscribe
	public void modConstruction(FMLConstructionEvent evt) {
		try {
			FMLNetworkHandler.instance().registerNetworkMod(new ManPackNetworkModHandler(this));
			FMLLog.info("Succeeded registering SAPManPack Packet Handler");
		} catch( Throwable e ) {
			FMLLog.log(Level.SEVERE, e, "Failed to register packet handler for SAPManPack");
		}
	}

    @Override
    public ModMetadata getMetadata() {
        return this.md;
    }

    @Override
    public String getModId() {
        return this.md.modId;
    }

    @Override
    public String getName() {
        return this.md.name;
    }

    @Override
    public String getVersion() {
        return this.md.version;
    }

    @Override
    public String getDisplayVersion() {
        return this.md.version;
    }

    @Override
    public String toString() {
        return this.md != null ? this.getModId() : "Dummy Container ("+ModContainerManPack.modid+") @" + System.identityHashCode(this);
    }
}
