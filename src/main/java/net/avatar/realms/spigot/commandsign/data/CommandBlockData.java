package net.avatar.realms.spigot.commandsign.data;

import java.util.List;

public class CommandBlockData {

	private BlockData block;
	
	private List<String> commands;
	private List<String> permissions;
	private List<String> neededPermissions;
	
	public CommandBlockData() {
		
	}

	public BlockData getBlock() {
		return block;
	}

	public void setBlock(BlockData block) {
		this.block = block;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void setCommands(List<String> commands) {
		this.commands = commands;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public List<String> getNeededPermissions() {
		return neededPermissions;
	}

	public void setNeededPermissions(List<String> neededPermissions) {
		this.neededPermissions = neededPermissions;
	}
	
	
}
