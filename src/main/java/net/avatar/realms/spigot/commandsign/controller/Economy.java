package net.avatar.realms.spigot.commandsign.controller;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.avatar.realms.spigot.commandsign.CommandSign;


public class Economy {

	private static net.milkbowl.vault.economy.Economy economy = null;

	public static void initialize() {
		CommandSign plugin = CommandSign.getPlugin();
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<net.milkbowl.vault.economy.Economy> rsp = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if (rsp != null) {
				economy = rsp.getProvider();
				if (economy != null) {
					plugin.getLogger().info("Vault economy detected for command signs ! ");
				}
			}
		}
	}

	public static net.milkbowl.vault.economy.Economy getEconomy() {
		return economy;
	}

}
