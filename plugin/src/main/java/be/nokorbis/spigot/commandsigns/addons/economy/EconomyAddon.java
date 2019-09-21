package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyDataEditor;
import be.nokorbis.spigot.commandsigns.addons.economy.menus.MenuEconomy;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;

import java.util.HashMap;
import java.util.Map;


public class EconomyAddon extends AddonBase {
	private static final String IDENTIFIER = "ncs_economy";

	private net.milkbowl.vault.economy.Economy economy;

	private EconomyLifecycleHooker lifecycleHooker = null;
	private MenuEconomy editionMenu;

	private final EconomyConfigurationDataPersister configurationDataTransformer = new EconomyConfigurationDataPersister(this);

	public EconomyAddon(CommandSignsPlugin plugin) {
		super(plugin, IDENTIFIER, "Economy");
	}

	@Override
	public void onEnable() {
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.getLogger().info("Plugin vault detected");

			this.economy = plugin.getServer().getServicesManager().load(net.milkbowl.vault.economy.Economy.class);

			if (this.economy != null) {
				this.lifecycleHooker = new EconomyLifecycleHooker(this, economy);
				this.editionMenu = new MenuEconomy(this, economy);
				plugin.getLogger().info(String.format("Vault economy [%s] linked with command signs !", economy.getName()));
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
	public Class<? extends AddonConfigurationData> getConfigurationDataClass() {
		return EconomyConfigurationData.class;
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

	@Override
	public Map<String, AddonConfigurationDataEditor> getDataEditors() {
		if (isEconomyLinked()) {
			HashMap<String, AddonConfigurationDataEditor> editors = new HashMap<>();
			editors.put("ncs.economy", new EconomyDataEditor(this));
			return editors;
		}
		return null;
	}
}
