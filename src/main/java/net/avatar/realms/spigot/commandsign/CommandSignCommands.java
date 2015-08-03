package net.avatar.realms.spigot.commandsign;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class CommandSignCommands implements CommandExecutor{
	
	private CommandSign plugin;
	
	public CommandSignCommands (CommandSign plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to execute this command.");
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
		else if (subCmd.equalsIgnoreCase("Delete") || subCmd.equalsIgnoreCase("Del")
				|| subCmd.equalsIgnoreCase("Remove") || subCmd.equalsIgnoreCase("Rem")) {
			return delete ((Player) sender);
		}
		else if (subCmd.equalsIgnoreCase("Copy") || subCmd.equalsIgnoreCase("Cp")) {
			return copy ((Player) sender);
		}
		else if (subCmd.equalsIgnoreCase("Info")) {
			return info ((Player) sender);
		}
		else {
			sender.sendMessage(ChatColor.RED + "Invalid Subcommand. Must be : info, create, edit or delete");
			return false;
		}
	}
	
	/* 
	 * These commands are only initiating command block creation/edition/deletion.
	 * The real configuration is made in the listener.
	 */
	
	private boolean info(Player player) {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.info")) {
			player.sendMessage(ChatColor.RED + "You do NOT have the permission to use that command.");
			return false;
		}
		
		if (!isPlayerAvailable(player)) {
			return false;
		}
		
		plugin.getInfoPlayers().add(player);
		player.sendMessage(ChatColor.GOLD + "Click on command block whose you want information");
		return true;
	}

	private boolean create (Player player) {
		
		
		CommandBlock cmdBlock = new CommandBlock();
		EditingConfiguration ecf = new EditingConfiguration(player, cmdBlock);
		ecf.setCreating(true);
		ecf.display();
		plugin.getCreatingConfigurations().put(player, ecf);
		
		return true;
	}
	
	private boolean edit (Player player) {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.edit")) {
			player.sendMessage(ChatColor.RED + "You do NOT have the permission to use that command.");
		}
		
		if (!isPlayerAvailable(player)) {
			return false;
		}
		
		EditingConfiguration conf = new EditingConfiguration(player);
		plugin.getEditingConfigurations().put(player, conf);
		player.sendMessage(ChatColor.GOLD + "Click on the block you want to edit");
		
		return true;
	}
	
	private boolean delete (Player player) {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.delete")) {
			player.sendMessage(ChatColor.RED + "You do NOT have the permission to use that command.");
		}
		
		if (!isPlayerAvailable(player)) {
			return false;
		}
		
		plugin.getDeletingBlocks().put(player, null);
		player.sendMessage(ChatColor.GOLD + "Click on the command block you want to delete.");
		
		return true;
	}
	
	private boolean copy (Player player) {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.copy")) {
			player.sendMessage(ChatColor.RED + "You do NOT have the permission to use that command.");
		}
		
		if (!isPlayerAvailable(player)) {
			return false;
		}
		
		plugin.getCopyingConfigurations().put(player, null);
		player.sendMessage(ChatColor.GOLD + "Click on the command block you want to copy.");
		
		return true;
	}
	
	/**
	 * Checks if the player is already doing some creation/edition/deletion about a configuration.
	 * @param player
	 * @return <code>true</code> if the player isn't doing anything
	 * <code>false</code> if the player is already doing something
	 */
	private boolean isPlayerAvailable(Player player) {
		if (plugin.getCreatingConfigurations().containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You are already creating a configuration");
			return false;
		}
		
		if (plugin.getEditingConfigurations().containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You are already editing a configuration");
			return false;
		}
		
		if (plugin.getDeletingBlocks().containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You are already deleting a block");
			return false;
		}
		
		if (plugin.getCopyingConfigurations().containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You are already copying a block");
			return false;
		}
		
		if (plugin.getCopyingConfigurations().containsKey(player)) {
			player.sendMessage(ChatColor.RED + "You are already copying a block");
			return false;
		}
		return true;
	}

}
