package net.avatar.realms.spigot.commandsign.data;

import java.util.List;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;

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
	
	public static CommandBlockData transform (CommandBlock cmd) {
		CommandBlockData data = new CommandBlockData();
		
		data.setBlock(BlockData.transform(cmd.getBlock()));
		data.setCommands(cmd.getCommands());
		data.setPermissions(cmd.getPermissions());
		data.setNeededPermissions(cmd.getNeededPermissions());
		
		return data;
	}
	
	public static CommandBlock transform (CommandBlockData data) {
		CommandBlock cmd = new CommandBlock();
		
		cmd.setBlock(BlockData.transform(data.getBlock()));
		
		for (String str : data.getCommands()) {
			cmd.addCommand(str);
		}
		
		for (String str : data.getPermissions()) {
			cmd.addPermission(str);
		}
		
		for (String str : data.getNeededPermissions()) {
			cmd.addNeededPermission(str);
		}
		
		return cmd;
	}
	
	
}
