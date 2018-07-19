package be.nokorbis.spigot.commandsigns.api.addons;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public interface Addon
{
    String getName();

    RequirementHandler getRequirementHandler();
    CostHandler getCostHandler();

    JsonSerializer<? extends AddonConfigurationData> getConfigurationDataSerializer();
    JsonDeserializer<? extends AddonConfigurationData> getConfigurationDataDeserializer();

    JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer();
    JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer();
}
