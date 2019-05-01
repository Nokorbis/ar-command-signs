package be.nokorbis.commandsigns.updater.data.v16x;

import be.nokorbis.commandsigns.updater.models.CommandBlock;
import be.nokorbis.commandsigns.updater.models.Location;
import com.google.gson.*;

import java.lang.reflect.Type;


public class CommandBlockGsonDeserializer implements JsonDeserializer<CommandBlock>{


    @Override
    public CommandBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject root = jsonElement.getAsJsonObject();

        try {
            CommandBlock cmdBlock = new CommandBlock();
            cmdBlock.id = root.get("id").getAsLong();
            JsonElement name = root.get("name");
            if (name != null) {
                cmdBlock.name = name.getAsString();
            }

            Location location = new Location();
            location.world = root.get("world").getAsString();
            location.x = root.get("x").getAsInt();
            location.y = root.get("y").getAsInt();
            location.z = root.get("z").getAsInt();

            cmdBlock.location = location;

            JsonElement disabled = root.get("disabled");
            if (disabled != null) {
                cmdBlock.disabled = (disabled.getAsBoolean());
            }

            JsonElement price = root.get("price");
            if (price != null && !price.isJsonNull()) {
                cmdBlock.economyPrice = price.getAsDouble();
            }

            JsonElement tbe = root.get("time_before_execution");
            if (tbe != null && !tbe.isJsonNull()) {
                cmdBlock.timeBeforeExecution = tbe.getAsInt();
            }
            JsonElement cancel = root.get("move_cancel_timer");
            if (cancel != null && !cancel.isJsonNull()) {
                cmdBlock.cancelledOnMove = (cancel.getAsBoolean());
            }
            JsonElement reset = root.get("move_reset_timer");
            if (reset != null && !reset.isJsonNull()) {
                cmdBlock.resetOnMove = reset.getAsBoolean();
            }

            JsonElement globalCooldown = root.get("global_time_between_usages");
            if (globalCooldown != null && !globalCooldown.isJsonNull()) {
                cmdBlock.timeBetweenUsage = (globalCooldown.getAsInt());
            }
            JsonElement playerCooldown = root.get("player_time_between_usages");
            if (playerCooldown != null && !playerCooldown.isJsonNull()) {
                cmdBlock.timeBetweenPlayerUsage = playerCooldown.getAsInt();
            }

            JsonArray commands = root.get("commands").getAsJsonArray();
            for (JsonElement command : commands) {
                cmdBlock.commands.add(command.getAsString());
            }

            JsonArray neededPerms = root.get("needed_permissions").getAsJsonArray();
            for (JsonElement perm : neededPerms) {
                cmdBlock.neededPermissions.add(perm.getAsString());
            }

            JsonArray permissions = root.get("temporary_permissions").getAsJsonArray();
            for (JsonElement permission : permissions) {
                cmdBlock.permissions.add(permission.getAsString());
            }

            return cmdBlock;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
