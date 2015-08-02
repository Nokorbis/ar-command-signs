package net.avatar.realms.spigot.commandsign.model;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.enums.EditionState;

public class EditingConfiguration {

	private CommandBlock commandBlock;
	private EditionState state;
	private boolean isCreating;
	private Player player;
	private ChatColor c = ChatColor.AQUA;
	
	public EditingConfiguration(Player player) {
		state = EditionState.MainMenu;
		setCreating(false);
	}
	
	public EditingConfiguration(Player player, CommandBlock cmd) {
		this(player);
		this.setCommandBlock(cmd);
	}
	
	/* Getters and Setter */

	public CommandBlock getCommandBlock() {
		return commandBlock;
	}

	public void setCommandBlock(CommandBlock commandBlock) {
		this.commandBlock = commandBlock;
	}

	public boolean isCreating() {
		return isCreating;
	}

	public void setCreating(boolean isCreating) {
		this.isCreating = isCreating;
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/* Business */
	
	/**
	 * Display the menu that the player (admin) sees when he is editing a command block configuration
	 */
	public void display() {
		switch (state) {
			case MainMenu : 
				printMainMenu();
				break;
			case NeededPermissions :
				printNeededPermissions();
				break;
			case Permissions :
				printPermissions();
				break;
			case Commands:
				printCommands();
				break;
			case NeededPermissionsAdd:
				player.sendMessage(c + "Enter the needed permission string : ");
				break;
			case NeededPermissionsRemove:
				printNeededPermissionsRemove();
				break;
			case NeededPermissionsEdit:
				printNeededPermissionsEdit();
				break;
			case PermissionsAdd:
				player.sendMessage(c + "Enter the temporary permission string : ");
				break;
			case PermissionsRemove:
				printPermissionsRemove();
				break;
			case PermissionsEdit:
				printPermissionsEdit();
				break;
			case CommandsAdd:
				player.sendMessage(c + "Enter the command string : ");
				break;
			case CommandsRemove:
				printCommandsRemove();
				break;
			case CommandsEdit:
				printCommandsEdit();
				break;
		}
	}
	
	public void input(int index) {
		switch (state) {
			case MainMenu:
				if (index == 2) {
					state = EditionState.NeededPermissions;
				}
				else if (index == 3) {
					state = EditionState.Permissions;
				}
				else if (index == 4) {
					state = EditionState.Commands;
				}
				else if (index == 5) {
					if (commandBlock.validate()) {
						if (isCreating) {
							CommandSign.getPlugin().getCommandBlocks().put(commandBlock.getBlock(), commandBlock);
							CommandSign.getPlugin().getCreatingConfigurations().remove(player);
						}
						else {
							CommandSign.getPlugin().getEditingConfigurations().remove(player);
						}
					}
				}
				break;
			case Permissions:
				if (index == 2) {
					state = EditionState.PermissionsAdd;
				}
				else if (index == 3) {
					state = EditionState.PermissionsEdit;
				}
				else if (index == 4) {
					state = EditionState.PermissionsRemove;
				}
				else if (index == 5) {
					state = EditionState.MainMenu;
				}
				break;
			case Commands:
				if (index == 2) {
					state = EditionState.CommandsAdd;
				}
				else if (index == 3) {
					state = EditionState.CommandsEdit;
				}
				else if (index == 4) {
					state = EditionState.CommandsRemove;
				}
				else if (index == 5) {
					state = EditionState.MainMenu;
				}
				break;
			case NeededPermissions:
				if (index == 2) {
					state = EditionState.NeededPermissionsAdd;
				}
				else if (index == 3) {
					state = EditionState.NeededPermissionsEdit;
				}
				else if (index == 4) {
					state = EditionState.NeededPermissionsRemove;
				}
				else if (index == 5) {
					state = EditionState.MainMenu;
				}
				break;
			default :// Don't know what to do. lol.
					
		}
	}
	
	public void input (String str) {
		String[] args;
		int index = 0;
		switch (state) {
			case PermissionsAdd:
				commandBlock.addPermission(str);
				break;
			case PermissionsEdit:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.editPermission(index, args[1]);
				} catch (Exception e) {
				}
				break;
			case PermissionsRemove:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.removePermission(index);
				} catch (Exception e) {
				}
				break;
			case CommandsAdd:
				commandBlock.addCommand(str);
				break;
			case CommandsEdit:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.editCommand(index, args[1]);
				} catch (Exception e) {
				}
				break;
			case CommandsRemove:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.removeCommand(index);
				} catch (Exception e) {
				}
				break;
			case NeededPermissionsAdd:
				commandBlock.addNeededPermission(str);
				break;
			case NeededPermissionsEdit:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.editNeededPermission(index, args[1]);
				} catch (Exception e) {
				}
				break;
			case NeededPermissionsRemove:
				try {
					args = str.split(" ", 1);
					index = Integer.parseInt(args[0]);
					commandBlock.removeNeededPermission(index);
				} catch (Exception e) {
				}
				break;
			default :
				try {
					args = str.split(" ");
					index = Integer.parseInt(args[0]);
					input(index);
				} catch (Exception e) {
				}
		}
	}
	
	private void printMainMenu() {
		Block block = commandBlock.getBlock();
		player.sendMessage(c + "1. Refresh");
		if (block == null) {
			player.sendMessage(c + "   Blocks : None");
		}
		else {
			String str = c + "   Blocks : " + block.getType() + "#" + block.getX() + ":" + block.getZ() + "("+block.getY()+")";
			if (isCreating) {
				str += " [Set on click]";
			}
			player.sendMessage(str);
		}                  
		player.sendMessage("2. Needed permissions");
		player.sendMessage("3. Temporary permissions");
		player.sendMessage("4. Commands");
		player.sendMessage(ChatColor.GREEN + "5. Done");
	}
	
	private void printNeededPermissions() {
		player.sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getNeededPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "1. Refresh");
		player.sendMessage(c + "2. Add");
		player.sendMessage(c + "3. Edit");
		player.sendMessage(c + "4. Remove");
		player.sendMessage(ChatColor.GREEN + "5. Done");
	}
	
	private void printNeededPermissionsRemove() {
		player.sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getNeededPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "Enter the index of the permission you want to remove : ");
	}
	
	private void printNeededPermissionsEdit() {
		player.sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getNeededPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "Enter the index of the permission you want to edit followed by the new permission string : ");
	}
	
	private void printPermissions() {
		player.sendMessage(c + "Temporary permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "1. Refresh");
		player.sendMessage(c + "2. Add");
		player.sendMessage(c + "3. Edit");
		player.sendMessage(c + "4. Remove");
		player.sendMessage(ChatColor.GREEN + "5. Done");
	}
	
	private void printPermissionsRemove() {
		player.sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "Enter the index of the permission you want to remove : ");
	}
	
	private void printPermissionsEdit() {
		player.sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : commandBlock.getPermissions()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + perm);
		}
		player.sendMessage(c + "Enter the index of the permission you want to edit followed by the new permission string : ");
	}
	
	private void printCommands() {
		player.sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String cmd : commandBlock.getCommands()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + cmd);
		}
		player.sendMessage(c + "1. Refresh");
		player.sendMessage(c + "2. Add");
		player.sendMessage(c + "3. Edit");
		player.sendMessage(c + "4. Remove");
		player.sendMessage(ChatColor.GREEN + "5. Done");
	}
	
	private void printCommandsRemove() {
		player.sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String cmd : commandBlock.getCommands()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + cmd);
		}
		player.sendMessage(c + "Enter the index of the command you want to remove : ");
	}
	
	private void printCommandsEdit() {
		player.sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String cmd : commandBlock.getCommands()) {
			player.sendMessage(ChatColor.GRAY + "---" + cpt++ + cmd);
		}
		player.sendMessage(c + "Enter the index of the command you want to edit followed by the new command string : ");
	}
	
}
