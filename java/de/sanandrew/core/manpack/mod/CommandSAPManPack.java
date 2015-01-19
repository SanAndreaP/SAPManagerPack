/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.mod;

import de.sanandrew.core.manpack.util.helpers.SAPUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import java.util.concurrent.RejectedExecutionException;

public class CommandSAPManPack
        extends CommandBase
{
    @Override
    public String getCommandName() {
        return "sapmanpack";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.sapmanpack.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] cmdParams) {
        if( cmdParams.length < 1 || cmdParams.length > 2 ) {
            throw new WrongUsageException("commands.sapmanpack.usage");
        }

        if( "restart".equals(cmdParams[0]) ) {
            try {
                SAPUtils.restartApp();
            } catch( RejectedExecutionException e ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
