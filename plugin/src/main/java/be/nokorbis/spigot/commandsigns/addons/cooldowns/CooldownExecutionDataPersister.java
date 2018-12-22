package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;


public class CooldownExecutionDataPersister implements JsonSerializer<CooldownExecutionData>, JsonDeserializer<CooldownExecutionData> {

	private final CooldownAddon addon;

	public CooldownExecutionDataPersister(CooldownAddon addon) {
		this.addon = addon;
	}

	@Override
	public CooldownExecutionData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		CooldownExecutionData executionData = addon.createExecutionData();
		if (executionData != null) {
			JsonObject root = json.getAsJsonObject();
			JsonArray usages = root.getAsJsonArray("player_usages");
			for (JsonElement element : usages) {
				JsonObject usage = element.getAsJsonObject();
				String pUuid = usage.get("player_uuid").getAsString();
				long time = usage.get("time").getAsLong();
				executionData.addPlayerUsage(UUID.fromString(pUuid), time);
			}
		}
		return executionData;
	}

	@Override
	public JsonElement serialize(CooldownExecutionData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		JsonArray usages = new JsonArray();
		Map<UUID, Long> playerUsages = src.getPlayerUsages();
		for (Map.Entry<UUID, Long> entry : playerUsages.entrySet()) {
			JsonObject usage = new JsonObject();
			usage.addProperty("player_uuid", entry.getKey().toString());
			usage.addProperty("time", entry.getValue());
			usages.add(usage);
		}

		root.add("player_usages", usages);
		return root;
	}
}
