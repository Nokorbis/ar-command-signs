package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class DeleteCommand extends Command {
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (args.size() < 2) {
            if (isPlayerAvailable(player)) {
                Container.getContainer().getDeletingBlocks().put(player, null);
                player.sendMessage(ChatColor.GOLD + Messages.get("howto.click_to_delete"));
                return true;
            }
        }
        else {
            try {
                long id = Long.parseLong(args.get(1));
                if (Container.getContainer().getDeletingBlocks().containsKey(player)) {
                    Location loc = Container.getContainer().getDeletingBlocks().get(player);
                    CommandBlock cmd = Container.getContainer().getCommandBlocks().get(loc);
                    if  (cmd != null && cmd.getId() == id) {
                        Container.getContainer().getCommandBlocks().remove(loc);
                        Container.getContainer().getDeletingBlocks().remove(player);
                        Container.getContainer().getSaver().delete(cmd.getId());
                        player.sendMessage(ChatColor.GREEN + Messages.get("info.command_deleted"));
                        return true;
                    }
                }
                else if (isPlayerAvailable(player)) {
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    Container.getContainer().getDeletingBlocks().put(player, cmd.getLocation());
                    player.sendMessage(ChatColor.GOLD + Messages.get("howto.confirm_deletion"));
                    return true;
                }
            }
            catch (NumberFormatException ex) {
                throw new CommandSignsCommandException(Messages.get("error.number_argument"));
            }
        }
        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign delete [ID]");
    }
}
