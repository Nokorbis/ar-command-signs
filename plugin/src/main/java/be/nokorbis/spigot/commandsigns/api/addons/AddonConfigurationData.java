package be.nokorbis.spigot.commandsigns.api.addons;

import com.google.gson.JsonObject;


public class AddonConfigurationData extends AddonData{

	private JsonObject configurationData;

	public AddonConfigurationData(final Addon addon, final JsonObject configurationData) {
		super(addon);
		this.configurationData = configurationData;
	}

	public final JsonObject getConfigurationData() {
		return configurationData;
	}
}
