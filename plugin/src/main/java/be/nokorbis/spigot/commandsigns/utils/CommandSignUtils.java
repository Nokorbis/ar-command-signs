package be.nokorbis.spigot.commandsigns.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.model.BlockActivationMode;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;


public class CommandSignUtils {

	private static final DisplayMessages commandsMessages = DisplayMessages.getDisplayMessages("messages/commands");
	private static final DecimalFormat   decimalFormat    = new DecimalFormat();
	static {
		DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		decimalFormat.setDecimalFormatSymbols(symbols);
		decimalFormat.setMaximumFractionDigits(2);
	}

	public static List<Location> getLocationsAroundPoint(Location location, int radius) {
		List<Location> locations = new LinkedList<>();

		final World world = location.getWorld();
		if (world != null) {
			final int xorg = location.getBlockX();
			final int yorg = location.getBlockY();
			final int zorg = location.getBlockZ();

			for (int x = xorg - radius; x <= (xorg + radius); x++) {
				for (int y = yorg - radius; y <= (yorg + radius); y++) {
					for (int z = zorg - radius; z <= (zorg + radius); z++) {
						Block block = world.getBlockAt(x, y, z);
						if (block.getLocation().distance(location) <= radius) {
							locations.add(block.getLocation());
						}
					}
				}
			}
		}

		return locations;
	}

	public static String formatLocation(String messageWithLocation, Location location) {
		return messageWithLocation
				.replace("{X}", String.valueOf(location.getBlockX()))
				.replace("{Y}", String.valueOf(location.getBlockY()))
				.replace("{Z}", String.valueOf(location.getBlockZ()))
				.replace("{BLOCK_TYPE}", String.valueOf(location.getBlock().getType()));
	}

	public static String formatName(String messageWithName, String name) {
		return messageWithName.replace("{NAME}",formatName(name));
	}

	public static String formatName(String name) {
		if (name == null) {
			return commandsMessages.get("info.no_name");
		}
		return name;
	}

	public static Block findTripwireHookInDirection(Block startingBlock, BlockFace direction) {
		Block block = startingBlock;
		while (block.getType() == Material.TRIPWIRE) {
			block = block.getRelative(direction);
		}
		if (block.getType() == Material.TRIPWIRE_HOOK) {
			return block;
		}

		return null;
	}

	public static String formatTime(double seconds) {
		StringBuilder builder = new StringBuilder();
		long totalSeconds = (long) seconds;
		long minutes = totalSeconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		seconds = seconds % 60;
		minutes = minutes % 60;
		hours 	= hours % 24;

		if (days > 0) {
			builder.append(days).append(" ").append(commandsMessages.get("info.days")).append(" ");
		}
		if (hours > 0) {
			builder.append(hours).append(" ").append(commandsMessages.get("info.hours")).append(" ");
		}
		if (minutes > 0) {
			builder.append(minutes).append(" ").append(commandsMessages.get("info.minutes")).append(" ");
		}
		if (seconds > 0) {
			builder.append(decimalFormat.format(seconds)).append(" ").append(commandsMessages.get("info.seconds"));
		}

		return builder.toString();
	}

	public static String formatTime(long seconds) {
		StringBuilder builder = new StringBuilder();
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		seconds = seconds % 60;
		minutes = minutes % 60;
		hours 	= hours % 24;

		if (days > 0) {
			builder.append(days).append(" ").append(commandsMessages.get("info.days")).append(" ");
		}
		if (hours > 0) {
			builder.append(hours).append(" ").append(commandsMessages.get("info.hours")).append(" ");
		}
		if (minutes > 0) {
			builder.append(minutes).append(" ").append(commandsMessages.get("info.minutes")).append(" ");
		}
		if (seconds > 0) {
			builder.append(seconds).append(" ").append(commandsMessages.get("info.seconds"));
		}

		return builder.toString();
	}

	public static void info (Player player, CommandBlock cmdB, Set<Addon> addons) {

		player.sendMessage(formatName(commandsMessages.get("info.identifier_format"), cmdB.getName()).replace("{ID}", String.valueOf(cmdB.getId())));
        player.sendMessage(formatLocation(commandsMessages.get("info.block_format"), cmdB.getLocation()));

		sendActivationModeInfo(player, cmdB);

		if (cmdB.isDisabled()) {
        	player.sendMessage(commandsMessages.get("info.disabled"));
		}

		sendTimerInfo(player, cmdB);
		sendPermissionsInfo(player, cmdB);
		sendCommandsInfo(player, cmdB);

		for (Addon addon : addons) {
			AddonConfigurationData data = cmdB.getAddonConfigurationData(addon);
			if (data != null) {
				data.info(player);
			}
		}
    }

	private static void sendCommandsInfo (Player player, CommandBlock cmdB) {
		if (!cmdB.getCommands().isEmpty()) {
			String format = commandsMessages.get("info.command_format");
			player.sendMessage(commandsMessages.get("info.commands"));
			int i = 1;
			for (String command : cmdB.getCommands()) {
				player.sendMessage(format.replace("{NUMBER}", String.valueOf(i++)).replace("{COMMAND}", command));
			}
		}
	}

	private static void sendPermissionsInfo (Player player, CommandBlock cmdB) {
		if (!cmdB.getTemporarilyGrantedPermissions().isEmpty()) {
			String format = commandsMessages.get("info.permission_format");
			player.sendMessage(commandsMessages.get("info.granted_permissions"));
			int i = 1;
			for (String permission : cmdB.getTemporarilyGrantedPermissions()) {
				player.sendMessage(format.replace("{NUMBER}", String.valueOf(i++)).replace("{PERMISSION}", permission));
			}
		}
	}

	private static void sendTimerInfo (Player player, CommandBlock cmdB) {
		if ((cmdB.hasTimer())) {
			String timer = commandsMessages.get("info.timer")
										   .replace("{TIME}", formatTime(cmdB.getTimeBeforeExecution()))
										   .replace("{|CANCELLED|}",cmdB.isCancelledOnMove() ? commandsMessages.get("info.cancelled_on_move"): "")
										   .replace("{|RESET|}", cmdB.isResetOnMove() ? commandsMessages.get("info.reset_on_move") : "");
			player.sendMessage(timer);
		}
	}

	private static void sendActivationModeInfo (Player player, CommandBlock cmdB) {
		BlockActivationMode mode = cmdB.getActivationMode();
		if (mode != BlockActivationMode.BOTH) {
			String msg = commandsMessages.get("info.activation_mode");
			if (mode == BlockActivationMode.ACTIVATED) {
				msg = msg.replace("{MODE}", commandsMessages.get("info.activation_mode.activated"));
			}
			else {
				msg = msg.replace("{MODE}", commandsMessages.get("info.activated_mode.deactivated"));
			}
			player.sendMessage(msg);
		}
	}
}
