package be.nokorbis.spigot.commandsigns.addons.permissions;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;


public class PermissionsConfigurationDataTransformer implements JsonSerializer<PermissionsConfigurationData>, JsonDeserializer<PermissionsConfigurationData> {

	private final PermissionsAddon addon;

	public PermissionsConfigurationDataTransformer(PermissionsAddon addon) {
		this.addon = addon;
	}

	@Override
	public PermissionsConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final PermissionsConfigurationData configurationData = addon.createConfigurationData();

		if (configurationData != null) {
			List<String> permissions = configurationData.getRequiredPermissions();

			JsonObject root = json.getAsJsonObject();
			JsonArray requiredPermissions = root.getAsJsonArray("required_permissions");
			for (JsonElement element : requiredPermissions) {
				final String permission = element.getAsString();
				permissions.add(permission);
			}
		}

		return configurationData;
	}

	@Override
	public JsonElement serialize(PermissionsConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		JsonArray permissions = new JsonArray();
		List<String> requiredPermissions = src.getRequiredPermissions();
		for (String permission : requiredPermissions) {
			permissions.add(permission);
		}

		root.add("required_permissions", permissions);
		return root;
	}
}
