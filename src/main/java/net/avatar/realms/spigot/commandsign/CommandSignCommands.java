package net.avatar.realms.spigot.commandsign;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandSignCommands implements CommandExecutor{
	
	private CommandSign plugin;
	
	public CommandSignCommands (CommandSign plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a played to execute this command.");
			return false;
		}
		
		if (args.length < 1 ) {
			sender.sendMessage(ChatColor.RED + "You need an argument to use this command.");
			return false;
		}
		
		String subCmd = args[0];
		
		if (subCmd.equalsIgnoreCase("Create") || subCmd.equalsIgnoreCase("Cr")) {
			return create((Player) sender);
		}
		else if (subCmd.equalsIgnoreCase("Edit")) {
			return edit((Player) sender);
		}
		else if (subCmd.equalsIgnoreCase("Delete") || subCmd.equalsIgnoreCase("Del")) {
			return delete ((Player) sender);
		}
		else {
			sender.sendMessage(ChatColor.RED + "Invalid Subcommand. Must be : create, edit or delete");
			return false;
		}
	}
	
	
	private boolean create (Player player) {
		return true;
	}
	
	private boolean edit (Player player) {
		return true;
	}
	
	private boolean delete (Player player) {
		return true;
	}

}
