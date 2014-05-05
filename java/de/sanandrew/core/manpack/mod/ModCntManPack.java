package de.sanandrew.core.manpack.mod;

import java.util.Arrays;

import org.apache.logging.log4j.Level;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.mod.packet.ChannelHandler;
import de.sanandrew.core.manpack.util.javatuples.Triplet;

public class ModCntManPack
    extends DummyModContainer
{
    public static final String MOD_CHANNEL = "sapmanpack";
    public static final String MOD_ID = "sapmanpack";
    public static final String MOD_LOG = "SAPManPack";
    public static final String UPD_LOG = "SAPUpdateMgr";

    public static final String MOD_VERSION = "1.7.2-2.0.0";

    // Annotation does not work in a productive MC environment (see below)
    //@SidedProxy(clientSide = "de.sanandrew.core.manpack.mod.client.ClientProxy", serverSide = "de.sanandrew.core.manpack.mod.CommonProxy")
    public static CommonProxy proxy;

    public static ChannelHandler channelHandler;

    public ModCntManPack() {
        super(new ModMetadata());
        ModMetadata meta = super.getMetadata();
        meta.modId = ModCntManPack.MOD_ID;
        meta.name = "SanAndreasPs Manager Pack CORE edition";
        meta.version = ModCntManPack.MOD_VERSION;
        meta.authorList = Arrays.asList(new String[] { "SanAndreasP" });
        meta.description = "A helper coremod which is needed for all my mods.";
        meta.url = "http://www.minecraftforge.net/forum/index.php/topic,2828.0.html";
        meta.screenshots = new String[0];
        meta.logoFile = "";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void modConstruction(FMLConstructionEvent event) {
        System.out.println(event.getASMHarvestedData().getAnnotationsFor(this));
        NetworkRegistry.INSTANCE.register(this, this.getClass(), null, event.getASMHarvestedData());

        // for some reason the proxy annotation does not work in a coremod within a productive MC environment,
        // so I just initialize it on my own here until I find a "proper" solution.
        if( proxy == null ) {
            FMLLog.log(MOD_LOG, Level.INFO, "Inject missing Proxy class for sapmanpack");
            String proxyClsName = "de.sanandrew.core.manpack.mod.CommonProxy";
            if( event.getSide() == Side.CLIENT ) {
                proxyClsName = "de.sanandrew.core.manpack.mod.client.ClientProxy";
            }

            try {
                Class<?> cls = Class.forName(proxyClsName);
                proxy = (CommonProxy) cls.newInstance();
            } catch( ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException e ) {
                e.printStackTrace();
            }
        }

        channelHandler = new ChannelHandler(ModCntManPack.MOD_ID, ModCntManPack.MOD_CHANNEL);
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent evt) {
        ModCntManPack.proxy.registerPackets();
    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {
        channelHandler.initialise();
        ModCntManPack.proxy.registerRenderStuff();
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {
        channelHandler.postInitialise();

        for( Triplet<SAPUpdateManager, Boolean, String> udm : SAPUpdateManager.UPD_MANAGERS ) {
            udm.getValue0().checkForUpdate();
        }
    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSAPManPack());
    }
}
