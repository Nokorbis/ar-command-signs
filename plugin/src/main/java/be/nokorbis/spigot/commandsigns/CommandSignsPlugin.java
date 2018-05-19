package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.controller.*;
import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSignsPlugin extends JavaPlugin{

	private static CommandSignsPlugin plugin;

	@Override
	public void onLoad()
	{
		plugin = this;

		Settings.loadSettings(plugin);
	}

	@Override
	public void onEnable()
	{
		Economy.initialize();
		NCommandSignsManager manager = new NCommandSignsManager(this);
		CommandSignCommands executor = new CommandSignCommands(manager);
		Container.getContainer(); // Intialize the all stuff
		this.getCommand("commandsign").setExecutor(executor);
		this.getCommand("commandsign").setTabCompleter(executor);
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(), this);
	}

	@Override
	public void onDisable()
	{
		plugin = null;
	}

	public static CommandSignsPlugin getPlugin()
	{
		return plugin;
	}
}