package be.nokorbis.spigot.commandsigns.addons.commands;

import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;


public class CommandsLifecycleHooker extends AddonLifecycleHookerBase {

	private static final int TICKS_PER_SECOND = 20;

	private final CommandsAddon                   addon;
	private final Map<UUID, PermissionAttachment> playersPermissions = new HashMap<>();

	public CommandsLifecycleHooker(CommandsAddon addon) {
		this.addon = addon;
	}

	@Override
	@NCSLifecycleHook
	public void onPreExecution(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		CommandsConfigurationData data = (CommandsConfigurationData) configurationData;

		if (!data.getTemporarilyGrantedPermissions().isEmpty()) {
			final PermissionAttachment playerPerms = getPlayerPermissions(player);
			for (String permission : data.getTemporarilyGrantedPermissions()) {
				if (!player.hasPermission(permission)) {
					playerPerms.setPermission(permission, true);
				}
			}
		}
	}

	@Override
	@NCSLifecycleHook
	public void onExecution(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		CommandsConfigurationData data = (CommandsConfigurationData) configurationData;

		CommandsRunner runner = new CommandsRunner(addon, player, data.getCommands());
		BukkitTask task = runner.runTaskTimer(addon.getPlugin(), 0, 0);
	}

	@Override
	@NCSLifecycleHook
	public void onPostExecution(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		CommandsConfigurationData data = (CommandsConfigurationData) configurationData;
		//TODO : find a better way to remove permissions without trusting faithfully the scheduler
		//wait
		//notify
		BukkitScheduler scheduler = Bukkit.getScheduler();
		scheduler.runTaskLater(addon.getPlugin(), new TemporaryPermissionsRemover(getPlayerPermissions(player), data.getTemporarilyGrantedPermissions()), data.getTotalDelay() * TICKS_PER_SECOND);

	}

	private PermissionAttachment getPlayerPermissions(final Player player) {
		return playersPermissions.computeIfAbsent(player.getUniqueId(), (uuid) -> player.addAttachment(addon.getPlugin()));
	}
}
