package be.nokorbis.spigot.commandsigns.api.addons;


import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import org.bukkit.plugin.Plugin;

import java.util.Map;


public interface Addon {
	String getIdentifier();
    String getName();
    Plugin getPlugin();

    boolean shouldAddonBeHooked();
    AddonLifecycleHooker getLifecycleHooker();

    AddonSubmenuHolder getSubmenus();

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

    void onEnable();

    Class<? extends AddonExecutionData> getExecutionDataClass();
    JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer();
    JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer();

    Class<? extends AddonConfigurationData> getConfigurationDataClass();
	JsonSerializer<? extends AddonConfigurationData> getConfigurationDataSerializer();
	JsonDeserializer<? extends AddonConfigurationData> getConfigurationDataDeserializer();

	Map<String, AddonConfigurationDataEditor> getDataEditors();
}
