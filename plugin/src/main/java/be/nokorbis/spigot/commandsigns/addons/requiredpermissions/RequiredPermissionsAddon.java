package be.nokorbis.spigot.commandsigns.addons.requiredpermissions;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public class RequiredPermissionsAddon extends AddonBase {

    private static final String IDENTIFIER = "ncs_required_permissions";

    private final RequiredPermissionsLifecycleHooker            lifecycleHooker              = new RequiredPermissionsLifecycleHooker();
    private final RequiredPermissionsConfigurationDataPersister configurationDataTransformer = new RequiredPermissionsConfigurationDataPersister(this);

    public RequiredPermissionsAddon() {
        super(IDENTIFIER, "Required requiredpermissions");
    }

    @Override
    public AddonLifecycleHooker getLifecycleHooker() {
        return lifecycleHooker;
    }

    @Override
    public RequiredPermissionsConfigurationData createConfigurationData() {
		return new RequiredPermissionsConfigurationData(this);
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
    public RequiredPermissionsConfigurationDataPersister getConfigurationDataSerializer() {
        return configurationDataTransformer;
    }

    @Override
    public RequiredPermissionsConfigurationDataPersister getConfigurationDataDeserializer() {
        return configurationDataTransformer;
    }
}
