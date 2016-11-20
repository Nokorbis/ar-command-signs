package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        if (args.size() < 2) {
            throw new CommandSignsCommandException(Messages.get("error.command_needs_arguments"));
        }

        Player player = (Player) sender;

        try {
            long id = Long.parseLong(args.get(1));
            CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
            if (cmd == null) {
                throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
            }
            player.teleport(cmd.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
            return true;
        }
        catch (NumberFormatException ex) {
            throw new CommandSignsCommandException(Messages.get("error.number_argument"));
        }
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign teleport <ID>");
    }
}
