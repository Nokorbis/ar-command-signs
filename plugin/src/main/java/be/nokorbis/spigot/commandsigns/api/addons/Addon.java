package be.nokorbis.spigot.commandsigns.api.addons;


import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public interface Addon
{
	String getIdentifier();
    String getName();

    boolean shouldAddonBeHooked();

    AddonLifecycleHooker getLifecycleHooker();

	/**
	 * Instantiate the base object of the configuration data or your addon. One object will be created for each command sign existing in the server.<br>
	 * If your addon does not use configuration, return null.
	 * @return A Json root object for the base of your command sign data.
	 */
	AddonConfigurationData createConfigurationData();

	/**
	 * Instantiate the base object of the execution data for your addon. One object will be created for each command sign existing in the server.<br>
	 * If your addon does not use configuration, return null.
	 * @return A Json root object for the base of your command sign data.
	 */
    AddonExecutionData createExecutionData();

    JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer();
    JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer();

	JsonSerializer<? extends AddonConfigurationData> getConfigurationDataSerializer();
	JsonDeserializer<? extends AddonConfigurationData> getConfigurationDataDeserializer();
}
