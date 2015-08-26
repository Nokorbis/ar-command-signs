package net.avatar.realms.spigot.commandsign.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class ExecuteTask implements Runnable{

	private Player player;
	private CommandBlock cmdBlock;
	private int taskId;
	private Location location;

	public ExecuteTask(Player player, CommandBlock cmd) {
		this.player = player;
		this.cmdBlock = cmd;
	}

	@Override
	public void run () {
		if ((this.player == null) || !this.player.isOnline() || this.player.isDead()) {
			CommandSign.getPlugin().getExecutingTasks().remove(this.player.getUniqueId());
			return;
		}
		this.cmdBlock.execute(this.player);
		CommandSign.getPlugin().getExecutingTasks().remove(this.player.getUniqueId());
	}

	public CommandBlock getCommandBlock() {
		return this.cmdBlock;
	}

	public int getTaskId () {
		return this.taskId;
	}

	public void setTaskId (int taskId) {
		this.taskId = taskId;
	}

	public Location getLocation () {
		return this.location;
	}

	public void setLocation (Location location) {
		this.location = location;
	}

	public Player getPlayer() {
		return this.player;
	}
}
