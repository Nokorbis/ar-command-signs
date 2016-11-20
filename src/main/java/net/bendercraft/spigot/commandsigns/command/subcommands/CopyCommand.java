package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
