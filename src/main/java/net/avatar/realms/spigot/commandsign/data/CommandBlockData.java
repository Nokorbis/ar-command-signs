package net.avatar.realms.spigot.commandsign.data;

import java.util.List;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandBlockData {

	private LocationData location;

	private Long id;
	private String name;
	private List<String> commands;
	private List<String> permissions;
	private List<String> neededPermissions;
	private Double economyPrice;
	private Integer time; // Time before execution
	private Boolean cancelOnMove;
	private Boolean resetOnMove;
	private Integer timeBetweenUsage;
	private Integer timeBetweenPlayerUsage;

	public CommandBlockData() {

	}

	public LocationData getLocation() {
		return this.location;
	}

	public void setLocation(LocationData block) {
		this.location = block;
	}

	public List<String> getCommands() {
		return this.commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public List<String> getPermissions() {
		return this.permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<String> getNeededPermissions() {
		return this.neededPermissions;
	}

	public void setNeededPermissions(List<String> neededPermissions) {
		this.neededPermissions = neededPermissions;
	}

	public Integer getTime () {
		return this.time;
	}

	public void setTime (Integer time) {
		this.time = time;
	}

	public Boolean getCancelOnMove () {
		return this.cancelOnMove;
	}

	public void setCancelOnMove (Boolean cancelOnMove) {
		this.cancelOnMove = cancelOnMove;
	}

	public Boolean getResetOnMove () {
		return this.resetOnMove;
	}

	public void setResetOnMove (Boolean resetOnMove) {
		this.resetOnMove = resetOnMove;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static CommandBlockData transform (CommandBlock cmd) {
		CommandBlockData data = new CommandBlockData();

		data.setId(cmd.getId());
		data.setLocation(LocationData.transform(cmd.getLocation()));
		data.setCommands(cmd.getCommands());
		data.setPermissions(cmd.getPermissions());
		data.setNeededPermissions(cmd.getNeededPermissions());
		data.setEconomyPrice(cmd.getEconomyPrice());
		data.setTime(cmd.getTimer());
		data.setCancelOnMove(cmd.isCancelledOnMove());
		data.setResetOnMove(cmd.isResetOnMove());
		data.setTimeBetweenPlayerUsage(cmd.getTimeBetweenPlayerUsage());
		data.setTimeBetweenUsage(cmd.getTimeBetweenUsage());
		data.setName(cmd.getName());

		return data;
	}

	public static CommandBlock transform (CommandBlockData data) {
		CommandBlock cmd = null;
		if (data.getId() == null) {
			cmd = new CommandBlock();
		}
		else {
			cmd = new CommandBlock(data.getId());
		}

		cmd.setLocation(LocationData.transform(data.getLocation()));

		if (cmd.getLocation() == null) {
			CommandSign.getPlugin().getLogger().warning("Block is null, command block cannot exist... deleting command block");
			return null;
		}

		for (String str : data.getCommands()) {
			cmd.addCommand(str);
		}

		for (String str : data.getPermissions()) {
			cmd.addPermission(str);
		}

		for (String str : data.getNeededPermissions()) {
			cmd.addNeededPermission(str);
		}

		cmd.setEconomyPrice(data.getEconomyPrice());

		cmd.setTimer(data.getTime());
		cmd.setCancelledOnMove(data.getCancelOnMove());
		cmd.setResetOnMove(data.getResetOnMove());

		if (data.getTimeBetweenPlayerUsage() != null) {
			cmd.setTimeBetweenPlayerUsage(data.getTimeBetweenPlayerUsage());
		}

		if (data.getTimeBetweenUsage() != null) {
			cmd.setTimeBetweenUsage(data.getTimeBetweenUsage());
		}

		cmd.setName(data.getName());

		return cmd;
	}

	public Double getEconomyPrice() {
		return this.economyPrice;
	}

	public void setEconomyPrice(Double economyPrice) {
		this.economyPrice = economyPrice;
	}

	public Integer getTimeBetweenUsage() {
		return this.timeBetweenUsage;
	}

	public void setTimeBetweenUsage(Integer timeBetweenUsage) {
		this.timeBetweenUsage = timeBetweenUsage;
	}

	public Integer getTimeBetweenPlayerUsage() {
		return this.timeBetweenPlayerUsage;
	}

	public void setTimeBetweenPlayerUsage(Integer timeBetweenCommands) {
		this.timeBetweenPlayerUsage = timeBetweenCommands;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
