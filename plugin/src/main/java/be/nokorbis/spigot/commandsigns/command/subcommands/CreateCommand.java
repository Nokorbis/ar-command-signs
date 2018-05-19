package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class CreateCommand extends Command
{
    private NCommandSignsManager manager;

    public CreateCommand(NCommandSignsManager manager)
    {
        super("create", new String[] { "cr", "mk", "make" });
        this.manager = manager;
        this.basePermission = "commandsign.admin.create";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException
    {
        if (!(sender instanceof Player))
        {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }
        Player player = (Player) sender;

        if (isPlayerAvailable(player)) {
            CommandBlock cmdBlock = new CommandBlock();

            EditingConfiguration<CommandBlock> ecf = new EditingConfiguration<>(player, cmdBlock, true);
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
