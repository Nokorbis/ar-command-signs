package net.avatar.realms.spigot.commandsign;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSignCommands implements CommandExecutor{
	
	private CommandSign plugin;
	
	public CommandSignCommands (CommandSign plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		sender.sendMessage("Sender : " + sender + "| CMD : " + cmd.toString() + " |label : " + label);
		if (!(sender instanceof Player)) {
			return false;
		}
		
		return true ;
	}

}
