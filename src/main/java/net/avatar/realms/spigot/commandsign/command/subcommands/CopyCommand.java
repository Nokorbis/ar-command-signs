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
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }

        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            if (args.size() < 2) {
                Container.getContainer().getCopyingConfigurations().put(player, null);
                player.sendMessage(Messages.get("howto.click_to_copy"));
            }
            else {
                try {
                    long id = Long.parseLong(args.get(1));
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    if (cmd == null) {
                        throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
                    }
                    Container.getContainer().getCopyingConfigurations().put(player, cmd.copy());
                    player.sendMessage(Messages.get("howto.click_to_paste"));
                }
                catch (NumberFormatException ex) {
                    throw new CommandSignsCommandException(Messages.get("error.number_argument"));
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
