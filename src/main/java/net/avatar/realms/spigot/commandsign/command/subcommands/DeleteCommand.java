package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.model.CommandSignsException;
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
        if (!hasBasePermission(sender)) {
            throw new CommandSignsCommandException(Messages.NO_PERMISSION);
        }
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }
        Player player = (Player) sender;

        if (args.size() < 2) {
            if (isPlayerAvailable(player)) {
                Container.getContainer().getDeletingBlocks().put(player, null);
                player.sendMessage(ChatColor.GOLD + Messages.CLICK_TO_DELETE);
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
                        player.sendMessage(ChatColor.GREEN + Messages.COMMAND_DELETED);
                        return true;
                    }
                }
                else if (isPlayerAvailable(player)) {
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    Container.getContainer().getDeletingBlocks().put(player, cmd.getLocation());
                    player.sendMessage(ChatColor.GOLD + Messages.CONFIRM_DELETION);
                    return true;
                }
            }
            catch (NumberFormatException ex) {
                throw new CommandSignsCommandException(Messages.NUMBER_ARGUMENT);
            }
        }
        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign delete [ID]");
    }
}
