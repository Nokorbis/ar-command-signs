package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Created by Nokorbis on 22/01/2016.
 */
public class CommandBlockGsonSerializer implements JsonSerializer<CommandBlock>, JsonDeserializer<CommandBlock>{


    @Override
    public CommandBlock deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject root = jsonElement.getAsJsonObject();

        try
        {
            long id = root.get("id").getAsLong();
            CommandBlock cmdBlock = new CommandBlock(id);
            JsonElement name = root.get("name");
            if (name != null)
            {
                cmdBlock.setName(name.getAsString());
            }

            JsonElement loc = root.get("world");
            if (loc != null)
            {
                World world = Bukkit.getWorld(loc.getAsString());
                if (world == null)
                {
                    UUID worldUuid = UUID.fromString(loc.getAsString());
                    world = Bukkit.getWorld(worldUuid);
                }
                int x = root.get("x").getAsInt();
                int y = root.get("y").getAsInt();
                int z = root.get("z").getAsInt();
                Location location = new Location(world, x, y, z);
                cmdBlock.setLocation(location);
            }

            JsonElement disabled = root.get("disabled");
            if (disabled != null)
            {
                cmdBlock.setDisabled(disabled.getAsBoolean());
            }

            cmdBlock.setEconomyPrice(root.get("price").getAsDouble());

            cmdBlock.setTimeBeforeExecution(root.get("time_before_execution").getAsInt());
            cmdBlock.setCancelledOnMove(root.get("move_cancel_timer").getAsBoolean());
            cmdBlock.setResetOnMove(root.get("move_reset_timer").getAsBoolean());

            cmdBlock.setTimeBetweenUsage(root.get("global_time_between_usages").getAsInt());
            cmdBlock.setTimeBetweenPlayerUsage(root.get("player_time_between_usages").getAsInt());

            JsonArray commands = root.get("commands").getAsJsonArray();
            for (JsonElement command : commands)
            {
                cmdBlock.addCommand(command.getAsString());
            }

            JsonArray neededPerms = root.get("needed_permissions").getAsJsonArray();
            for (JsonElement perm : neededPerms)
            {
                cmdBlock.addNeededPermission(perm.getAsString());
            }

            JsonArray permissions = root.get("temporary_permissions").getAsJsonArray();
            for (JsonElement permission : permissions)
            {
                cmdBlock.addPermission(permission.getAsString());
            }

            return cmdBlock;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    @Override
    public JsonElement serialize(CommandBlock commandBlock, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject root = new JsonObject();
        root.add("id", new JsonPrimitive(commandBlock.getId()));
        if (commandBlock.getName() != null)
        {
            root.addProperty("name", commandBlock.getName());
        }

        //Save location
        if (commandBlock.getLocation() != null)
        {
            root.addProperty("world", commandBlock.getLocation().getWorld().getName());
            root.addProperty("x", commandBlock.getLocation().getBlockX());
            root.addProperty("y", commandBlock.getLocation().getBlockY());
            root.addProperty("z", commandBlock.getLocation().getBlockZ());
        }

        root.addProperty("disabled", commandBlock.isDisabled());

        //Save commands
        JsonArray jsonCmds = new JsonArray();
        for (String cmd : commandBlock.getCommands())
        {
            jsonCmds.add(new JsonPrimitive(cmd));
        }
        root.add("commands", jsonCmds);

        //Save needed permissions
        JsonArray jsonNeededPerms = new JsonArray();
        for (String perm : commandBlock.getNeededPermissions())
        {
            jsonNeededPerms.add(new JsonPrimitive(perm));
        }
        root.add("needed_permissions", jsonNeededPerms);

        //Save temporary permissions
        JsonArray jsonTempPerms = new JsonArray();
        for (String perm : commandBlock.getPermissions())
        {
            jsonTempPerms.add(new JsonPrimitive(perm));
        }
        root.add("temporary_permissions", jsonTempPerms);

        root.addProperty("price", commandBlock.getEconomyPrice());

        root.addProperty("time_before_execution", commandBlock.getTimeBeforeExecution());
        root.addProperty("move_cancel_timer", commandBlock.isCancelledOnMove());
        root.addProperty("move_reset_timer", commandBlock.isResetOnMove());

        root.addProperty("global_time_between_usages", commandBlock.getTimeBetweenUsage());
        root.addProperty("player_time_between_usages", commandBlock.getTimeBetweenPlayerUsage());
        return root;
    }
}
