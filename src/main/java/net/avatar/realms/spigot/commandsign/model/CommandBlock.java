package net.avatar.realms.spigot.commandsign.model;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.CommandSignUtils;

public class CommandBlock {

	private Location location;

	private List<String> neededPermissions;
	private List<String> commands;
	private List<String> permissions;

	private Double economyPrice;

	private Integer timer; // Value in second
	private Boolean resetOnMove;
	private Boolean cancelledOnMove;

	public CommandBlock () {
		// We use ArrayList because we want to remove/edit them by the index.
		this.commands = new ArrayList<String>();
		this.permissions = new ArrayList<String>();
		this.neededPermissions = new ArrayList<String>();
		this.setTimer(0);
		this.resetOnMove = false;
		this.cancelledOnMove = false;
		this.setEconomyPrice(0.0);
	}

	/* Getters and setters */

	/* Block */
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}

	/* Commands */
	public void addCommand (String command) {
		command = command.intern();
		if (!this.commands.contains(command)) {
			this.commands.add(command);
		}
	}

	public List<String> getCommands() {
		return this.commands;
	}

	public boolean removeCommand (int index) {
		if (index < 0) {
			return false;
		}
		if (this.commands.size() <= index) {
			return false;
		}
		this.commands.remove(index);
		return true;
	}

	public void editCommand (int index, String newCmd) {
		if (index < 0) {
			return;
		}
		removeCommand(index);
		this.commands.add(index, newCmd);
	}

	/* Needed permissions */
	public void addNeededPermission (String permission) {
		permission = permission.intern();
		if (!this.neededPermissions.contains(permission)) {
			this.neededPermissions.add(permission);
		}
	}

	public List<String> getNeededPermissions() {
		return this.neededPermissions;
	}

	public boolean removeNeededPermission(int index) {
		if (index < 0) {
			return false;
		}
		if (this.neededPermissions.size() <= index) {
			return false;
		}
		this.neededPermissions.remove(index);
		return true;
	}

	public void editNeededPermission(int index, String newPerm) {
		if (index < 0) {
			return;
		}
		removeNeededPermission(index);
		this.neededPermissions.add(index, newPerm);
	}

	/* Permissions */
	public void addPermission (String permission) {
		permission = permission.intern();
		if (!this.permissions.contains(permission)) {
			this.permissions.add(permission);
		}
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public boolean removePermission(int index) {
		if (index < 0){
			return false;
		}
		if (this.permissions.size() <= index) {
			return false;
		}

		this.permissions.remove(index);
		return true;
	}

	public void editPermission(int index, String newPerm) {
		if (index < 0) {
			return;
		}
		removePermission(index);
		this.permissions.add(index, newPerm);
	}

	/* Timers */

	public Integer getTimer () {
		return this.timer;
	}

	public void setTimer (Integer timer) {
		if ((timer == null) || (timer < 0)) {
			timer = 0;
		}
		this.timer = timer;
	}

	public Boolean isCancelledOnMove() {
		return this.cancelledOnMove;
	}

	public void setCancelledOnMove(Boolean cancel) {
		if (cancel == null) {
			cancel = false;
		}
		this.cancelledOnMove = cancel;
	}

	public Boolean isResetOnMove() {
		return this.resetOnMove;
	}

	public void setResetOnMove(Boolean reset) {
		if (reset == null) {
			reset = false;
		}
		this.resetOnMove = reset;
	}

	public boolean hasTimer() {
		return this.timer >= 1;
	}

	/* Economy price */

	public Double getEconomyPrice() {
		return this.economyPrice;
	}

	public void setEconomyPrice(Double price) {
		if ((price == null) || (price < 0)) {
			price = 0.0;
		}
		this.economyPrice = price;
	}

	/* Business */

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
		if (this.location == null) {
			return false;
		}

		if (!CommandSignUtils.isValidBlock(this.location.getBlock())) {
			return false;
		}

		if ((this.permissions == null) || (this.commands == null) || (this.neededPermissions == null)) {
			return false;
		}

		return true;
	}

	public void info (Player player, ChatColor c) {
		player.sendMessage(c + "Block : " + blockSummary());
		if (CommandSign.getPlugin().getEconomy() != null) {
			player.sendMessage(c + "Costs : " + CommandSign.getPlugin().getEconomy().format(this.economyPrice));
		}
		player.sendMessage(c + "Needed permissions :");
		int cpt = 1;
		for (String perm : this.neededPermissions) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
		}
		player.sendMessage(c + "Permissions :");
		cpt = 1;
		for (String perm : this.permissions) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
		}
		player.sendMessage(c + "Commands :");
		cpt = 1;
		for (String cmd : this.commands) {
			player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + cmd);
		}
		if ((this.timer != null) && (this.timer > 0)) {
			player.sendMessage(c + "Timer :");
			player.sendMessage(ChatColor.GRAY + "" + this.timer + " seconds");
			if (this.cancelledOnMove) {
				player.sendMessage(ChatColor.GRAY + "---" + "Cancelled on move");
			}
			if (this.resetOnMove) {
				player.sendMessage(ChatColor.GRAY + "---" + "Reset on move");
			}
		}
	}

	private String blockSummary () {
		if (this.location == null) {
			return "";
		}
		String str = this.location.getBlock().getType() + " #" + this.location.getX() + ":" + this.location.getZ()+"(" +this.location.getY()+")";
		return str;
	}

}
