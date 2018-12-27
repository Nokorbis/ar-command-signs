package be.nokorbis.spigot.commandsigns.addons.commands.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.utils.Settings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class CommandsConfigurationData extends AddonConfigurationData {

	private final ArrayList<String> temporarilyGrantedPermissions = new ArrayList<>();
	private final ArrayList<String> commands = new ArrayList<>();

	private int totalDelay = 0;

	public CommandsConfigurationData(Addon addon) {
		super(addon);
	}

	public ArrayList<String> getTemporarilyGrantedPermissions() {
		return temporarilyGrantedPermissions;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public List<String> getNewCommandsQueue() {
		return new ArrayList<>(commands);
	}

	public List<String> getNewPermissionsQueue() {
		return new ArrayList<>(commands);
	}

	public final int getTotalDelay() {
		return totalDelay;
	}

	public final void preprocessTotalDelay() {
		final String delayPrefix = String.valueOf(Settings.DELAY_CHAR());

		this.totalDelay = 0;
		for (final String command : this.commands) {
			if (command.startsWith(delayPrefix)) {
				try {
					int delay = Integer.parseInt(command.substring(1).trim());
					this.totalDelay += delay;
				}
				catch (NumberFormatException ex) {
					addon.getPlugin().getLogger().warning("Invalid delay configuration: " + command);
				}
			}
		}
	}
}
