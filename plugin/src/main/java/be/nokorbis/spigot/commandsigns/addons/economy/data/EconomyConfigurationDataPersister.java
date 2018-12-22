package be.nokorbis.spigot.commandsigns.addons.economy.data;

import be.nokorbis.spigot.commandsigns.addons.economy.EconomyAddon;
import com.google.gson.*;

import java.lang.reflect.Type;


public class EconomyConfigurationDataPersister implements JsonSerializer<EconomyConfigurationData>, JsonDeserializer<EconomyConfigurationData> {

	private final EconomyAddon addon;

	public EconomyConfigurationDataPersister(EconomyAddon addon) {
		this.addon = addon;
	}

	@Override
	public EconomyConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		EconomyConfigurationData configuration = addon.createConfigurationData();
		if (configuration != null) {
			JsonObject root = json.getAsJsonObject();
			configuration.setPrice(root.get("price").getAsDouble());
		}
		return configuration;
	}

	@Override
	public JsonElement serialize(EconomyConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = new JsonObject();
		root.addProperty("price", src.getPrice());
		return root;
	}
}
