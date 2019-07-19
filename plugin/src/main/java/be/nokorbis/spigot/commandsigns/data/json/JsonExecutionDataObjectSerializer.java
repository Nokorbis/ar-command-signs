package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.model.AddonExecutionDataObject;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;


public class JsonExecutionDataObjectSerializer implements JsonSerializer<AddonExecutionDataObject>, JsonDeserializer<AddonExecutionDataObject> {

	private Set<Addon> addons;

	public JsonExecutionDataObjectSerializer(Set<Addon> addons) {
		this.addons = addons;
	}


	@Override
	public AddonExecutionDataObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		AddonExecutionDataObject object = new AddonExecutionDataObject();

		JsonObject root = json.getAsJsonObject();
		object.id = root.get("id").getAsLong();
		JsonObject jsonAddons = root.get("addons").getAsJsonObject();
		for (Addon addon : this.addons) {
			JsonElement addonElement = jsonAddons.get(addon.getIdentifier());
			if (addonElement != null && !addonElement.isJsonNull()) {
				AddonExecutionData execData = context.deserialize(addonElement, addon.getExecutionDataClass());
				if (execData != null) {
					object.addonExecutions.put(addon, execData);
				}
			}
		}

		return object;
	}

	@Override
	public JsonElement serialize(AddonExecutionDataObject src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();

		root.addProperty("id", src.id);
		JsonObject addonsArray = new JsonObject();

		for (Map.Entry<Addon, AddonExecutionData> entry : src.addonExecutions.entrySet()) {
			Addon addon = entry.getKey();
			if (addon.getExecutionDataSerializer() != null) {
				addonsArray.add(addon.getIdentifier(), context.serialize(entry.getValue()));
			}
		}

		root.add("addons", addonsArray);

		return root;
	}
}
