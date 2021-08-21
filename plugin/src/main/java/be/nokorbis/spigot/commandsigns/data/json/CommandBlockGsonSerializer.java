package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.model.BlockActivationMode;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import com.google.gson.*;
import org.bukkit.Location;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by Nokorbis on 22/01/2016.
 */
public class CommandBlockGsonSerializer implements JsonSerializer<CommandBlock>, JsonDeserializer<CommandBlock> {

    private final Set<Addon> addons;

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

            Location loc = jsonContext.deserialize(root.get("location"), Location.class);
            if (loc != null) {
                cmdBlock.setLocation(loc);
            }
            else {
                return null;
            }

            JsonElement disabled = root.get("disabled");
            if (disabled != null) {
                cmdBlock.setDisabled(disabled.getAsBoolean());
            }

            JsonElement activationMode = root.get("activation_mode");
            if (activationMode != null) {
                String mode = activationMode.getAsString();
                cmdBlock.setActivationMode(BlockActivationMode.fromName(mode));
            }



            JsonObject timer = root.getAsJsonObject("timer");
            int duration = timer.getAsJsonPrimitive("duration").getAsInt();
            boolean cancelled = timer.getAsJsonPrimitive("cancelled_on_move").getAsBoolean();
            boolean reset = timer.getAsJsonPrimitive("reset_on_move").getAsBoolean();
            cmdBlock.setTimeBeforeExecution(duration);
            cmdBlock.setCancelledOnMove(cancelled);
            cmdBlock.setResetOnMove(reset);

            JsonArray commands = root.getAsJsonArray("commands");
            for (JsonElement element : commands) {
                cmdBlock.getCommands().add(element.getAsString());
            }

            JsonArray permissions = root.getAsJsonArray("temporarily_granted_permissions");
            for (JsonElement permission : permissions) {
                cmdBlock.getTemporarilyGrantedPermissions().add(permission.getAsString());
            }

            JsonObject jsonAddons = root.getAsJsonObject("addons");
            for (Addon addon : addons) {
                JsonElement addonData = jsonAddons.get(addon.getIdentifier());
                if (addonData != null && !addonData.isJsonNull()) {
                    AddonConfigurationData parsedAddonData = jsonContext.deserialize(addonData, addon.getConfigurationDataClass());
                    cmdBlock.setAddonConfigurationData(addon, parsedAddonData);
                }
            }

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
        root.addProperty("activation_mode", commandBlock.getActivationMode().name());

        Location location = commandBlock.getLocation();
        if (location != null) {
            root.add("location", jsonContext.serialize(location));
        }

        JsonObject timer = new JsonObject();
        timer.addProperty("duration", commandBlock.getTimeBeforeExecution());
        timer.addProperty("cancelled_on_move", commandBlock.isCancelledOnMove());
        timer.addProperty("reset_on_move", commandBlock.isResetOnMove());
        root.add("timer", timer);

        JsonArray commands = new JsonArray();
        for (String command : commandBlock.getCommands()) {
            commands.add(command);
        }
        root.add("commands", commands);

        JsonArray permissions = new JsonArray();
        for (String permission : commandBlock.getTemporarilyGrantedPermissions()) {
            permissions.add(permission);
        }
        root.add("temporarily_granted_permissions", permissions);

        final JsonObject addonData = new JsonObject();
        for (Addon addon : addons) {
            if (addon.getConfigurationDataClass() != null && addon.getConfigurationDataSerializer() != null && addon.getConfigurationDataDeserializer() != null) {
                AddonConfigurationData addonConfigurationData = commandBlock.getAddonConfigurationData(addon);
                addonData.add(addon.getIdentifier(), jsonContext.serialize(addonConfigurationData));
            }
        }

        root.add("addons", addonData);

        return root;
    }
}
