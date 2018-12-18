package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import java.util.Objects;


public class PermissionsAddon implements Addon
{
    private final String IDENTIFIER = "ncs_required_permissions";

    private PermissionsRequirementHandler handler = new PermissionsRequirementHandler();

    private final PermissionsConfigurationDataTransformer configurationDataTransformer = new PermissionsConfigurationDataTransformer(this);

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public final String getName()
    {
        return "Required permissions";
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
    public PermissionsConfigurationData createConfigurationData() {
		return new PermissionsConfigurationData(this);
    }

    @Override
    public AddonExecutionData createExecutionData() {
        return null;
    }

    @Override
    public AddonExecutionDataUpdater getAddonExecutionDataUpdater() {
        return null;
    }

    @Override
    public JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer() {
        return null;
    }

    @Override
    public JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer() {
        return null;
    }

    @Override
    public PermissionsConfigurationDataTransformer getConfigurationDataSerializer() {
        return configurationDataTransformer;
    }

    @Override
    public PermissionsConfigurationDataTransformer getConfigurationDataDeserializer() {
        return configurationDataTransformer;
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
        return Objects.equals(IDENTIFIER, that.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(IDENTIFIER);
    }
}
