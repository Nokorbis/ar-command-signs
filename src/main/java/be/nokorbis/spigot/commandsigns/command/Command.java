package be.nokorbis.spigot.commandsigns.command;

import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.model.CommandSignsException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nokorbis on 1/20/16.
 */
public abstract class Command implements ICommand
{
    protected final String command;
    protected final String[] aliases;
    protected String basePermission;

    protected Command(String command, String[] aliases)
    {
        this.command = command;
        this.aliases = aliases;
    }

    @Override
    public final boolean isCommand(String label)
    {
        label = label.toLowerCase();
        if (this.command.equals(label))
        {
            return true;
        }

        for (String alias : this.aliases)
        {
            if (alias.equals(label))
            {
                return true;
            }
        }


        return false;
    }

    @Override
    public void printUsage(CommandSender sender, boolean permission) throws CommandSignsCommandException
    {
       if (sender.hasPermission(basePermission))
       {
           printUsage(sender);
       }
       else if (permission)
       {
            throw new CommandSignsCommandException(Messages.get("error.no_permission"));
       }
    }

    @Override
    public final String getCommand()
    {
        return this.command;
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args)
    {
        return Collections.emptyList();
    }

    @Override
    public final boolean hasBasePermission(CommandSender sender)
    {
        return sender.hasPermission(this.basePermission);
    }

    /**
     * Checks if the player is already doing some creation/edition/deletion about a configuration.
     * @param player
     * @return <code>true</code> if the player isn't doing anything
     * <code>false</code> if the player is already doing something
     * @throws CommandSignsException
     */
    protected boolean isPlayerAvailable(Player player) throws CommandSignsCommandException
    {
        if (Container.getContainer().getCreatingConfigurations().containsKey(player))
        {
            throw new CommandSignsCommandException(Messages.get("warning.already_creating_configuration"));
        }

        if (Container.getContainer().getEditingConfigurations().containsKey(player))
        {
            throw new CommandSignsCommandException(Messages.get("warning.already_editing_configuration"));
        }

        if (Container.getContainer().getDeletingBlocks().containsKey(player))
        {
            throw new CommandSignsCommandException(Messages.get("warning.already_deleting_configuration"));
        }

        if (Container.getContainer().getCopyingConfigurations().containsKey(player))
        {
            throw new CommandSignsCommandException(Messages.get("warning.already_copying_configuration"));
        }

        if (Container.getContainer().getInfoPlayers().contains(player))
        {
            throw new CommandSignsCommandException(Messages.get("warning.already_info_configuration"));
        }
        return true;
    }
}
