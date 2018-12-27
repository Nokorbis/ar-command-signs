package be.nokorbis.spigot.commandsigns.addons.commands;

import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CommandsRunner extends BukkitRunnable {


	private final CommandsAddon addon;
	private final Player        player;
	private final Queue<String> commands;

	private long nextTimeToRun;

	public CommandsRunner(CommandsAddon addon, Player player, List<String> commands) {
		this.addon = addon;
		this.player = player;
		this.commands = new LinkedList<>(commands);
		this.nextTimeToRun = System.currentTimeMillis();
	}

	@Override
	public void run() {
		final long now = System.currentTimeMillis();

		while (!commands.isEmpty() && nextTimeToRun <= now) {
			String command = commands.poll();

			interpretCommand(now, command);
		}

		if (commands.isEmpty()) {
			cancel();
		}
	}

	private void interpretCommand(final long now, final String command) {
		if (!command.isEmpty()) {
			final char prefix = command.charAt(0);

			if(Settings.DELAY_CHAR() == prefix) {
				interpretDelay(now, command);
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

	private void interpretDelay(long now, String command) {
		try {
			int delay = Integer.parseInt(command.substring(1).trim());
			this.nextTimeToRun = now + (delay*1000);
		}
		catch (NumberFormatException ex) {
			addon.getPlugin().getLogger().warning("Invalid delay configuration");
		}
	}
}
