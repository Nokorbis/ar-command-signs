package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.addons.confirmation.ConfirmationAddon;
import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownAddon;
import be.nokorbis.spigot.commandsigns.addons.economy.EconomyAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.api.AddonRegister;
import be.nokorbis.spigot.commandsigns.controller.*;
import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignsPlugin extends JavaPlugin {

	private static CommandSignsPlugin plugin;

	private NCommandSignsManager manager;
	private NCommandSignsAddonRegister addonRegister;


	@Override
	public void onLoad() {
		plugin = this;
		Settings.loadSettings(plugin);

		addonRegister = new NCommandSignsAddonRegister();
		getServer().getServicesManager().register(AddonRegister.class, addonRegister, this, ServicePriority.Normal);

		addonRegister.registerAddon(new RequiredPermissionsAddon(this));
		addonRegister.registerAddon(new ConfirmationAddon(this));
		addonRegister.registerAddon(new CooldownAddon(this));
		EconomyAddon economyAddon = new EconomyAddon(this);
		addonRegister.registerAddon(economyAddon);
	}

	@Override
	public void onEnable() {
		this.manager = new NCommandSignsManager(this);

		addonRegister.triggerEnable();
		addonRegister.registerInManager(manager);

		manager.loadIdsPerLocations();
		manager.initializeMenus();
		manager.initializeSerializers();

		NCommandBlockExecutor.setManager(manager);

		CommandSignCommands commandExecutor = new CommandSignCommands(manager);

		PluginCommand mainCommand = this.getCommand("commandsign");
		if (mainCommand != null) {
			mainCommand.setExecutor(commandExecutor);
			mainCommand.setTabCompleter(commandExecutor);
		}

		this.getServer().getPluginManager().registerEvents(new CommandSignListener(manager), this);
	}

	@Override
	public void onDisable() {
		this.manager = null;

		PluginCommand mainCommand = this.getCommand("commandsign");
		if (mainCommand != null) {
			mainCommand.setExecutor(null);
			mainCommand.setTabCompleter(null);
		}
	}

	public static CommandSignsPlugin getPlugin() {
		return plugin;
	}
}