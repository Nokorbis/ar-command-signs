package net.avatar.realms.spigot.commandsign.tasks;

import net.avatar.realms.spigot.commandsign.CommandSign;

public class SaverTask implements Runnable{
	
	private CommandSign plugin;
	
	public SaverTask(CommandSign plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		plugin.saveData();
	}

}
