package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import java.util.Objects;


public class PermissionsAddon implements Addon
{
    public final String NAME = "ncs_required_permissions";

    private PermissionsRequirementHandler handler = new PermissionsRequirementHandler();


    @Override
    public final String getName()
    {
        return NAME;
    }

    @Override
    public RequirementHandler getRequirementHandler()
    {
        return handler;
    }

    @Override
    public CostHandler getCostHandler()
    {
        return null;
    }

    @Override
    public JsonObject createConfigurationData() {
		JsonObject root = new JsonObject();
		root.add("required_permissions", new JsonArray());
		return root;
    }

    @Override
    public JsonObject createExecutionData() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Addon that = (Addon) o;
        return Objects.equals(NAME, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(NAME);
    }
}
