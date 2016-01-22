package net.avatar.realms.spigot.commandsign;

import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.controller.CommandSignCommands;
import net.avatar.realms.spigot.commandsign.controller.CommandSignListener;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.Economy;

public class CommandSign extends JavaPlugin{

	private static CommandSign plugin;

	@Override
	public void onEnable() {
		plugin = this;

		Economy.initialize();
		CommandSignCommands executor = new CommandSignCommands();
		this.getCommand("commandsign").setExecutor(executor);
		this.getCommand("commandsign").setTabCompleter(executor);
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(), this);
	}

	@Override
	public void onDisable() {
		plugin = null;
		Container.getContainer().getSaver().saveAll(Container.getContainer().getCommandBlocks().values());
	}

	public static CommandSign getPlugin() {
		return plugin;
	}
}