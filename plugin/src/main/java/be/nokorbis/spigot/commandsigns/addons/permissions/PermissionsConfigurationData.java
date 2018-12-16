package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;

import java.util.ArrayList;
import java.util.List;


public class PermissionsConfigurationData extends AddonConfigurationData {

	private List<String> requiredPermissions = new ArrayList<>(2);

	public PermissionsConfigurationData(Addon addon) {
		super(addon);
	}

	public List<String> getRequiredPermissions() {
		return requiredPermissions;
	}
}
