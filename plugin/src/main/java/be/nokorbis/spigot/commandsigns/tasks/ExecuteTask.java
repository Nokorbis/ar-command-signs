package be.nokorbis.spigot.commandsigns.tasks;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.controller.NCommandBlockExecutor;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class ExecuteTask implements Runnable {

	private NCommandBlockExecutor executor;
	private int taskId;
	private Location initialLocation;

	public ExecuteTask(NCommandBlockExecutor cmd) {
		this.executor = cmd;
	}

	@Override
	public void run() {
		final Player player = this.executor.getPlayer();
		if (player != null) {
			try {
				if (player.isOnline() && !player.isDead()) {
					this.executor.execute();
				}
			}
			catch (CommandSignsException e) {
				Bukkit.getScheduler().runTask(CommandSignsPlugin.getPlugin(), () -> player.sendMessage(e.getMessage()));
			}
			finally {
				this.executor.stopPlayerTask(this);
			}
		}
	}

	public CommandBlock getCommandBlock() {
		return this.executor.getCommandBlock();
	}

	public int getTaskId() {
		return this.taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public Location getInitialLocation() {
		return this.initialLocation;
	}

	public void setInitialLocation(Location initialLocation) {
		this.initialLocation = initialLocation;
	}

	public Player getPlayer() {
		return this.executor.getPlayer();
	}

}
