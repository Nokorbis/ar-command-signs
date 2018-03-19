package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.controller.CommandSignCommands;
import be.nokorbis.spigot.commandsigns.controller.Economy;
import be.nokorbis.spigot.commandsigns.controller.CommandSignListener;
import be.nokorbis.spigot.commandsigns.controller.Container;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignsPlugin extends JavaPlugin{

	private static CommandSignsPlugin plugin;

	@Override
	public void onEnable() {
		plugin = this;

		Economy.initialize();
		CommandSignCommands executor = new CommandSignCommands();
		Container.getContainer(); // Intialize the all stuff
		this.getCommand("commandsign").setExecutor(executor);
		this.getCommand("commandsign").setTabCompleter(executor);
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(), this);
	}

	@Override
	public void onDisable() {
		plugin = null;
	}

	public static CommandSignsPlugin getPlugin() {
		return plugin;
	}
}