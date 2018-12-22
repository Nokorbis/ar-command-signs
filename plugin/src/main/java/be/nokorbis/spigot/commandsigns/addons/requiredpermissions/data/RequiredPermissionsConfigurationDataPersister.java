package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;


public class RequiredPermissionsConfigurationDataPersister implements JsonSerializer<RequiredPermissionsConfigurationData>, JsonDeserializer<RequiredPermissionsConfigurationData> {

	private final RequiredPermissionsAddon addon;

	public RequiredPermissionsConfigurationDataPersister(RequiredPermissionsAddon addon) {
		this.addon = addon;
	}

	@Override
	public RequiredPermissionsConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		final RequiredPermissionsConfigurationData configurationData = addon.createConfigurationData();

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
	public JsonElement serialize(RequiredPermissionsConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
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
