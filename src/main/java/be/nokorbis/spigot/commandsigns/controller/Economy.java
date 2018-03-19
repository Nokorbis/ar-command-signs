package be.nokorbis.spigot.commandsigns.controller;

import org.bukkit.plugin.RegisteredServiceProvider;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;


public class Economy {

	private static net.milkbowl.vault.economy.Economy economy = null;

	public static void initialize() {
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			plugin.getLogger().info("Plugin vault detected");
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (rsp != null) {
				economy = rsp.getProvider();
				if (economy != null) {
					plugin.getLogger().info("Vault economy linked with command signs ! ");
				}
			}
			else {
				plugin.getLogger().info("No vault economy hooked.");
			}
		}
	}

	public static net.milkbowl.vault.economy.Economy getEconomy() {
		return economy;
	}

}
