package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionDataUpdater;
import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;


public class NCommandBlockExecutor {


	private static NCommandSignsManager manager;

	private final Player player;
	private final CommandBlock commandBlock;

	public NCommandBlockExecutor(Player player, CommandBlock commandBlock) {
		this.player = player;
		this.commandBlock = commandBlock;
	}

	public final Player getPlayer() {
		return player;
	}

	public final CommandBlock getCommandBlock() {
		return commandBlock;
	}

	public void run() {
		long time = 0;
		if (commandBlock.hasTimer() && !player.hasPermission("commandsign.timer.bypass")) {
			time = commandBlock.getTimeBeforeExecution();
			String msg = Messages.get("info.timer_delayed");
			msg = msg.replace("{TIME}", String.valueOf(time));
			player.sendMessage(msg);
		}

		ExecuteTask exe = new ExecuteTask(this);
		exe.setInitialLocation(player.getLocation().getBlock().getLocation());
		Container.getContainer().getExecutingTasks().put(player.getUniqueId(), exe);

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

		BukkitTask task = scheduler.runTaskLaterAsynchronously(CommandSignsPlugin.getPlugin(), exe, time * 20);
		exe.setTaskId(task.getTaskId());
	}

	public void execute() throws CommandSignsException {
		withdrawAddonCosts();

		//TODO :  execute command

		updateAddonsExecutionData();
	}

	private void updateAddonsExecutionData() {
		for (Addon addon : manager.getRegisteredAddons()) {
			AddonExecutionDataUpdater updater = addon.getAddonExecutionDataUpdater();
			if (updater != null) {
				updater.update(this.commandBlock.getAddonConfigurationData(addon), this.commandBlock.getAddonExecutionData(addon), this.player);
			}
		}
	}

	private void withdrawAddonCosts() {
		for (Addon addon : manager.getRegisteredAddons()) {
			CostHandler handler = addon.getCostHandler();
			if (handler != null) {
				handler.withdrawPlayer(player, this.commandBlock.getAddonConfigurationData(addon), this.commandBlock.getAddonExecutionData(addon));
			}
		}
	}

	public  void checkRequirements() throws CommandSignsRequirementException {
		if (this.player == null) {
			throw new CommandSignsRequirementException(Messages.get("usage.invalid_player"));
		}

		checkAddonRequirements();
	}

	private void checkAddonRequirements() throws CommandSignsRequirementException {
		for (Addon addon : manager.getRegisteredAddons()) {
			RequirementHandler handler = addon.getRequirementHandler();
			if (handler != null) {
				handler.checkRequirement(player, this.commandBlock.getAddonConfigurationData(addon), this.commandBlock.getAddonExecutionData(addon));
			}

			handler = addon.getCostHandler();
			if (handler != null) {
				handler.checkRequirement(player, this.commandBlock.getAddonConfigurationData(addon), this.commandBlock.getAddonExecutionData(addon));
			}
		}
	}

	public static void setManager(final NCommandSignsManager manager) {
		NCommandBlockExecutor.manager = manager;
	}
}
