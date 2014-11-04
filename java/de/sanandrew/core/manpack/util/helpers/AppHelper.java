/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP, SilverChiren and CliffracerX
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.helpers;

import cpw.mods.fml.common.FMLLog;
import de.sanandrew.core.manpack.mod.ModCntManPack;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;

final class AppHelper
{
    static void restartApp() throws IOException {
        try {
            String java = System.getProperty("java.home") + "/bin/javaw"; // java binary

            List<String> vmArguments = ManagementFactory.getRuntimeMXBean().getInputArguments(); // vm arguments
            StringBuilder vmArgsOneLine = new StringBuilder();
            for (String arg : vmArguments) {
                if (!arg.contains("-agentlib")) { // if it's the agent argument : we ignore it otherwise the
                    vmArgsOneLine.append(arg);    // address of the old application and the new one will be in conflict
                    vmArgsOneLine.append(' ');
                }
            }

            final StringBuffer cmd = new StringBuffer('"' + java + "\" " + vmArgsOneLine); // init the command to execute, add the vm args

            String[] mainCommand = System.getProperty("sun.java.command").split(" "); // program main and program arguments
            if( mainCommand[0].endsWith(".jar") ) { // program main is a jar, add -jar mainJar
                cmd.append("-jar ").append(new File(mainCommand[0]).getPath());
            } else { // else it's a .class, add the classpath and mainClass
                cmd.append("-cp \"").append(System.getProperty("java.class.path")).append("\" ").append(mainCommand[0]);
            }

            for (int i = 1; i < mainCommand.length; i++) { // finally add program arguments
                cmd.append(' ');
                cmd.append(mainCommand[i]);
            }

            // execute the command in a shutdown hook, to be sure that all the
            // resources have been disposed before restarting the application
            Runtime.getRuntime().addShutdownHook(new Thread() {
                                                     @Override
                                                     public void run() {
                                                         try {
                                                             ProcessBuilder builder = new ProcessBuilder(cmd.toString());
                                                             builder.inheritIO();    // inherit the console output from the super process (the process initiating the restart)
                                                             builder.start();        // start the new process
                                                         } catch (IOException e) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 });

            // exit
            try {
                Class.forName("net.minecraft.client.Minecraft");    // force-load the class. If it throws an exception, we're on a dedicated server.
                System.out.println();
                FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Restarting Minecraft Client! ===---");    // try to shutdown Minecraft applet
                System.out.println();
                Minecraft.getMinecraft().shutdownMinecraftApplet();
            } catch( ClassNotFoundException | NoClassDefFoundError ex ) {
                System.out.println();
                FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Restarting Minecraft Server! ===---");    // if Minecraft class was not found
                System.out.println();                                                                           // (usually the case on a dedi-server),
                MinecraftServer.getServer().initiateShutdown();                                                 // then shutdown server
            }
        } catch (Throwable e) {
            throw new IOException("Error while trying to restart the application", e);
        }
    }

    static void shutdownApp() {
        try {
            Class.forName("net.minecraft.client.Minecraft");    // force-load the class. If it throws an exception, we're on a dedicated server.
            System.out.println();
            FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Shutdown Minecraft Client! ===---");      // try to shutdown Minecraft applet
            System.out.println();
            Minecraft.getMinecraft().shutdownMinecraftApplet();
        } catch( ClassNotFoundException | NoClassDefFoundError ex ) {
            System.out.println();
            FMLLog.log(ModCntManPack.MOD_LOG, Level.INFO, "---=== Shutdown Minecraft Server! ===---");      // if Minecraft class was not found
            System.out.println();                                                                           // (usually the case on a dedi-server),
            MinecraftServer.getServer().initiateShutdown();                                                 // then shutdown server
        }
    }

    static File getMcDir(String path) {
        return new File(".", path);
    }
}
