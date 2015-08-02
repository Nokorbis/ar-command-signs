package net.avatar.realms.spigot.commandsign.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.avatar.realms.spigot.commandsign.CommandSign;

public class CommandBlock {

	private Block block;
	
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
	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
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
		if (commands.size() <= index) {
			return false;
		}
		commands.remove(index);
		return true;
	}
	
	public void editCommand (int index, String newCmd) {
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
		if (neededPermissions.size() <= index) {
			return false;
		}
		neededPermissions.remove(index);
		return true;
	}
	
	public void editNeededPermission(int index, String newPerm) {
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
		if (permissions.size() <= index) {
			return false;
		}
		
		permissions.remove(index);
		return true;
	}
	
	public void editPermission(int index, String newPerm) {
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
			CommandSign.getPlugin().getServer().dispatchCommand(player, cmd);
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
		
		cmd.replace("%player%", player.getName());
		cmd.replace("%PLAYER%", player.getName());
		
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
		if (block == null) {
			return false;
		}
		
		if (!CommandSign.VALID_MATERIALS.contains(block.getType())) {
			return false;
		}
		
		if (permissions == null || commands == null || neededPermissions == null) {
			return false;
		}
		
		return true;
	}
	
	
}
