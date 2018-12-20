package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;


/**
 * Created by nokorbis on 1/20/16.
 */
public class TeleportCommand extends CommandRequiringManager {
	public TeleportCommand(NCommandSignsManager manager) {
		super(manager, "teleport", new String[] {"tp"});
		this.basePermission = "commandsign.admin.teleport";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		if (!(sender instanceof Player)) {
			throw new CommandSignsCommandException(Messages.get("error.player_command"));
		}
		if (args.isEmpty()) {
			throw new CommandSignsCommandException(Messages.get("error.command_needs_arguments"));
		}

		Player player = (Player) sender;

		try {
			long id = Long.parseLong(args.get(0));
			CommandBlock commandBlock = manager.getCommandBlock(id);
			if (commandBlock == null) {
				throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
			}
			player.teleport(commandBlock.getLocation(), PlayerTeleportEvent.TeleportCause.COMMAND);
			return true;
		}
		catch (NumberFormatException ex) {
			throw new CommandSignsCommandException(Messages.get("error.number_argument"));
		}
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign teleport <ID>");
	}
}
