package be.nokorbis.spigot.commandsigns.controller.executions;

import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;


public class CommandsRunner implements Callable<CommandsRunner.Result> {

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
		this.player.chat(command);
	}

	private void interpretNormalCommand(String command) {
		this.player.performCommand(command);
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
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.substring(1));
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

	public static class Result {
		public boolean isToRunAgain;
		public long timeToWait;
	}
}
