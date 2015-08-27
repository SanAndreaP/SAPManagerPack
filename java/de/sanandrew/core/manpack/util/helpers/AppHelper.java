/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.util.helpers;

import cpw.mods.fml.common.FMLCommonHandler;
import de.sanandrew.core.manpack.init.ManPackLoadingPlugin;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

final class AppHelper
{
    static void restartApp() {
        try {
            String java = System.getProperty("java.home") + "/bin/javaw"; // java binary

            List<String> vmArguments = new ArrayList<>(ManagementFactory.getRuntimeMXBean().getInputArguments()); // vm arguments
            Iterator<String> it = vmArguments.iterator();
            while( it.hasNext() ) {
                if( it.next().contains("-agentlib") ) {      // if it's the agent argument: we delete it otherwise the
                    it.remove();                             // address of the old application and the new one will be in conflict
                }
            }

            final List<String> cmd = new ArrayList<>();


            cmd.add('"' + java + '"');                      // init the command to execute

            String[] mainCommand = System.getProperty("sun.java.command").split(" ");       // program main and program arguments
            if( mainCommand[0].endsWith(".jar") ) {                                         // program main is a jar, add -jar mainJar
                cmd.add("-jar");
                cmd.add(new File(mainCommand[0]).getPath());
            } else {                                                                        // else it's a .class, add the classpath and mainClass
                cmd.add("-cp");
                cmd.add('"' + System.getProperty("java.class.path") + '"');
                cmd.add(mainCommand[0]);
            }
            cmd.addAll(Arrays.asList(mainCommand).subList(1, mainCommand.length));          // finally add program arguments
            cmd.addAll(vmArguments);                        // add the vm args

            OutputStream os = System.out;
            Runtime.getRuntime().addShutdownHook(new Thread()               // execute the command in a shutdown hook, to be sure that all the
                                                 {                          // resources have been disposed before restarting the application
                                                     @Override
                                                     public void run() {
                                                         try {
                                                             ProcessBuilder builder = new ProcessBuilder(cmd);
//                                                             builder.redirectOutput(Redirect.from(new File("")));
                                                             builder.inheritIO();                                  // inherit the console output from the super process
                                                             builder.start();                                      // start the new process
                                                         } catch( IOException e ) {
                                                             e.printStackTrace();
                                                         }
                                                     }
                                                 });

            System.out.println();
            ManPackLoadingPlugin.MOD_LOG.log(Level.INFO, "---=== Restarting Minecraft! ===---");
            FMLCommonHandler.instance().exitJava(0, false);                                             // try to exit Minecraft
        } catch( Throwable e ) {
            throw new RejectedExecutionException("Error while trying to restart the application", e);
        }
    }

    static void shutdownApp() {
        System.out.println();
        ManPackLoadingPlugin.MOD_LOG.log(Level.INFO, "---=== Shutting down Minecraft! ===---");
        FMLCommonHandler.instance().exitJava(0, false);                                                 // try to exit Minecraft
    }
}
