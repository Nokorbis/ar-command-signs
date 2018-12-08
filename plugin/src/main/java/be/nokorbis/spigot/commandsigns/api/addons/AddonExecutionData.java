package be.nokorbis.spigot.commandsigns.api.addons;

import com.google.gson.JsonObject;


public class AddonExecutionData extends AddonData {

	private final JsonObject executionData;

	public AddonExecutionData(final Addon addon, final JsonObject executionData) {
		super(addon);
		this.executionData = executionData;
	}

	public final JsonObject getExecutionData() {
		return executionData;
	}

}
