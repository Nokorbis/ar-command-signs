package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.data.Pair;
import com.google.gson.*;
import org.bukkit.Location;
import java.lang.reflect.Type;


public class JsonCommandBlockLocationExtractor implements JsonDeserializer<Pair<Location, Long>> {

	@Override
	public Pair<Location, Long> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext gson) throws JsonParseException {
		if (json != null) {
			Location location;
			JsonObject jsonRoot = json.getAsJsonObject();
			JsonElement locationElement = jsonRoot.get("location");
			if (locationElement == null || locationElement.isJsonNull()) {
				return null;
			}

			location = gson.deserialize(locationElement, Location.class);

			if (location == null) {
				return null;
			}

			long id = jsonRoot.get("id").getAsLong();
			return new Pair<>(location, id);
		}
		return null;
	}
}
