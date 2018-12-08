package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class CooldownAddon implements Addon {

	private final String name = "ncs_cooldowns";

	private CooldownRequirementHandler handler = new CooldownRequirementHandler();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public RequirementHandler getRequirementHandler() {
		return handler;
	}

	@Override
	public CostHandler getCostHandler() {
		return null;
	}

	@Override
	public JsonObject createConfigurationData() {
		JsonObject root = new JsonObject();
		root.addProperty("global_cooldown", 0L);
		root.addProperty("player_cooldown", 0L);
		return root;
	}

	@Override
	public JsonObject createExecutionData() {
		JsonObject root = new JsonObject();
		root.add("usages", new JsonArray());
		return root;
	}
}
