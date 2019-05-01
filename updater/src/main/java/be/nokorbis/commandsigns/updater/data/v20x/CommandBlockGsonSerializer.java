package be.nokorbis.commandsigns.updater.data.v20x;

import be.nokorbis.commandsigns.updater.models.CommandBlock;
import be.nokorbis.commandsigns.updater.models.Location;
import com.google.gson.*;

import java.lang.reflect.Type;


public class CommandBlockGsonSerializer implements JsonSerializer<CommandBlock> {
	@Override
	public JsonElement serialize(CommandBlock commandBlock, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject root = new JsonObject();

		root.addProperty("id", commandBlock.id);
		if (commandBlock.name != null) {
			root.addProperty("name", commandBlock.name);
		}
		root.addProperty("disabled", commandBlock.disabled);
		root.addProperty("activation_mode", "BOTH");

		Location location = commandBlock.location;
		if (location != null) {
			root.add("location", serialize(location));
		}

		JsonObject timer = new JsonObject();
		timer.addProperty("duration", commandBlock.timeBeforeExecution);
		timer.addProperty("cancelled_on_move", commandBlock.cancelledOnMove);
		timer.addProperty("reset_on_move", commandBlock.resetOnMove);
		root.add("timer", timer);

		JsonArray commands = new JsonArray();
		for (String command : commandBlock.commands) {
			commands.add(command);
		}
		root.add("commands", commands);

		JsonArray permissions = new JsonArray();
		for (String permission : commandBlock.permissions) {
			permissions.add(permission);
		}
		root.add("temporarily_granted_permissions", permissions);

		final JsonObject addonData = new JsonObject();

		JsonObject economy = new JsonObject();
		economy.addProperty("price", commandBlock.economyPrice);
		addonData.add("ncs_economy", economy);

		JsonObject requiredPermissions = new JsonObject();
		permissions = new JsonArray();
		for (String neededPermission : commandBlock.neededPermissions) {
			permissions.add(neededPermission);
		}
		requiredPermissions.add("required_permissions", permissions);
		addonData.add("ncs_required_permissions", requiredPermissions);

		JsonObject cooldowns = new JsonObject();
		cooldowns.addProperty("is_global_once", false);
		cooldowns.addProperty("is_player_once", false);
		cooldowns.addProperty("global_cooldown", commandBlock.timeBetweenUsage);
		cooldowns.addProperty("player_cooldown", commandBlock.timeBetweenPlayerUsage);
		addonData.add("ncs_cooldowns", cooldowns);

		root.add("addons", addonData);

		return root;
	}

	private JsonObject serialize(Location location) {
		JsonObject root = new JsonObject();
		root.addProperty("world", location.world);
		root.addProperty("x", location.x);
		root.addProperty("y", location.y);
		root.addProperty("z", location.z);

		return root;
	}
}
