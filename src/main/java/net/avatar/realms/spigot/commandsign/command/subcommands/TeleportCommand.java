package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.model.CommandSignsException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class TeleportCommand extends Command {

    public TeleportCommand() {
        this.command = "teleport";
        this.aliases.add("tp");
        this.basePermission = "commandsign.admin.teleport";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!hasBasePermission(sender)) {
            throw new CommandSignsCommandException(Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }
        if (args.size() < 2) {
            throw new CommandSignsCommandException(Messages.COMMAND_NEEDS_ARGUMENTS);
        }

        Player player = (Player) sender;

        try {
            long id = Long.parseLong(args.get(1));
            CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
            if (cmd == null) {
                throw new CommandSignsCommandException(Messages.INVALID_COMMAND_ID);
            }
            player.teleport(cmd.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }
        catch (NumberFormatException ex) {
            throw new CommandSignsCommandException(Messages.NUMBER_ARGUMENT);
        }
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign teleport <ID>");
    }
}
