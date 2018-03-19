package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.Command;
import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class DeleteCommand extends Command {

    public DeleteCommand() {
        this.command = "delete";
        this.aliases.add("del");
        this.aliases.add("remove");
        this.aliases.add("rm");
        this.basePermission = "commandsign.admin.delete";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (args.size() < 2) {
            if (isPlayerAvailable(player)) {
                Container.getContainer().getDeletingBlocks().put(player, null);
                player.sendMessage(Messages.get("howto.click_to_delete"));
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
                        player.sendMessage(Messages.get("info.command_deleted"));
                        return true;
                    }
                }
                else if (isPlayerAvailable(player)) {
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    Container.getContainer().getDeletingBlocks().put(player, cmd.getLocation());
                    player.sendMessage(Messages.get("howto.confirm_deletion"));
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
