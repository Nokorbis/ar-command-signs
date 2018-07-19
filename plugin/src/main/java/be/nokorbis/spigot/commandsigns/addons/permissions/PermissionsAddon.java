package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public class PermissionsAddon implements Addon
{
    public static final String NAME = "ncs_required_permissions";

    private PermissionsRequirementHandler handler;

    public PermissionsAddon()
    {
        this.handler = new PermissionsRequirementHandler();
    }

    @Override
    public String getName()
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
    public JsonSerializer<? extends AddonConfigurationData> getConfigurationDataSerializer()
    {
        return null;
    }

    @Override
    public JsonDeserializer<? extends AddonConfigurationData> getConfigurationDataDeserializer()
    {
        return null;
    }

    @Override
    public JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer()
    {
        return null;
    }

    @Override
    public JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer()
    {
        return null;
    }
}
