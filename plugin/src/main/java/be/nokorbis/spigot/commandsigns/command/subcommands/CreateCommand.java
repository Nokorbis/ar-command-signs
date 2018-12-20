package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsConfigurationManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class CreateCommand extends CommandRequiringManager {

    public CreateCommand(NCommandSignsManager manager) {
        super(manager,"create", new String[] { "cr", "mk", "make" });
        this.basePermission = "commandsign.admin.create";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            CommandBlock cmdBlock = new CommandBlock();
            NCommandSignsConfigurationManager configurationManager = new NCommandSignsConfigurationManager(player);
            configurationManager.setCommandBlock(cmdBlock);
            configurationManager.setCreating(true);

            configurationManager.setCurrentMenu(manager.getMainMenu());
            configurationManager.display();

            manager.addConfigurationManager(configurationManager);

            return true;
        }

        return false;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign create");
    }
}
