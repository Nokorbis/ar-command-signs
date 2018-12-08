package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;

import java.util.Objects;


public class EconomyAddon implements Addon {
	private final String name = "ncs_economy";
	private net.milkbowl.vault.economy.Economy economy = null;

	private EconomyCostHandler handler = null;

	public EconomyAddon(CommandSignsPlugin plugin) {
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.getLogger().info("Plugin vault detected");

			this.economy = plugin.getServer().getServicesManager().load(net.milkbowl.vault.economy.Economy.class);

			if (this.economy != null) {
				this.handler = new EconomyCostHandler(this.economy);
				plugin.getLogger().info("Vault economy linked with command signs ! ");
			}
			else {
				plugin.getLogger().info("No vault economy hooked.");
			}
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public RequirementHandler getRequirementHandler() {
		return null;
	}

	@Override
	public CostHandler getCostHandler() {
		return this.handler;
	}

	@Override
	public JsonObject createConfigurationData() {
		JsonObject root = new JsonObject();
		root.addProperty("price", 0.0);
		return root;
	}

	@Override
	public JsonObject createExecutionData() {
		return null;
	}

	public final boolean isEconomyLinked() {
		return this.economy != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Addon that = (Addon) o;
		return Objects.equals(name, that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
