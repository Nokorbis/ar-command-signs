package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class CopyCommand extends Command {

    public CopyCommand() {
        this.command = "copy";
        this.aliases.add("cp");
        this.basePermission = "commandsign.admin.copy";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!hasBasePermission(sender)) {
            throw new CommandSignsCommandException(Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }

        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            if (args.size() < 2) {
                Container.getContainer().getCopyingConfigurations().put(player, null);
                player.sendMessage(ChatColor.GOLD + Messages.CLICK_TO_COPY);
            }
            else {
                try {
                    long id = Long.parseLong(args.get(1));
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    if (cmd == null) {
                        throw new CommandSignsCommandException(Messages.INVALID_COMMAND_ID);
                    }
                    Container.getContainer().getCopyingConfigurations().put(player, cmd.copy());
                    player.sendMessage(ChatColor.GOLD + Messages.CLICK_TO_PASTE);
                }
                catch (NumberFormatException ex) {
                    throw new CommandSignsCommandException(Messages.NUMBER_ARGUMENT);
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign copy [ID]");
    }
}
