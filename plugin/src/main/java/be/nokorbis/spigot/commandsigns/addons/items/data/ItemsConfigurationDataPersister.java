package be.nokorbis.spigot.commandsigns.addons.items.data;

import be.nokorbis.spigot.commandsigns.addons.items.ItemsAddon;
import com.google.gson.*;
import org.bukkit.Material;

import java.lang.reflect.Type;
import java.util.List;

public class ItemsConfigurationDataPersister implements JsonSerializer<ItemsConfigurationData>, JsonDeserializer<ItemsConfigurationData> {

    private final ItemsAddon addon;

    public ItemsConfigurationDataPersister(ItemsAddon addon) {
        this.addon = addon;
    }

    @Override
    public ItemsConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ItemsConfigurationData data = addon.createConfigurationData();

        JsonObject root = json.getAsJsonObject();

        JsonArray requiredItems = root.getAsJsonArray("required_items");
        for (JsonElement requiredItem : requiredItems) {
            JsonObject jsItem = requiredItem.getAsJsonObject();
            NCSItem item = deserializeItem(jsItem);
            if (item == null) continue;

            data.getRequirementNCSItems().add(item);
        }

        JsonArray costsItems = root.getAsJsonArray("costs_items");
        for (JsonElement costsItem : costsItems) {
            JsonObject jsItem = costsItem.getAsJsonObject();
            NCSItem item = deserializeItem(jsItem);
            if (item == null) continue;

            data.getCostsNCSItems().add(item);
        }

        return data;
    }

    private NCSItem deserializeItem(JsonObject jsItem) {
        String type = jsItem.getAsJsonPrimitive("type").getAsString();
        Material material = Material.getMaterial(type);
        if (material == null) {
            return null;
        }
        NCSItem item = new NCSItem(material);

        JsonElement jsQuantity = jsItem.get("quantity");
        if (jsQuantity != null && !jsQuantity.isJsonNull()) {
            int quantity = jsQuantity.getAsInt();
            item.setQuantity(quantity);
        }

        JsonElement jsHandOnly = jsItem.get("hand_only");
        if (jsHandOnly != null && !jsHandOnly.isJsonNull()) {
            boolean handOnly = jsHandOnly.getAsBoolean();
            item.setHandOnly(handOnly);
        }

        JsonElement jsNames = jsItem.get("names");
        if (jsNames != null && !jsNames.isJsonNull()) {
            JsonArray names = jsNames.getAsJsonArray();
            JsonElement name = names.get(0);
            if (name != null && !name.isJsonNull()) {
                item.setName(name.getAsString());
            }
        }

        JsonElement jsLores = jsItem.get("lores");
        if (jsLores != null && !jsLores.isJsonNull()) {
            JsonArray lores = jsLores.getAsJsonArray();
            for (JsonElement jsLore : lores) {
                String lore = jsLore.getAsString();
                item.getLore().add(lore);
            }
        }

        return item;
    }

    @Override
    public JsonElement serialize(ItemsConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();

        JsonArray requirements = new JsonArray();
        JsonArray costs = new JsonArray();

        List<NCSItem> requiredItems = src.getRequirementNCSItems();
        for (NCSItem item : requiredItems) {
            JsonObject jsItem = serializeItem(item);
            requirements.add(jsItem);
        }

        List<NCSItem> costsItems = src.getCostsNCSItems();
        for (NCSItem item : costsItems) {
            JsonObject jsItem = serializeItem(item);
            costs.add(jsItem);
        }

        root.add("required_items", requirements);
        root.add("costs_items", costs);

        return root;
    }

    private JsonObject serializeItem(NCSItem item) {
        JsonObject jsItem = new JsonObject();

        jsItem.addProperty("type", item.getType().name());
        jsItem.addProperty("quantity", item.getQuantity());
        jsItem.addProperty("hand_only", item.isHandOnly());

        JsonArray names = new JsonArray();
        names.add(item.getName());
        jsItem.add("names", names);

        JsonArray lores = new JsonArray();
        for (String lore : item.getLore()) {
            lores.add(lore);
        }
        jsItem.add("lores", lores);
        return jsItem;
    }

}
