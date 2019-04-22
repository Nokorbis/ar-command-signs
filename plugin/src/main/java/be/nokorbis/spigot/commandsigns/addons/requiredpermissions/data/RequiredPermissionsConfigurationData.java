package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import org.bukkit.entity.Player;

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

	@Override
	public AddonConfigurationData copy() {
		RequiredPermissionsConfigurationData data = new RequiredPermissionsConfigurationData(addon);
		data.requiredPermissions.addAll(this.requiredPermissions);
		return data;
	}

	@Override
	public void info(Player player) {
		if (!requiredPermissions.isEmpty()) {
			final String format = addonMessages.get("info.permission_format");
			player.sendMessage(addonMessages.get("info.required_permissions"));
			int i = 1;
			for (String permission : requiredPermissions) {
				player.sendMessage(format.replace("{NUMBER}", String.valueOf(i++)).replace("{PERMISSION}", permission));
			}
		}
	}
}
