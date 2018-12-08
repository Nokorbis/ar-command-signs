package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public class PermissionsRequirementHandler implements RequirementHandler
{
    @Override
    public void checkRequirement(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException {
        if (player != null) {
			JsonObject data = configurationData.getConfigurationData();
			JsonArray permissions = data.getAsJsonArray("required_permissions");
			for (JsonElement permissionObject : permissions) {
				String permission = permissionObject.getAsString();
				if (!player.hasPermission(permission)) {
					String err = Messages.get("usage.miss_needed_permission");
					err = err.replace("{NEEDED_PERM}", permission);
					throw new CommandSignsRequirementException(err);
				}
			}
		}
    }
}
