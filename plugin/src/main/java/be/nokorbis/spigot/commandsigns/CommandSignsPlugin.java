package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownAddon;
import be.nokorbis.spigot.commandsigns.addons.economy.EconomyAddon;
import be.nokorbis.spigot.commandsigns.addons.permissions.PermissionsAddon;
import be.nokorbis.spigot.commandsigns.api.AddonRegister;
import be.nokorbis.spigot.commandsigns.controller.*;
import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignsPlugin extends JavaPlugin {

	private static CommandSignsPlugin plugin;

	private NCommandSignsManager manager;

	@Override
	public void onLoad() {
		Settings.loadSettings(plugin);

		this.manager = new NCommandSignsManager(this);
		NCommandBlockExecutor.setManager(manager);

		AddonRegister addonRegister = new NCommandSignsAddonRegister(manager);
		getServer().getServicesManager().register(AddonRegister.class, addonRegister, this, ServicePriority.Normal);

		addonRegister.registerAddon(new PermissionsAddon());
		addonRegister.registerAddon(new CooldownAddon());
		EconomyAddon economyAddon = new EconomyAddon(this);
		if (economyAddon.isEconomyLinked()) {
			addonRegister.registerAddon(economyAddon);
		}
	}

	@Override
	public void onEnable() {
		plugin = this;
		EconomyWrapper.initialize();
		Container.getContainer(); // Initialize the all stuff

		CommandSignCommands commandExecutor = new CommandSignCommands(manager);

		PluginCommand mainCommand = this.getCommand("commandsign");
		mainCommand.setExecutor(commandExecutor);
		mainCommand.setTabCompleter(commandExecutor);


		this.getServer().getPluginManager().registerEvents(new CommandSignListener(manager), this);
	}

	@Override
	public void onDisable() {
		plugin = null;
		this.manager = null;

		PluginCommand mainCommand = this.getCommand("commandsign");
		mainCommand.setExecutor(null);
		mainCommand.setTabCompleter(null);
	}

	public static CommandSignsPlugin getPlugin() {
		return plugin;
	}
}