package net.avatar.realms.spigot.commandsign.model;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
		this.state = EditionState.MainMenu;
		setCreating(false);
		this.player = player;
	}

	public boolean isAddingCommand() {
		return this.state == EditionState.CommandsAdd;
	}

	public EditingConfiguration(Player player, CommandBlock cmd) {
		this(player);
		this.setCommandBlock(cmd);
	}

	/* Getters and Setter */

	public CommandBlock getCommandBlock() {
		return this.commandBlock;
	}

	public void setCommandBlock(CommandBlock commandBlock) {
		this.commandBlock = commandBlock;
	}

	public boolean isCreating() {
		return this.isCreating;
	}

	public void setCreating(boolean isCreating) {
		this.isCreating = isCreating;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	/* Business */

	/**
	 * Display the menu that the player (admin) sees when he is editing a command block configuration
	 */
	public void display() {
		switch (this.state) {
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
				this.player.sendMessage(this.c + "Enter the needed permission string : ");
				break;
			case NeededPermissionsRemove:
				printNeededPermissionsRemove();
				break;
			case NeededPermissionsEdit:
				printNeededPermissionsEdit();
				break;
			case PermissionsAdd:
				this.player.sendMessage(this.c + "Enter the temporary permission string : ");
				break;
			case PermissionsRemove:
				printPermissionsRemove();
				break;
			case PermissionsEdit:
				printPermissionsEdit();
				break;
			case CommandsAdd:
				this.player.sendMessage(this.c + "Enter the command string : ");
				break;
			case CommandsRemove:
				printCommandsRemove();
				break;
			case CommandsEdit:
				printCommandsEdit();
				break;
			case Timer:
				printTimerMenu();
				break;
			case TimeSet:
				printTimeSet();
				break;
			case TimerCancelled:
				printTimerCancelled();
				break;
			case TimerReset:
				printTimerReset();
				break;
			case Done:
				if (this.isCreating) {
					this.player.sendMessage(ChatColor.GREEN + "Command block created !");
				}
				else {
					this.player.sendMessage(ChatColor.GREEN + "Command block edited !");
				}

				break;
		}
	}


	private void printTimerReset () {
		this.player.sendMessage(this.c + "Should the timer be reset when the player moves ? (Yes/No)");

	}

	private void printTimerCancelled () {
		this.player.sendMessage(this.c + "Should the timer be cancelled when the player moves ? (Yes/No)");

	}

	private void printTimeSet () {
		this.player.sendMessage(this.c + "Enter the amount of time (in seconds) that the player must wait before the execution of the command");

	}

	private void printTimerMenu () {
		this.player.sendMessage(this.c + "1. Refresh");
		this.player.sendMessage(this.c + "2. Time (" + this.commandBlock.getTimer()+")");
		this.player.sendMessage(this.c + "3. Cancel on move (" + ((this.getCommandBlock().isCancelledOnMove())? "Yes" : "No") +")");
		this.player.sendMessage(this.c + "4. Reset on move (" + ((this.getCommandBlock().isResetOnMove())? "Yes" : "No") +")");
		this.player.sendMessage(ChatColor.GREEN + "9. Done");

	}

	public void input(int index) {
		switch (this.state) {
			case MainMenu:
				if (index == 2) {
					this.state = EditionState.NeededPermissions;
				}
				else if (index == 3) {
					this.state = EditionState.Permissions;
				}
				else if (index == 4) {
					this.state = EditionState.Commands;
				}
				else if (index == 5) {
					this.state = EditionState.Timer;
				}
				else if (index == 9) {
					if (this.commandBlock.validate()) {
						if (this.isCreating) {
							CommandSign.getPlugin().getCommandBlocks().put(this.commandBlock.getLocation(), this.commandBlock);
							CommandSign.getPlugin().getCreatingConfigurations().remove(this.player);
							this.state = EditionState.Done;
						}
						else {
							CommandSign.getPlugin().getEditingConfigurations().remove(this.player);
							this.state = EditionState.Done;
						}
					}
					else {
						this.player.sendMessage(ChatColor.RED + "The command block is not valid.");
					}
				}
				break;
			case Permissions:
				if (index == 2) {
					this.state = EditionState.PermissionsAdd;
				}
				else if (index == 3) {
					this.state = EditionState.PermissionsEdit;
				}
				else if (index == 4) {
					this.state = EditionState.PermissionsRemove;
				}
				else if (index == 9) {
					this.state = EditionState.MainMenu;
				}
				break;
			case Commands:
				if (index == 2) {
					this.state = EditionState.CommandsAdd;
				}
				else if (index == 3) {
					this.state = EditionState.CommandsEdit;
				}
				else if (index == 4) {
					this.state = EditionState.CommandsRemove;
				}
				else if (index == 9) {
					this.state = EditionState.MainMenu;
				}
				break;
			case NeededPermissions:
				if (index == 2) {
					this.state = EditionState.NeededPermissionsAdd;
				}
				else if (index == 3) {
					this.state = EditionState.NeededPermissionsEdit;
				}
				else if (index == 4) {
					this.state = EditionState.NeededPermissionsRemove;
				}
				else if (index == 9) {
					this.state = EditionState.MainMenu;
				}
				break;
			case Timer:
				if (index == 2) {
					this.state = EditionState.TimeSet;
				}
				else if (index == 3) {
					this.state = EditionState.TimerCancelled;
				}
				else if (index == 4) {
					this.state = EditionState.TimerReset;
				}
				else if (index == 9) {
					this.state = EditionState.MainMenu;
				}
			default :// Don't know what to do. lol.

		}
	}

	public void input (String str) {
		String[] args;
		int index = 0;
		switch (this.state) {
			case PermissionsAdd:
				this.commandBlock.addPermission(str);
				this.state = EditionState.Permissions;
				break;
			case PermissionsEdit:
				try {
					this.state = EditionState.Permissions;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.editPermission(index - 1, args[1]);
				} catch (Exception e) {
				}
				break;
			case PermissionsRemove:
				try {
					this.state = EditionState.Permissions;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.removePermission(index - 1);
				} catch (Exception e) {
				}
				break;
			case CommandsAdd:
				this.commandBlock.addCommand(str);
				this.state = EditionState.Commands;
				break;
			case CommandsEdit:
				try {
					this.state = EditionState.Commands;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.editCommand(index -1, args[1]);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(str);
				}
				break;
			case CommandsRemove:
				try {
					this.state = EditionState.Commands;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.removeCommand(index -1);
				} catch (Exception e) {
				}
				break;
			case NeededPermissionsAdd:
				this.commandBlock.addNeededPermission(str);
				this.state = EditionState.NeededPermissions;
				break;
			case NeededPermissionsEdit:
				try {
					this.state = EditionState.NeededPermissions;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.editNeededPermission(index -1, args[1]);
				} catch (Exception e) {
				}
				break;
			case NeededPermissionsRemove:
				try {
					this.state = EditionState.NeededPermissions;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.removeNeededPermission(index -1);
				} catch (Exception e) {
				}
				break;
			case TimeSet:
				try {
					this.state = EditionState.Timer;
					args = str.split(" ", 2);
					index = Integer.parseInt(args[0]);
					this.commandBlock.setTimer(index);
				} catch (Exception e) {
				}
				break;
			case TimerCancelled: 
				args = str.split(" ");
				if (args[0].equalsIgnoreCase("Yes") || args[0].equalsIgnoreCase("Y") || args[0].equalsIgnoreCase("True")) {
					this.commandBlock.setCancelledOnMove(true);
				}
				else {
					if (!args[0].equals("CANCEL")) {
						this.commandBlock.setCancelledOnMove(false);
					}
				}
				this.state = EditionState.Timer;
				break;
			case TimerReset:
				args = str.split(" ");
				if (args[0].equalsIgnoreCase("Yes") || args[0].equalsIgnoreCase("Y") || args[0].equalsIgnoreCase("True")) {
					this.commandBlock.setResetOnMove(true);
				}
				else {
					if (!args[0].equals("CANCEL")) {
						this.commandBlock.setResetOnMove(false);
					}
				}
				this.state = EditionState.Timer;
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
		Location loc = this.commandBlock.getLocation();
		this.player.sendMessage(this.c + "1. Refresh");
		if (loc == null) {
			this.player.sendMessage(this.c + "   Blocks : None");
		}
		else {
			String str = this.c + "   Blocks : " + loc.getBlock().getType() + "#" + loc.getX() + ":" + loc.getZ() + "("+loc.getY()+")";
			if (this.isCreating) {
				str += " [Set on click]";
			}
			this.player.sendMessage(str);
		}                  
		this.player.sendMessage(this.c + "2. Needed permissions");
		this.player.sendMessage(this.c + "3. Temporary permissions");
		this.player.sendMessage(this.c + "4. Commands");
		this.player.sendMessage(this.c + "5. Timer");
		this.player.sendMessage(ChatColor.GREEN + "9. Done");
	}

	private void printNeededPermissions() {
		this.player.sendMessage(this.c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getNeededPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "1. Refresh");
		this.player.sendMessage(this.c + "2. Add");
		this.player.sendMessage(this.c + "3. Edit");
		this.player.sendMessage(this.c + "4. Remove");
		this.player.sendMessage(ChatColor.GREEN + "9. Done");
	}

	private void printNeededPermissionsRemove() {
		this.player.sendMessage(this.c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getNeededPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "Enter the index of the permission you want to remove : ");
	}

	private void printNeededPermissionsEdit() {
		this.player.sendMessage(this.c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getNeededPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "Enter the index of the permission you want to edit followed by the new permission string : ");
	}

	private void printPermissions() {
		this.player.sendMessage(this.c + "Temporary permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "1. Refresh");
		this.player.sendMessage(this.c + "2. Add");
		this.player.sendMessage(this.c + "3. Edit");
		this.player.sendMessage(this.c + "4. Remove");
		this.player.sendMessage(ChatColor.GREEN + "9. Done");
	}

	private void printPermissionsRemove() {
		this.player.sendMessage(this.c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "Enter the index of the permission you want to remove : ");
	}

	private void printPermissionsEdit() {
		this.player.sendMessage(this.c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : this.commandBlock.getPermissions()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		this.player.sendMessage(this.c + "Enter the index of the permission you want to edit followed by the new permission string : ");
	}

	private void printCommands() {
		this.player.sendMessage(this.c + "Commands : ");
		int cpt = 1;
		for (String cmd : this.commandBlock.getCommands()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
		}
		this.player.sendMessage(this.c + "1. Refresh");
		this.player.sendMessage(this.c + "2. Add");
		this.player.sendMessage(this.c + "3. Edit");
		this.player.sendMessage(this.c + "4. Remove");
		this.player.sendMessage(ChatColor.GREEN + "9. Done");
	}

	private void printCommandsRemove() {
		this.player.sendMessage(this.c + "Commands : ");
		int cpt = 1;
		for (String cmd : this.commandBlock.getCommands()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
		}
		this.player.sendMessage(this.c + "Enter the index of the command you want to remove : ");
	}

	private void printCommandsEdit() {
		this.player.sendMessage(this.c + "Commands : ");
		int cpt = 1;
		for (String cmd : this.commandBlock.getCommands()) {
			this.player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
		}
		this.player.sendMessage(this.c + "Enter the index of the command you want to edit followed by the new command string : ");
	}

}
