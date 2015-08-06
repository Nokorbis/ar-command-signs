package net.avatar.realms.spigot.commandsign.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.avatar.realms.spigot.commandsign.CommandSign;

public class CommandBlock {

	private Location location;
	
	private List<String> neededPermissions;
	private List<String> commands;
	private List<String> permissions;
	
	public CommandBlock () {
		
		// We use ArrayList because we want to remove/edit them by the index.
		this.commands = new ArrayList<String>();
		this.permissions = new ArrayList<String>();
		this.neededPermissions = new ArrayList<String>();
	}
	
			/* Getters and setters */

	/* Block */
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}
	
	/* Commands */
	public void addCommand (String command) {
		command = command.intern();
		if (!commands.contains(command)) {
			commands.add(command);
		}
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public boolean removeCommand (int index) {
		if (index < 0) {
			return false;
		}
		if (commands.size() <= index) {
			return false;
		}
		commands.remove(index);
		return true;
	}
	
	public void editCommand (int index, String newCmd) {
		if (index < 0) {
			return;
		}
		removeCommand(index);
		commands.add(index, newCmd);
	}
	
	/* Needed permissions */
	public void addNeededPermission (String permission) {
		permission = permission.intern();
		if (!neededPermissions.contains(permission)) {
			neededPermissions.add(permission);
		}
	}
	
	public List<String> getNeededPermissions() {
		return neededPermissions;
	}
	
	public boolean removeNeededPermission(int index) {
		if (index < 0) {
			return false;
		}
		if (neededPermissions.size() <= index) {
			return false;
		}
		neededPermissions.remove(index);
		return true;
	}
	
	public void editNeededPermission(int index, String newPerm) {
		if (index < 0) {
			return;
		}
		removeNeededPermission(index);
		neededPermissions.add(index, newPerm);
	}
	
	/* Permissions */
	public void addPermission (String permission) {
		permission = permission.intern();
		if (!permissions.contains(permission)) {
			permissions.add(permission);
		}
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	public boolean removePermission(int index) {
		if (index < 0){
			return false;
		}
		if (permissions.size() <= index) {
			return false;
		}
		
		permissions.remove(index);
		return true;
	}
	
	public void editPermission(int index, String newPerm) {
		if (index < 0) {
			return;
		}
		removePermission(index);
		permissions.add(index, newPerm);
	}
	
			/* Business */
	
	/**
	 * Execute the command block as the player
	 * @param player The player that executes the command block
	 * @return <code>true</code> if the player had the needed permissions to execute the command block
	 * <code>false</code> if the player did not have the needed permissions to execute the command block
	 */
	public boolean execute (Player player) {
		if (player == null) {
			return false;
		}
		
		for (String needed : neededPermissions) {
			if (!player.hasPermission(needed)) {
				player.sendMessage(ChatColor.DARK_RED + "You do not have the needed permission : " + needed);
				return false;
			}
		}
		
		PermissionAttachment perms = CommandSign.getPlugin().getPlayerPermissions(player);
		for (String perm : permissions) {
			if (!player.hasPermission(perm)) {
				perms.setPermission(perm, true);
			}
		}
		
		for (String command : commands) {
			String cmd = formatCommand(command, player);
			System.out.println(cmd);
			player.chat(cmd);
			//CommandSign.getPlugin().getServer().dispatchCommand(player, cmd);
		}
		
		for (String perm : permissions) {
			if (perms.getPermissions().containsKey(perm)) {
				perms.unsetPermission(perm);
			}
		}
		
		return true;
	}
	
	private String formatCommand (String command, Player player) {
		String cmd = new String(command);
		
		cmd = cmd.replaceAll("%player%", player.getName());
		cmd = cmd.replaceAll("%PLAYER%", player.getName());
		
		return cmd;
	}

	public CommandBlock copy() {
		CommandBlock newBlock = new CommandBlock();
		
		for (String perm : this.permissions) {
			newBlock.addPermission(perm);
		}
		
		for (String perm : this.neededPermissions) {
			newBlock.addNeededPermission(perm);
		}
		
		for (String cmd : this.commands) {
			newBlock.addCommand(cmd);
		}
		
		return newBlock;
	}
	
	public boolean validate() {
		if (location == null) {
			return false;
		}
		
		if (!CommandSign.VALID_MATERIALS.contains(location.getBlock().getType())) {
			return false;
		}
		
		if (permissions == null || commands == null || neededPermissions == null) {
			return false;
		}
		
		return true;
	}
	
	public void info (Player player, ChatColor c) {
		player.sendMessage(c + "Block : " + blockSummary());
		player.sendMessage(c + "Needed permissions :");
		int cpt = 1;
		for (String perm : neededPermissions) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
		}
		player.sendMessage(c + "Permissions :");
		cpt = 1;
		for (String perm : permissions) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
		}
		player.sendMessage(c + "Commands :");
		cpt = 1;
		for (String cmd : commands) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + cmd);
		}
	}
	
	private String blockSummary () {
		if (location == null) {
			return "";
		}
		String str = location.getBlock().getType() + " #" + location.getX() + ":" + location.getZ()+"(" +location.getY()+")";
		return str;
	}
	
	
}
