package be.nokorbis.spigot.commandsigns.addons.cooldowns.data;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownAddon;
import com.google.gson.*;

import java.lang.reflect.Type;


public class CooldownConfigurationDataPersister implements JsonDeserializer<CooldownConfigurationData>, JsonSerializer<CooldownConfigurationData> {

	private final CooldownAddon addon;

	public CooldownConfigurationDataPersister(CooldownAddon addon) {
		this.addon = addon;
	}

	@Override
	public CooldownConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		CooldownConfigurationData data = addon.createConfigurationData();

		if (data != null) {
			JsonObject root = json.getAsJsonObject();
			data.setGlobalCooldown(root.get("global_cooldown").getAsLong());
			data.setPlayerCooldown(root.get("player_cooldown").getAsLong());

			JsonElement isGlobalOnce = root.get("is_global_once");
			if (isGlobalOnce != null && !isGlobalOnce.isJsonNull()) {
				data.setGlobalOnlyOnce(isGlobalOnce.getAsBoolean());
			}

			JsonElement isPlayerOnce = root.get("is_player_once");
			if (isPlayerOnce != null && !isPlayerOnce.isJsonNull()) {
				data.setPlayerOnlyOnce(isPlayerOnce.getAsBoolean());
			}
		}

		return data;
	}

	@Override
	public JsonElement serialize(CooldownConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		if (src != null) {
			root.addProperty("global_cooldown", src.getGlobalCooldown());
			root.addProperty("player_cooldown", src.getPlayerCooldown());
			root.addProperty("is_global_once", src.isGlobalOnlyOnce());
			root.addProperty("is_player_once", src.isPlayerOnlyOnce());
		}

		return root;
	}
}
