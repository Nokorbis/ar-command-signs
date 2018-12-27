package be.nokorbis.spigot.commandsigns.addons.commands.data;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import com.google.gson.*;

import java.lang.reflect.Type;


public class CommandsConfigurationDataPersister implements JsonSerializer<CommandsConfigurationData>, JsonDeserializer<CommandsConfigurationData> {

	private CommandsAddon addon;

	public CommandsConfigurationDataPersister(CommandsAddon addon) {
		this.addon = addon;
	}

	@Override
	public CommandsConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		CommandsConfigurationData configurationData = addon.createConfigurationData();
		if (configurationData != null) {
			JsonObject root = json.getAsJsonObject();

			JsonArray commands = root.getAsJsonArray("commands");
			for (JsonElement cmdElement : commands) {
				configurationData.getCommands().add(cmdElement.getAsString());
			}

			JsonArray permissions = root.getAsJsonArray("temporary_permissions");
			for (JsonElement cmdElement : permissions) {
				configurationData.getTemporarilyGrantedPermissions().add(cmdElement.getAsString());
			}


			configurationData.preprocessTotalDelay();
		}
		return configurationData;
	}

	@Override
	public JsonElement serialize(CommandsConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		JsonArray jsonCommands = new JsonArray();
		for (String command : src.getCommands()) {
			jsonCommands.add(command);
		}

		JsonArray jsonPermissions = new JsonArray();
		for (String grantedPermission : src.getTemporarilyGrantedPermissions()) {
			jsonPermissions.add(grantedPermission);
		}

		root.add("commands", jsonCommands);
		root.add("temporary_permissions", jsonPermissions);

		return root;
	}

}
