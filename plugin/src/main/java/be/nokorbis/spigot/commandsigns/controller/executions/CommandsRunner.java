package be.nokorbis.spigot.commandsigns.controller.executions;

import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommandsRunner implements Callable<CommandsRunner.Result> {

	private static final Pattern ALL_PATTERN    = Pattern.compile("%ALL%", Pattern.CASE_INSENSITIVE);
	private static final Pattern RADIUS_PATTERN = Pattern.compile("%RADIUS=(\\d+)%", Pattern.CASE_INSENSITIVE);
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%PLAYER%", Pattern.CASE_INSENSITIVE);

	private static final Pattern PLAYER_LOC_WORLD_PATTERN = Pattern.compile("%PLAYER_LOC_WORLD%", Pattern.CASE_INSENSITIVE);
	private static final Pattern PLAYER_LOC_X_PATTERN = Pattern.compile("%PLAYER_LOC_X(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);
	private static final Pattern PLAYER_LOC_Y_PATTERN = Pattern.compile("%PLAYER_LOC_Y(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);
	private static final Pattern PLAYER_LOC_Z_PATTERN = Pattern.compile("%PLAYER_LOC_Z(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);

	private static final Pattern SIGN_LOC_WORLD_PATTERN = Pattern.compile("%SIGN_LOC_WORLD%", Pattern.CASE_INSENSITIVE);
	private static final Pattern SIGN_LOC_X_PATTERN = Pattern.compile("%SIGN_LOC_X(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);
	private static final Pattern SIGN_LOC_Y_PATTERN = Pattern.compile("%SIGN_LOC_Y(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);
	private static final Pattern SIGN_LOC_Z_PATTERN = Pattern.compile("%SIGN_LOC_Z(?:([-+*/])(\\d+))?%", Pattern.CASE_INSENSITIVE);

	private final Player        player;
	private final Location      signLocation;
	private final Queue<String> commands;

	private final Result result = new Result();

	public CommandsRunner(Player player, Location signLocation, List<String> commands) {
		this.player = player;
		this.signLocation = signLocation;
		this.commands = new LinkedList<>(commands);
	}

	@Override
	public Result call() {
		long commandTime = -1;
		while (commandTime < 1 && !commands.isEmpty()) {
			String command = commands.poll();
			commandTime = interpretCommand(command);
		}
		result.isToRunAgain = !commands.isEmpty();
		result.timeToWait = commandTime;
		return result;
	}

	private long interpretCommand(final String command) {
		if (!command.isEmpty()) {
			final char prefix = command.charAt(0);

			if(Settings.DELAY_CHAR() == prefix) {
				return interpretDelay(command);
			}
			else if (Settings.SERVER_CHAR() == prefix) {
				interpretServerCommand(command);
			}
			else if (Settings.OP_CHAR() == prefix) {
				interpretOpCommand(command);
			}
			else if (Settings.COMMAND_CHAR() == prefix) {
				interpretNormalCommand(command.substring(1));
			}
			else if (Settings.MESSAGE_CHAR() == prefix) {
				interpretMessage(command.substring(1));
			}
			else {
				interpretChat(command);
			}
		}
		return 0L;
	}

	private void interpretMessage(String command) {
		String message = command.replace("&", "§");
		List<String> cmds = fillPlaceholders(message);
		for (String cmd : cmds) {
			this.player.sendMessage(cmd);
		}
	}

	private void interpretChat(String command) {
		List<String> cmds = fillPlaceholders(command);
		for (String cmd : cmds) {
			this.player.chat(cmd);
		}
	}

	private void interpretNormalCommand(String command) {
		List<String> cmds = fillPlaceholders(command);
		for (String cmd : cmds) {
			this.player.performCommand(cmd);
		}
	}

	private void interpretOpCommand(String command) {
		String cmd = command.substring(1);
		if (!this.player.isOp()) {
			this.player.setOp(true);
			interpretNormalCommand(cmd);
			this.player.setOp(false);
		}
		else {
			interpretNormalCommand(cmd);
		}
	}

	private void interpretServerCommand(String command) {
		List<String> cmds = fillPlaceholders(command.substring(1));
		for (String cmd : cmds) {
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}
	}

	private long interpretDelay(String command) {
		try {
			int delay = Integer.parseInt(command.substring(1).trim());
			return (delay*1000L);
		}
		catch (NumberFormatException ex) {
		}
		return -1L;
	}

	private List<String> fillPlaceholders(String command) {
		List<String> cmds = new ArrayList<>();

		command = replacePlayerNamePlaceholder(command);

		command = replacePlayerLocation(command);
		command = replaceSignLocation(command);

		Matcher m = ALL_PATTERN.matcher(command);
		if (m.find()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				cmds.add(m.replaceAll(p.getName()));
			}
		}
		else {
			m = RADIUS_PATTERN.matcher(command);
			if (m.find()) {
				try {
					String str = m.group(1);
					int radius = Integer.parseInt(str);
					if (radius > 0) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().equals(player.getWorld()) && p.getLocation().distance(player.getLocation()) <= radius) {
								cmds.add(m.replaceAll(p.getName()));
							}
						}
					}
				}
				catch (Exception ignored) {
				}
			}
			else {
				cmds.add(command);
			}
		}
		return cmds;
	}

	private String replacePlayerLocation (String command) {
		command = replacePlayerWorldPlaceholder(command);
		command = replacePlayerXPlaceholder(command);
		command = replacePlayerYPlaceholder(command);
		command = replacePlayerZPlaceholder(command);
		return command;
	}

	private String replaceSignLocation (String command) {
		command = replaceSignWorldPlaceholder(command);
		command = replaceSignXPlaceholder(command);
		command = replaceSignYPlaceholder(command);
		command = replaceSignZPlaceholder(command);
		return command;
	}

	private String replacePlayerWorldPlaceholder (String command) {
		Matcher m = PLAYER_LOC_WORLD_PATTERN.matcher(command);
		if (m.find()) {
			command = m.replaceAll(player.getLocation().getWorld().getName());
		}
		return command;
	}

	private String replaceSignWorldPlaceholder (String command) {
		Matcher m = SIGN_LOC_WORLD_PATTERN.matcher(command);
		if (m.find()) {
			command = m.replaceAll(signLocation.getWorld().getName());
		}
		return command;
	}

	private String replaceLocParameter (String command, Pattern patternToMatch, int position) {
		Matcher m = patternToMatch.matcher(command);
		if (m.find()) {
			if (m.groupCount() > 0) {
				position = computePositionValue(position, m.group(1), m.group(2));
			}
			command = m.replaceAll(String.valueOf(position));
		}
		return command;
	}

	private String replaceSignZPlaceholder (String command) {
		return replaceLocParameter(command, SIGN_LOC_Z_PATTERN, signLocation.getBlockZ());
	}

	private String replaceSignYPlaceholder (String command) {
		return replaceLocParameter(command, SIGN_LOC_Y_PATTERN, signLocation.getBlockY());
	}

	private String replaceSignXPlaceholder (String command) {
		return replaceLocParameter(command, SIGN_LOC_X_PATTERN, signLocation.getBlockX());
	}

	private String replacePlayerZPlaceholder (String command) {
		return replaceLocParameter(command, PLAYER_LOC_Z_PATTERN, player.getLocation().getBlockZ());
	}

	private String replacePlayerYPlaceholder (String command) {
		return replaceLocParameter(command, PLAYER_LOC_Y_PATTERN, player.getLocation().getBlockY());
	}

	private String replacePlayerXPlaceholder (String command) {
		return replaceLocParameter(command, PLAYER_LOC_X_PATTERN, player.getLocation().getBlockX());
	}

	private int computePositionValue (int initialValue, String operator, String diffParameter) {
		try {
			int difference = Integer.parseInt(diffParameter);
			if ("+".equals(operator)) {
				initialValue += difference;
			}
			else if ("-".equals(operator)) {
				initialValue -= difference;
			}
			else if ("/".equals(operator)) {
				initialValue /= difference;
			}
			else if ("*".equals(operator)) {
				initialValue += difference;
			}
		}
		catch (Exception ignored) {
		}
		return initialValue;
	}

	private String replacePlayerNamePlaceholder (String command) {
		Matcher m = PLAYER_PATTERN.matcher(command);
		if (m.find()) {
			command = m.replaceAll(player.getName());
		}
		return command;
	}

	public static class Result {
		public boolean isToRunAgain;
		public long timeToWait;
	}
}
