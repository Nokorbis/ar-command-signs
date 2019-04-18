package be.nokorbis.spigot.commandsigns.data.json;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;


public class JsonLocationPersister implements JsonSerializer<Location>, JsonDeserializer<Location> {

	@Override
	public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Location location = null;
		if (json != null) {
			JsonObject root = json.getAsJsonObject();
			World world = getWorldFromString(root.getAsJsonPrimitive("world").getAsString());

			if (world != null) {
				double x = root.getAsJsonPrimitive("x").getAsDouble();
				double y = root.getAsJsonPrimitive("y").getAsDouble();
				double z = root.getAsJsonPrimitive("z").getAsDouble();
				location = new Location(world, x, y, z);
			}

		}
		return location;
	}

	private World getWorldFromString(String worldName) {
		try {
			World world = Bukkit.getWorld(worldName);
			if (world == null) {
				UUID worldUuid = UUID.fromString(worldName);
				world = Bukkit.getWorld(worldUuid);
			}
			return world;
		}
		catch (IllegalArgumentException e) {
			return null;
		}
	}

	@Override
	public JsonElement serialize(Location loc, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject root = null;

		if (loc != null) {
			root = new JsonObject();
			root.addProperty("x", loc.getX());
			root.addProperty("y", loc.getY());
			root.addProperty("z", loc.getZ());
			root.addProperty("world", loc.getWorld().getName());
		}

		return root;
	}
}
