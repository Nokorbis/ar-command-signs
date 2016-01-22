package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.CommandSignUtils;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class InfoCommand extends Command {

    public InfoCommand() {
        this.command = "info";
        this.aliases.add("i");
        this.basePermission = "commandsign.admin.info";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (args.size() > 2) {
            try {
                long id = Long.parseLong(args.get(1));
                CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                if (cmd == null) {
                    throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
                }
                CommandSignUtils.info(player, ChatColor.GREEN, cmd);
            }
            catch (NumberFormatException ex) {
                throw new CommandSignsCommandException(Messages.get("error.number_argument"));
            }
        }
        else {
            if (!isPlayerAvailable(player)) {
                return false;
            }
            Container.getContainer().getInfoPlayers().add(player);
            player.sendMessage(ChatColor.GOLD + Messages.get("howto.click_for_info"));
        }

        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign info [ID]");
    }
}
