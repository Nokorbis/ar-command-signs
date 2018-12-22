package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import java.util.Objects;


public class PermissionsAddon extends AddonBase {

    private static final String IDENTIFIER = "ncs_required_permissions";

    private final RequiredPermissionsLifecycleHooker lifecycleHooker = new RequiredPermissionsLifecycleHooker();
    private final PermissionsConfigurationDataTransformer configurationDataTransformer = new PermissionsConfigurationDataTransformer(this);

    public PermissionsAddon() {
        super(IDENTIFIER, "Required permissions");
    }

    @Override
    public AddonLifecycleHooker getLifecycleHooker() {
        return lifecycleHooker;
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
}
