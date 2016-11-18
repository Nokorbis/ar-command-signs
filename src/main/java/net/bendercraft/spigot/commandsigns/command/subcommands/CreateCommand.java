package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
        this.basePermission = "commandsigns.admin.create";

    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
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
        sender.sendMessage("/commandsigns create");
    }
}
