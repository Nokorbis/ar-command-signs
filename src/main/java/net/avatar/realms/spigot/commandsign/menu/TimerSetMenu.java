package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class TimerSetMenu extends EditionMenu {
	
	public TimerSetMenu(EditionMenu parent) {
		super(parent, "Time");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(
				c + "Enter the amount of time (in seconds) that the player must wait before the execution of the command");
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().setTimer(index);
		}
		catch (Exception e) {
		}
	}

	@Override
	public String formatName(CommandBlock cmd) {
		//2. Time (30)
		StringBuilder build = new StringBuilder();
		build.append(getName());
		build.append(" (");
		build.append(cmd.getTimer());
		build.append(")");
		return build.toString();
	}
}
