/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.event.FMLInterModComms.IMCMessage;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import de.sanandrew.core.manpack.managers.SAPUpdateManager;
import de.sanandrew.core.manpack.managers.SAPUpdateManager.Version;
import de.sanandrew.core.manpack.mod.client.ClientProxy;
import de.sanandrew.core.manpack.network.NetworkManager;
import de.sanandrew.core.manpack.util.MutableString;
import de.sanandrew.core.manpack.util.javatuples.Triplet;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Collections;

public class ModCntManPack
        extends DummyModContainer
{
    public static final String MOD_CHANNEL = "sapmanpack";
    public static final String MOD_ID = "sapmanpack";
    public static final Logger MOD_LOG = LogManager.getLogger(MOD_ID);
    public static final Logger UPD_LOG = LogManager.getLogger("SAPUpdateMgr");

    public static final String MOD_VERSION = "2.5.0";

    public static final int FORGE_BULD_MIN = 1291;

    public static CommonProxy proxy;

    public ModCntManPack() {
        super(new ModMetadata());
        ModMetadata meta = super.getMetadata();
        meta.modId = ModCntManPack.MOD_ID;
        meta.name = "SanAndreasPs Manager Pack CORE edition";
        meta.version = ModCntManPack.MOD_VERSION;
        meta.authorList = Collections.singletonList("SanAndreasP");
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
        if( Integer.parseInt(FMLInjectionData.data()[3].toString()) < FORGE_BULD_MIN ) {
            MOD_LOG.log(Level.FATAL, "The installed version of Forge is outdated! Minimum build required is %d, installed is build %s. " +
                           "Either update Forge or remove this mod, it will cause problems otherwise!", FORGE_BULD_MIN, FMLInjectionData.data()[3]);
        }

        NetworkRegistry.INSTANCE.register(this, this.getClass(), null, event.getASMHarvestedData());
    }

    @Subscribe
    @SideOnly(Side.CLIENT)
    public void injectClientProxy(FMLPreInitializationEvent evt) {
        proxy = new ClientProxy();
        this.commonPreInit(evt);
    }

    @Subscribe
    @SideOnly(Side.SERVER)
    public void injectServerProxy(FMLPreInitializationEvent evt) {
        proxy = new CommonProxy();
        this.commonPreInit(evt);
    }

    public void commonPreInit(FMLPreInitializationEvent event) {
        ConfigurationManager.load(event.getSuggestedConfigurationFile());

        SAPUpdateManager.createUpdateManager("SAP Manager Pack", new Version(MOD_VERSION),
                                             "https://raw.githubusercontent.com/SanAndreasP/SAPManagerPack/master/update.json",
                                             "http://www.curseforge.com/projects/226994/", this.getSource());
    }

    @Subscribe
    public void init(FMLInitializationEvent evt) {
        ModCntManPack.proxy.registerRenderStuff();
    }

    @Subscribe
    public void postInit(FMLPostInitializationEvent evt) {
        NetworkManager.initPacketHandler();

        for( Triplet<SAPUpdateManager, MutableBoolean, MutableString> udm : SAPUpdateManager.UPD_MANAGERS ) {
            udm.getValue0().checkForUpdate();
        }
    }

    @Subscribe
    public void imcReceive(FMLInterModComms.IMCEvent event) {
        for( IMCMessage msg : event.getMessages() ) {
            if( msg.key.equalsIgnoreCase("add-updatemgr") && msg.isNBTMessage() ) {
                NBTTagCompound nbt = msg.getNBTValue();
                SAPUpdateManager.createUpdateManager(nbt.getString("modName"), new Version(nbt.getString("version")), nbt.getString("updateInfoUrl"),
                                                     nbt.getString("projectLink"), nbt.hasKey("jarLocation") ? new File(nbt.getString("jarLocation")) : null);
            }
        }
    }

    @Subscribe
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSAPManPack());
    }

    @Override
    public File getSource() {
        return ManPackLoadingPlugin.source;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }
}
