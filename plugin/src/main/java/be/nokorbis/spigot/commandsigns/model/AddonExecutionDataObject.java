package be.nokorbis.spigot.commandsigns.model;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;

import java.util.HashMap;
import java.util.Map;


public class AddonExecutionDataObject {
	public long                           id;
	public Map<Addon, AddonExecutionData> addonExecutions = new HashMap<>();
}
