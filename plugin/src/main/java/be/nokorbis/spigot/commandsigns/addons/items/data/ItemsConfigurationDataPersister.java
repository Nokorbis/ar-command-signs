package be.nokorbis.spigot.commandsigns.addons.items.data;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ItemsConfigurationDataPersister implements JsonSerializer<ItemsConfigurationData>, JsonDeserializer<ItemsConfigurationData> {

    @Override
    public ItemsConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(ItemsConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();

        JsonArray requirements = new JsonArray();
        JsonArray costs = new JsonArray();

        root.add("required_items", requirements);
        root.add("costs_items", costs);

        return root;
    }

}
