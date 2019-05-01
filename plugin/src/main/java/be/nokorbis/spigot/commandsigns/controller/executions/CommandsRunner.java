package be.nokorbis.spigot.commandsigns.controller.executions;

import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.Bukkit;
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

	private final Player        player;
	private final Queue<String> commands;

	private final Result result = new Result();

	public CommandsRunner(Player player, List<String> commands) {
		this.player = player;
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
			else {
				interpretChat(command);
			}
		}
		return 0L;
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
		Matcher m = PLAYER_PATTERN.matcher(command);
		if (m.find()) {
			command = m.replaceAll(player.getName());
		}
		m = ALL_PATTERN.matcher(command);
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

	public static class Result {
		public boolean isToRunAgain;
		public long timeToWait;
	}
}
