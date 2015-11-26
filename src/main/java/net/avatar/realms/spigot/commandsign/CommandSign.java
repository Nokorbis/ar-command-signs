package net.avatar.realms.spigot.commandsign;

import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.controller.CommandSignCommands;
import net.avatar.realms.spigot.commandsign.controller.CommandSignListener;
import net.avatar.realms.spigot.commandsign.data.Container;

public class CommandSign extends JavaPlugin{

	private static CommandSign plugin;

	@Override
	public void onEnable() {
		plugin = this;

		this.getCommand("commandsign").setExecutor(new CommandSignCommands());
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(), this);
	}

	@Override
	public void onDisable() {
		plugin = null;
		Container.getContainer().saveData();
	}

	public static CommandSign getPlugin() {
		return plugin;
	}
}