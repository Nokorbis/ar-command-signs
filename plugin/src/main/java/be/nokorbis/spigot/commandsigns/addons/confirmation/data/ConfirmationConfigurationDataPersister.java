package be.nokorbis.spigot.commandsigns.addons.confirmation.data;

import be.nokorbis.spigot.commandsigns.addons.confirmation.ConfirmationAddon;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfirmationConfigurationDataPersister implements JsonSerializer<ConfirmationConfigurationData>, JsonDeserializer<ConfirmationConfigurationData> {

    private ConfirmationAddon addon;

    public ConfirmationConfigurationDataPersister(ConfirmationAddon addon) {
        this.addon = addon;
    }

    @Override
    public ConfirmationConfigurationData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ConfirmationConfigurationData data = this.addon.createConfigurationData();

        if (data != null) {
            JsonObject root = json.getAsJsonObject();
            data.setRequireConfirmation(root.get("require_confirmation").getAsBoolean());
        }

        return data;
    }

    @Override
    public JsonElement serialize(ConfirmationConfigurationData src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();

        if (src != null) {
            root.addProperty("require_confirmation", src.isRequireConfirmation());
        }

        return root;
    }
}
