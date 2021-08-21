package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHooker;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.controller.executions.CommandsRunner;
import be.nokorbis.spigot.commandsigns.controller.executions.TemporaryPermissionsGranter;
import be.nokorbis.spigot.commandsigns.controller.executions.TemporaryPermissionsRemover;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;


public class NCommandBlockExecutor {

	private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/events");

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
			String msg = messages.get("usage.timer_delayed");
			msg = msg.replace("{TIME}", String.valueOf(time));
			player.sendMessage(msg);
		}

		ExecuteTask exe = new ExecuteTask(this);
		exe.setInitialLocation(player.getLocation().getBlock().getLocation());
		manager.addRunningExecutor(player, exe);

		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

		BukkitTask task = scheduler.runTaskLaterAsynchronously(CommandSignsPlugin.getPlugin(), exe, time * 20);
		exe.setTaskId(task.getTaskId());
	}

	public void execute() throws CommandSignsException {
		final NCommandSignsAddonLifecycleHolder lifecycleHolder = manager.getLifecycleHolder();

		try {
			processStart(lifecycleHolder);
			processRequirementsCheck(lifecycleHolder);
			processCostsWithdrawn(lifecycleHolder);
			processPreExecution(lifecycleHolder);
			processExecution();
			processPostExecution(lifecycleHolder);
		}
		finally {
			manager.saveExecutionData(this.commandBlock);
			processComplete(lifecycleHolder);
		}
	}

	public void stopPlayerTask(ExecuteTask task) {
		if (task.getCommandBlock() == this.commandBlock) {
			manager.removeRunningExecutor(player, task);
		}
	}

	private void processStart(final NCommandSignsAddonLifecycleHolder lifecycleHolder)
			throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onStartHandlers, hook -> hook::onStarted);
	}

	private void processRequirementsCheck(final NCommandSignsAddonLifecycleHolder lifecycleHolder) throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onRequirementCheckHandlers, hook -> hook::onRequirementCheck);
	}

	private void processCostsWithdrawn(final NCommandSignsAddonLifecycleHolder lifecycleHolder)
			throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onCostWithdrawHandlers, hook -> hook::onCostWithdraw);
	}

	private void processPreExecution(final NCommandSignsAddonLifecycleHolder lifecycleHolder)
			throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onPreExecutionHandlers, hook -> hook::onPreExecution);
	}

	private void processExecution() {

		final BukkitScheduler scheduler = Bukkit.getScheduler();
		final CommandSignsPlugin plugin = manager.getPlugin();

		PermissionAttachment playerPermissions= null;
		try {
			TemporaryPermissionsGranter granter = new TemporaryPermissionsGranter(plugin, this.player, commandBlock.getTemporarilyGrantedPermissions());
			Future<PermissionAttachment> future = scheduler.callSyncMethod(plugin, granter);
			playerPermissions = future.get();
		}
		catch (InterruptedException | ExecutionException e) {
			plugin.getLogger().severe(e.getMessage());
		}

		try {
			CommandsRunner runner = new CommandsRunner(this.player, commandBlock.getCommands());
			Future<CommandsRunner.Result> future = scheduler.callSyncMethod(plugin, runner);
			CommandsRunner.Result result = future.get();
			while(result.isToRunAgain) {
				Thread.sleep(result.timeToWait);
				future = scheduler.callSyncMethod(plugin, runner);
				result = future.get();
			}
		}
		catch (ExecutionException | InterruptedException e) {
			plugin.getLogger().warning(e.getMessage());
		}

		try {
			if (playerPermissions != null) {
				TemporaryPermissionsRemover remover = new TemporaryPermissionsRemover(playerPermissions, commandBlock.getTemporarilyGrantedPermissions());
				Future<Void> future = scheduler.callSyncMethod(plugin, remover);
				future.get();
			}
		}
		catch (InterruptedException | ExecutionException e) {
			plugin.getLogger().warning(e.getMessage());
		}
	}

	private void processPostExecution(final NCommandSignsAddonLifecycleHolder lifecycleHolder)
			throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onPostExecutionHandlers, hook -> hook::onPostExecution);
	}

	private void processComplete(final NCommandSignsAddonLifecycleHolder lifecycleHolder)
			throws CommandSignsRequirementException {
		processLifecycleStep(lifecycleHolder.onCompletedHandlers, hook -> hook::onCompleted);
	}

	@FunctionalInterface
	private interface StepProcessor {
		void process(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) throws CommandSignsRequirementException;
	}

	private void processLifecycleStep(Collection<Addon> addons, Function<AddonLifecycleHooker, StepProcessor> stepProcessorMapper)
			throws CommandSignsRequirementException {
		for (Addon addon : addons) {
			final AddonLifecycleHooker hook = addon.getLifecycleHooker();
			final AddonConfigurationData configuration = commandBlock.getAddonConfigurationData(addon);
			final AddonExecutionData executionData = commandBlock.getAddonExecutionData(addon);

			StepProcessor stepProcessor = stepProcessorMapper.apply(hook);
			stepProcessor.process(player, configuration, executionData);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		NCommandBlockExecutor that = (NCommandBlockExecutor) o;
		return player.equals(that.player) && commandBlock.equals(that.commandBlock);
	}

	@Override
	public int hashCode() {
		return Objects.hash(player, commandBlock);
	}

	public static void setManager(final NCommandSignsManager manager) {
		NCommandBlockExecutor.manager = manager;
	}
}
