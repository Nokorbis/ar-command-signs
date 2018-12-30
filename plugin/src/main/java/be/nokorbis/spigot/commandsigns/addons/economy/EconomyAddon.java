package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.economy.menus.MenuEconomy;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public class EconomyAddon extends AddonBase {
	private static final String IDENTIFIER = "ncs_economy";

	private final net.milkbowl.vault.economy.Economy economy;

	private EconomyLifecycleHooker lifecycleHooker = null;
	private MenuEconomy editionMenu;

	private final EconomyConfigurationDataPersister configurationDataTransformer = new EconomyConfigurationDataPersister(this);

	public EconomyAddon(CommandSignsPlugin plugin) {
		super(plugin, IDENTIFIER, "Economy");

		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.getLogger().info("Plugin vault detected");

			this.economy = plugin.getServer().getServicesManager().load(net.milkbowl.vault.economy.Economy.class);

			if (this.economy != null) {
				this.lifecycleHooker = new EconomyLifecycleHooker(this, economy);
				this.editionMenu = new MenuEconomy(this, economy);
				plugin.getLogger().info("Vault economy linked with command signs ! ");
			}
			else {
				plugin.getLogger().info("No vault economy hooked.");
			}
		}
		else {
			this.economy = null;
		}
	}

	@Override
	public final boolean shouldAddonBeHooked() {
		return isEconomyLinked() && this.lifecycleHooker != null;
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return lifecycleHooker;
	}

	@Override
	public AddonSubmenuHolder getSubmenus() {
		AddonSubmenuHolder holder = null;
		if (economy != null && editionMenu != null) {
			holder = new AddonSubmenuHolder();
			holder.costSubmenus.add(editionMenu);
		}
		return holder;
	}

	@Override
	public EconomyConfigurationData createConfigurationData() {
		return new EconomyConfigurationData(this);
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
	public EconomyConfigurationDataPersister getConfigurationDataSerializer() {
		return configurationDataTransformer;
	}

	@Override
	public EconomyConfigurationDataPersister getConfigurationDataDeserializer() {
		return configurationDataTransformer;
	}

	public final boolean isEconomyLinked() {
		return this.economy != null;
	}

}
