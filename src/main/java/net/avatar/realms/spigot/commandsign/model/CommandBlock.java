package net.avatar.realms.spigot.commandsign.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.utils.CommandSignUtils;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class CommandBlock {

	private static Set<Long> usedIds = new HashSet<Long>();
	private static Long biggerUsedId = 0L;

	private long id;
	private String name;

	private Location location;

	private List<String> neededPermissions;
	private List<String> commands;
	private List<String> permissions;

	private Double economyPrice;

	private Integer timeBeforeExecution; // Value in second
	private Boolean resetOnMove;
	private Boolean cancelledOnMove;

	private int timeBetweenUsage;
	private int timeBetweenPlayerUsage;

	private long lastTimeUsed;
	private Map<UUID, Long> usages;

	public CommandBlock () {
		// We use ArrayList because we want to remove/edit them by the index.
		this.commands = new ArrayList<String>();
		this.permissions = new ArrayList<String>();
		this.neededPermissions = new ArrayList<String>();
		this.setTimer(0);
		this.resetOnMove = false;
		this.cancelledOnMove = false;
		this.setEconomyPrice(0.0);

		this.setTimeBetweenUsage(0);
		this.timeBetweenPlayerUsage = 0;
		this.lastTimeUsed = 0;
		this.setId(getFreeId());

		this.usages = new HashMap<UUID, Long>();
	}

	public CommandBlock (Long id) {
		this.commands = new ArrayList<String>();
		this.permissions = new ArrayList<String>();
		this.neededPermissions = new ArrayList<String>();
		this.setTimer(0);
		this.resetOnMove = false;
		this.cancelledOnMove = false;
		this.setEconomyPrice(0.0);

		this.setTimeBetweenUsage(0);
		this.lastTimeUsed = 0;
		if (usedIds.contains(id)) {
			CommandSign.getPlugin().getLogger().warning("A strange error occured : It seems that the registered id (" + id + ") is already in used... Getting a new one..."); 
			id = getFreeId();
		}
		this.setId(id);
	}

	private static long getFreeId() {
		for (long i = 0; i <= biggerUsedId; i++) {
			if (!usedIds.contains(i)) {
				return i;
			}
		}
		return ++biggerUsedId;
	}

	/* Getters and setters */

	/* Id */

	private void setId(long id) {
		this.id = id;
		usedIds.add(id);

		if (id > biggerUsedId){
			biggerUsedId = id;
		}
	}

	public long getId() {
		return this.id;
	}

	/* Name */

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* Time between usage */

	public int getTimeBetweenUsage() {
		return this.timeBetweenUsage;
	}

	public void setTimeBetweenUsage(int timeBetweenUsage) {
		if (timeBetweenUsage < 0) {
			timeBetweenUsage = 0;
		}
		this.timeBetweenUsage = timeBetweenUsage;
	}

	/* Time between player usage */

	public int getTimeBetweenPlayerUsage() {
		return this.timeBetweenPlayerUsage;
	}

	public void setTimeBetweenPlayerUsage(int timeBetweenPlayerUsage) {
		if (timeBetweenPlayerUsage < 0) {
			timeBetweenPlayerUsage = 0;
		}
		this.timeBetweenPlayerUsage = timeBetweenPlayerUsage;
	}

	/* Last time used */

	public long getLastTimeUsed() {
		return this.lastTimeUsed;
	}

	public void refreshLastTime() {
		this.lastTimeUsed = System.currentTimeMillis();
	}

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
		this.commands.add(command);
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
		this.neededPermissions.add(permission);
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
		this.permissions.add(permission);
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
		return this.timeBeforeExecution;
	}

	public void setTimer (Integer timer) {
		if ((timer == null) || (timer < 0)) {
			timer = 0;
		}
		this.timeBeforeExecution = timer;
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
		return this.timeBeforeExecution >= 1;
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

	/* Player usages */
	public boolean hasPlayerRecentlyUsed(Player player) {
		if (this.timeBetweenPlayerUsage == 0) {
			return false;
		}
		long now = System.currentTimeMillis();
		LinkedList<UUID> toRemove = new LinkedList<UUID>();
		for (Entry<UUID, Long> entry : this.usages.entrySet()) {
			if (entry.getValue() + this.timeBetweenPlayerUsage < now) {
				toRemove.add(entry.getKey());
			}
		}
		for (UUID id : toRemove) {
			this.usages.remove(id);
		}

		return this.usages.containsKey(player.getUniqueId());
	}

	public void addUsage(Player player) {
		if (this.timeBetweenPlayerUsage != 0) {
			this.usages.put(player.getUniqueId(), System.currentTimeMillis());
		}
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

		if (this.economyPrice != null && this.economyPrice > 0) {
			newBlock.setEconomyPrice(this.economyPrice);
		}

		if (this.hasTimer()) {
			newBlock.setTimer(this.timeBeforeExecution);
		}

		if (this.cancelledOnMove != null && this.cancelledOnMove) {
			newBlock.setCancelledOnMove(true);
		}

		if (this.resetOnMove != null && this.resetOnMove) {
			newBlock.setResetOnMove(true);
		}

		if (this.timeBetweenUsage > 0) {
			newBlock.setTimeBetweenUsage(this.timeBetweenUsage);
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
		player.sendMessage(c + "Id : " + this.id);
		player.sendMessage(c + "Name : " + ((this.name == null)? Messages.NO_NAME : this.name));
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
		if ((this.timeBeforeExecution != null) && (this.timeBeforeExecution > 0)) {
			player.sendMessage(c + "Time before execution :");
			player.sendMessage(ChatColor.GRAY + "" + this.timeBeforeExecution + " seconds");
			if (this.cancelledOnMove) {
				player.sendMessage(ChatColor.GRAY + "---" + "Cancelled on move");
			}
			if (this.resetOnMove) {
				player.sendMessage(ChatColor.GRAY + "---" + "Reset on move");
			}
		}
		if (this.timeBetweenUsage > 0) {
			player.sendMessage(c + "Time between usages : " + this.timeBetweenUsage);
		}
		if (this.timeBetweenPlayerUsage > 0) {
			player.sendMessage(c + "Time between commands : " + this.timeBetweenPlayerUsage);
		}
	}

	public String blockSummary () {
		if (this.location == null) {
			return "";
		}
		String str = this.location.getBlock().getType() + " #" + this.location.getX() + ":" + this.location.getZ()+"(" +this.location.getY()+")";
		return str;
	}
}
