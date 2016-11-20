package net.bendercraft.spigot.commandsigns;

import net.bendercraft.spigot.commandsigns.controller.CommandSignCommands;
import net.bendercraft.spigot.commandsigns.controller.CommandSignListener;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.controller.Economy;
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
		Container.getContainer().getSaver().saveAll(Container.getContainer().getCommandBlocks().values());
	}

	public static CommandSignsPlugin getPlugin() {
		return plugin;
	}
}