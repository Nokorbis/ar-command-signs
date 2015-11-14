package net.avatar.realms.spigot.commandsign.model;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.utils.Settings;
import net.milkbowl.vault.economy.Economy;

public class CommandBlockExecutor {

	private static DecimalFormat df;

	private Player player;
	private CommandBlock cmdBlock;

	public CommandBlockExecutor (Player player, CommandBlock cmdBlock) {
		this.player = player;
		this.cmdBlock = cmdBlock;
		if (df == null) {
			df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
		}
	}

	public Player getPlayer() {
		return this.player;
	}

	public CommandBlock getCommandBlock() {
		return this.cmdBlock;
	}

	public void checkRequirements() {
		if (this.player == null) {
			throw new CommandSignsException("Invalid player.");
		}

		for (String needed : this.cmdBlock.getNeededPermissions()) {
			if (!this.player.hasPermission(needed)) {
				throw new CommandSignsException("You do not have the needed permission : " + needed);
			}
		}

		if (this.cmdBlock.getTimeBetweenUsage() > 0){
			long now = System.currentTimeMillis();
			long toWait = this.cmdBlock.getLastTimeUsed() + (this.cmdBlock.getTimeBetweenUsage()*1000) - now;
			if (toWait > 0) {
				if (!this.player.hasPermission("commandsign.timer.bypass")) {
					throw new CommandSignsException("This command block has been used " + df.format(this.cmdBlock.getTimeBetweenUsage() - (toWait/1000.0)) + " seconds ago. You must wait " + df.format(toWait/1000.0) + " more seconds.");
				}
			}
		}

		if ((CommandSign.getPlugin().getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0)) {
			Economy eco = CommandSign.getPlugin().getEconomy();
			if (!eco.has(this.player, this.cmdBlock.getEconomyPrice()) && !this.player.hasPermission("commandsign.costs.bypass")) {
				throw new CommandSignsException(
						"You do not have enough money to use this command block. (" + eco.format(this.cmdBlock.getEconomyPrice()) + ")");
			}
		}

		if (!this.player.hasPermission("commandsign.timer.bypass")) {
			this.cmdBlock.refreshLastTime();
		}
	}

	public boolean execute() {
		if (this.player == null) {
			return false;
		}

		if ((CommandSign.getPlugin().getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0)) {
			if (!this.player.hasPermission("commandsign.costs.bypass")) {
				Economy eco = CommandSign.getPlugin().getEconomy();
				if (eco.has(this.player, this.cmdBlock.getEconomyPrice())) {
					eco.withdrawPlayer(this.player, this.cmdBlock.getEconomyPrice());
					this.player.sendMessage("You paied " + eco.format(this.cmdBlock.getEconomyPrice()) + " to use this command");
				}
				else {
					this.player.sendMessage(ChatColor.DARK_RED + "You do not have enough money to use this command block.");
					return false;
				}
			}
		}

		PermissionAttachment perms = CommandSign.getPlugin().getPlayerPermissions(this.player);
		for (String perm : this.cmdBlock.getPermissions()) {
			if (!this.player.hasPermission(perm)) {
				perms.setPermission(perm, true);
			}
		}

		for (String command : this.cmdBlock.getCommands()) {
			handleCommand(command);
		}

		for (String perm : this.cmdBlock.getPermissions()) {
			if (perms.getPermissions().containsKey(perm)) {
				perms.unsetPermission(perm);
			}
		}

		return true;
	}

	private void handleCommand(String command) {
		String cmd = formatCommand(command, this.player);
		char special = cmd.charAt(0);
		if (special == Settings.opChar){
			cmd = "/" + cmd.substring(1);
			if (!this.player.isOp()) {
				this.player.setOp(true);
				this.player.chat(cmd);
				this.player.setOp(false);
			}
			else {
				this.player.chat(cmd);
			}
		}
		else if (special == Settings.serverChar) {
			cmd = cmd.substring(1);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
		}
		else {
			this.player.chat(cmd);
		}
	}

	private String formatCommand (String command, Player player) {
		String cmd = new String(command);

		cmd = cmd.replaceAll("%player%", player.getName());
		cmd = cmd.replaceAll("%PLAYER%", player.getName());

		return cmd;
	}
}
