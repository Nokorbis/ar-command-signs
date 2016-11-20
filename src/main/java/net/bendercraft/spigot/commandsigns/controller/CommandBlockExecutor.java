package net.bendercraft.spigot.commandsigns.controller;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.bendercraft.spigot.commandsigns.utils.Messages;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.bendercraft.spigot.commandsigns.utils.Settings;

public class CommandBlockExecutor {

	private static final Pattern ALL_PATTERN = Pattern.compile("%[Aa][Ll][Ll]%");
	private static final Pattern RADIUS_PATTERN = Pattern.compile("%[Rr][Aa][Dd][Ii][Uu][Ss]=(\\d+)%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%[Pp][Ll][Aa][Yy][Ee][Rr]%");

	private static DecimalFormat df;

	private final Player player;
	private final CommandBlock cmdBlock;

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

	public void checkRequirements() throws CommandSignsException {
		if (this.player == null) {
			throw new CommandSignsException(Messages.get("usage.invalid_player"));
		}

		for (String needed : this.cmdBlock.getNeededPermissions()) {
			if (!this.player.hasPermission(needed)) {
				String err = Messages.get("usage.miss_needed_permission");
				err = err.replace("{NEEDED_PERM}", needed);
				throw new CommandSignsException(err);
			}
		}

		if (this.cmdBlock.getTimeBetweenUsage() > 0){
			long now = System.currentTimeMillis();
			long toWait = this.cmdBlock.getLastTimeUsed() + (this.cmdBlock.getTimeBetweenUsage()*1000) - now;
			if (toWait > 0) {
				if (!this.player.hasPermission("commandsign.timer.bypass")) {
					String msg = Messages.get("usage.general_cooldown");
					msg = msg.replace("{TIME}", df.format(this.cmdBlock.getTimeBetweenUsage() - (toWait/1000.0)));
					msg = msg.replace("{REMAINING}", df.format(toWait/1000.0));
					throw new CommandSignsException(msg);
				}
			}
		}

		if (this.cmdBlock.getTimeBetweenPlayerUsage() > 0) {
			if (this.cmdBlock.hasPlayerRecentlyUsed(this.player)) {
				long now = System.currentTimeMillis();
				long toWait = this.cmdBlock.getLastTimePlayerRecentlyUsed(this.player) + (this.cmdBlock.getTimeBetweenUsage()*1000) - now;
				if (!player.hasPermission("commandsign.timer.bypass")) {
					String msg = Messages.get("usage.player_cooldown");
					msg = msg.replace("{TIME}", df.format(this.cmdBlock.getTimeBetweenUsage() - (toWait/1000.0)));
					msg = msg.replace("{REMAINING}", df.format(toWait/1000.0));
					throw new CommandSignsException(msg);
				}
			}
		}

		if ((Economy.getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0)) {
			if (!Economy.getEconomy().has(this.player, this.cmdBlock.getEconomyPrice()) && !this.player.hasPermission("commandsign.costs.bypass")) {
				String err = Messages.get("usage.not_enough_money");
				err = err.replace("{PRICE}", Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
				throw new CommandSignsException(err);
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

		if ((Economy.getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0)) {
			if (!this.player.hasPermission("commandsign.costs.bypass")) {
				if (Economy.getEconomy().has(this.player, this.cmdBlock.getEconomyPrice())) {
					Economy.getEconomy().withdrawPlayer(this.player, this.cmdBlock.getEconomyPrice());
					String msg = Messages.get("usage.you_paied");
					msg = msg.replace("{PRICE}",Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
					this.player.sendMessage(msg);
				}
				else {
					String err = Messages.get("usage.not_enough_money");
					err = err.replace("{PRICE}", Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
					this.player.sendMessage(err);
					return false;
				}
			}
		}

		if (this.cmdBlock.getTimeBetweenPlayerUsage() > 0) {
			if (this.cmdBlock.hasPlayerRecentlyUsed(this.player)) {
				if (!player.hasPermission("commandsign.timer.bypass")) {
					this.player.sendMessage(Messages.get("usage.player_cooldown"));
					return false;
				}
			}
			this.cmdBlock.addUsage(this.player);
		}

		PermissionAttachment perms = Container.getContainer().getPlayerPermissions(this.player);
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
		for (String cmd : formatCommand(command, this.player)) {
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
	}

	private List<String> formatCommand (String command, Player player) {
		List<String> cmds = new LinkedList<String>();
		String cmd = new String(command);

		Matcher m = PLAYER_PATTERN.matcher(cmd);
		if (m.find()) {
			cmd = m.replaceAll(player.getName());
		}
		m = ALL_PATTERN.matcher(cmd);
		if (m.find()) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				cmds.add(m.replaceAll(p.getName()));
			}
		}
		else {
			m = RADIUS_PATTERN.matcher(cmd);
			if (m.find()) {
				try {
					String str = m.group(1);
					int radius = Integer.parseInt(str);
					if (radius > 0) {
						for (Player p : Bukkit.getOnlinePlayers()) {
							if (p.getWorld().equals(player.getWorld()) && p.getLocation().distance(player.getLocation()) <= radius) {
								cmds.add(m.replaceAll(p.getName()));
							}
						}
					}
				}
				catch (Exception ex) {
				}
			}
			else {
				cmds.add(cmd);
			}
		}

		return cmds;
	}
}
