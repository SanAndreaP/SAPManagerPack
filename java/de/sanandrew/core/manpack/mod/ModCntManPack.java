package de.sanandrew.core.manpack.mod;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import de.sanandrew.core.manpack.mod.packet.ChannelHandler;

public class ModCntManPack extends DummyModContainer
{
	public static final String MOD_VERSION = "1.6.4-2.0.0";
	public static final String MOD_CHANNEL = "sapmanpack";
	public static final String MOD_ID = "sapmanpack";
	public static final String MOD_LOG = "SAPManPack";

	public static ChannelHandler MANPACK_CH_HANDLER;

	public static File modLocation;
    private ModMetadata md;

	@SidedProxy(clientSide="de.sanandrew.core.manpack.mod.client.ClientProxy", serverSide="de.sanandrew.core.manpack.mod.CommonProxy")
	public static CommonProxy proxy;

	public ModCntManPack() {
        super(new ModMetadata());
        this.md = super.getMetadata();
        File mcmod = new File(ModCntManPack.modLocation, "mcmod.info");
        MetadataCollection mc = null;
        try( FileInputStream fis = new FileInputStream(mcmod) ) {
            mc = MetadataCollection.from(fis, ModCntManPack.modLocation.getName());
            System.out.println(String.format("Found an mcmod.info file in directory %s", ModCntManPack.modLocation.getName()));
        } catch( Throwable e1 ) {
            System.out.println(String.format("No mcmod.info file found in directory %s", ModCntManPack.modLocation.getName()));
        }

        if( mc != null ) {
        	this.md = mc.getMetadataForId(ModCntManPack.MOD_ID, null);
        } else {
        	this.md.authorList = Arrays.asList(new String[] { "SanAndreasP" });
        	this.md.description = "A helper coremod which is needed for all my mods.";
        	this.md.modId = ModCntManPack.MOD_ID;
        	this.md.version = ModCntManPack.MOD_VERSION;
        	this.md.name = "SanAndreasPs Manager Pack CORE edition";
        	this.md.url = "http://www.minecraftforge.net/forum/index.php/topic,2828.0.html";
        }
        MANPACK_CH_HANDLER = new ChannelHandler(ModCntManPack.MOD_ID, ModCntManPack.MOD_CHANNEL);
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent evt) {
		ModCntManPack.proxy.registerPackets();
//		MANPACK_CH_HANDLER.registerPacket(ExamplePacket.class);
	}

	@Subscribe
	public void init(FMLInitializationEvent evt) {
	    MANPACK_CH_HANDLER.initialise();
	    FMLCommonHandler.instance().bus().register(new TickEventUpdMgr());
		ModCntManPack.proxy.registerRenderStuff();
	}

	@Subscribe
    public void postInit(FMLPostInitializationEvent evt) {
        MANPACK_CH_HANDLER.postInitialise();
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
        return this.md != null ? this.getModId() : "Container ("+ModCntManPack.MOD_ID+") @" + System.identityHashCode(this);
    }
}
