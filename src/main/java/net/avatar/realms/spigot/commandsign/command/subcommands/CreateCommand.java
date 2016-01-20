package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class CreateCommand extends Command {

    public CreateCommand() {
        this.command = "create";
        this.aliases.add("cr");
        this.aliases.add("mk");
        this.aliases.add("make");
        this.basePermission = "commandsign.admin.create";

    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }
        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            CommandBlock cmdBlock = new CommandBlock();

            EditingConfiguration<CommandBlock> ecf = new EditingConfiguration<CommandBlock>(player, cmdBlock, true);
            ecf.setCurrentMenu(Container.getContainer().getMainMenu());
            ecf.display();
            Container.getContainer().getCreatingConfigurations().put(player, ecf);

            return true;
        }

        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign create");
    }
}
