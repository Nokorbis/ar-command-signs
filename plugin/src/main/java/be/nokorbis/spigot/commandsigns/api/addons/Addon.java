package be.nokorbis.spigot.commandsigns.api.addons;

import com.google.gson.JsonObject;


public interface Addon
{
    String getName();

    RequirementHandler getRequirementHandler();
    CostHandler getCostHandler();

	/**
	 * Instantiate the base object of the configuration data or your addon. One object will be created for each command sign existing in the server.<br>
	 * If your addon does not use configuration, return null.
	 * @return A Json root object for the base of your command sign data.
	 */
	JsonObject createConfigurationData();

	/**
	 * Instantiate the base object of the execution data for your addon. One object will be created for each command sign existing in the server.<br>
	 * If your addon does not use configuration, return null.
	 * @return A Json root object for the base of your command sign data.
	 */
    JsonObject createExecutionData();
}
