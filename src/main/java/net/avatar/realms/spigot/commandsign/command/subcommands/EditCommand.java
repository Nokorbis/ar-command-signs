package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
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
public class EditCommand extends Command {

    public EditCommand () {
        this.command = "edit";

        this.basePermission = "commandsign.admin.edit";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            EditingConfiguration<CommandBlock> conf = null;
            if (args.size() < 2) {
                conf = new EditingConfiguration<CommandBlock>(player, false);
                player.sendMessage(ChatColor.GOLD + Messages.get("howto.click_to_edit"));
            }
            else {
                try {
                    long id = Long.parseLong(args.get(1));
                    CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                    if (cmd == null) {
                        throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
                    }
                    conf = new EditingConfiguration<CommandBlock>(player, cmd, false);
                }
                catch (NumberFormatException ex) {
                    throw new CommandSignsCommandException(Messages.get("error.number_argument"));
                }
            }

            conf.setCurrentMenu(Container.getContainer().getMainMenu());
            if (conf.getEditingData() != null) {
                conf.display();
            }
            Container.getContainer().getEditingConfigurations().put(player, conf);
            return true;
        }

        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign edit [ID]");
    }
}
