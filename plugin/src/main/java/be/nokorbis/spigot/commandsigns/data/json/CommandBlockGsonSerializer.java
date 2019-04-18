package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Nokorbis on 22/01/2016.
 */
public class CommandBlockGsonSerializer implements JsonSerializer<CommandBlock>, JsonDeserializer<CommandBlock> {

    private Set<Addon> addons;

    public CommandBlockGsonSerializer(Set<Addon> addons) {
        this.addons = addons;
    }

    @Override
    public CommandBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();

        try {
            long id = root.get("id").getAsLong();
            CommandBlock cmdBlock = new CommandBlock(id);
            JsonElement name = root.get("name");
            if (name != null) {
                cmdBlock.setName(name.getAsString());
            }

            JsonElement disabled = root.get("disabled");
            if (disabled != null) {
                cmdBlock.setDisabled(disabled.getAsBoolean());
            }

            Location loc = jsonContext.deserialize(root.get("location"), Location.class);
            if (loc != null) {
                cmdBlock.setLocation(loc);
            }

            JsonObject timer = root.getAsJsonObject("timer");
            int duration = timer.getAsJsonPrimitive("duration").getAsInt();
            boolean cancelled = timer.getAsJsonPrimitive("cancelled_on_move").getAsBoolean();
            boolean reset = timer.getAsJsonPrimitive("reset_on_move").getAsBoolean();
            cmdBlock.setTimeBeforeExecution(duration);
            cmdBlock.setCancelledOnMove(cancelled);
            cmdBlock.setResetOnMove(reset);

            return cmdBlock;
        }
        catch (Exception ex) {
            return null;
        }
    }

    @Override
    public JsonElement serialize(CommandBlock commandBlock, Type type, final JsonSerializationContext jsonContext) {
        JsonObject root = new JsonObject();

        root.addProperty("id", commandBlock.getId());
        if (commandBlock.getName() != null) {
            root.addProperty("name", commandBlock.getName());
        }
        root.addProperty("disabled", commandBlock.isDisabled());

        Location location = commandBlock.getLocation();
        if (location != null) {
            root.add("location", jsonContext.serialize(location));
        }


        JsonObject timer = new JsonObject();
        timer.addProperty("duration", commandBlock.getTimeBeforeExecution());
        timer.addProperty("cancelled_on_move", commandBlock.isCancelledOnMove());
        timer.addProperty("reset_on_move", commandBlock.isResetOnMove());
        root.add("timer", timer);

        final JsonObject addonData = new JsonObject();
        commandBlock.forEachAddonConfiguration((addon, configuration) -> {
            if (addon.getConfigurationDataSerializer() != null && addon.getConfigurationDataDeserializer() != null) {
                addonData.add(addon.getIdentifier(), jsonContext.serialize(configuration));
            }
        });

        root.add("addons", addonData);

        return root;
    }
}
