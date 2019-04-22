package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by nokorbis on 1/20/16.
 */
public class NearCommand extends CommandRequiringManager {

	public NearCommand(NCommandSignsManager manager) {
		super(manager, "near", new String[0]);
		this.basePermission = "commandsign.admin.near";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		if (!(sender instanceof Player)) {
			throw new CommandSignsCommandException(commandMessages.get("error.command.player_requirement"));
		}

		if (args.isEmpty()) {
			throw new CommandSignsCommandException(commandMessages.get("error.command.radius_requirement"));
		}

		Player player = (Player) sender;

		try {
			int radius = Integer.parseInt(args.get(0));

			LinkedList<CommandBlock> commandBlocks = new LinkedList<>();
			for (Location loc : CommandSignUtils.getLocationsAroundPoint(player.getLocation(), radius)) {
				CommandBlock commandBlock = manager.getCommandBlock(loc);
				if (commandBlock != null) {
					commandBlocks.add(commandBlock);
				}
			}
			if (!commandBlocks.isEmpty()) {
				final String format = commandMessages.get("commands.near.format");
				for (CommandBlock cmd : commandBlocks) {
					sender.sendMessage(formatCommand(format, cmd));
				}

			}
		}
		catch (NumberFormatException ex) {
			throw new CommandSignsCommandException(commandMessages.get("error.command.number_requirement"));
		}

		return true;
	}

	/**
	 * Format the message so that it can be easily read by a player
	 *
	 * @param cmd The command block whose String needs to be formatted
	 *
	 * @return A formatted String
	 */
	private String formatCommand(final String format, CommandBlock cmd) {
		final Location location = cmd.getLocation();
		return format.replace("{NAME}", cmd.getName())
					 .replace("{ID}", String.valueOf(cmd.getId()))
					 .replace("{BLOCK_TYPE}", String.valueOf(location.getBlock().getType()))
					 .replace("{X}", String.valueOf(location.getX()))
					 .replace("{Y}", String.valueOf(location.getY()))
					 .replace("{Z}", String.valueOf(location.getZ()));
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign near <radius>");
	}
}
