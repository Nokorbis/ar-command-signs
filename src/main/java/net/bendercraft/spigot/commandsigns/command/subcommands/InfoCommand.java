package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.CommandSignUtils;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
        this.basePermission = "commandsigns.admin.info";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (args.size() >= 2) {
            try {
                long id = Long.parseLong(args.get(1));
                CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                if (cmd == null) {
                    throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
                }
                CommandSignUtils.info(player, cmd);
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
            player.sendMessage(Messages.get("howto.click_for_info"));
        }

        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsigns info [ID]");
    }
}
