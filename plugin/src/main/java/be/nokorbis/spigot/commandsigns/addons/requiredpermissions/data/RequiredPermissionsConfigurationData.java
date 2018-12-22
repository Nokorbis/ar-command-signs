package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;

import java.util.ArrayList;
import java.util.List;


public class RequiredPermissionsConfigurationData extends AddonConfigurationData {

	private List<String> requiredPermissions = new ArrayList<>(2);

	public RequiredPermissionsConfigurationData(Addon addon) {
		super(addon);
	}

	public List<String> getRequiredPermissions() {
		return requiredPermissions;
	}
}
