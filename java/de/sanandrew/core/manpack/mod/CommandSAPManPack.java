package de.sanandrew.core.manpack.mod;

import de.sanandrew.core.manpack.util.SAPUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

import java.io.IOException;

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
            } catch( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
