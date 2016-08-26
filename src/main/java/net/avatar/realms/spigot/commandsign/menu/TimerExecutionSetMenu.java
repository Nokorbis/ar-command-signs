package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class TimerExecutionSetMenu extends EditionMenu {

	public TimerExecutionSetMenu(EditionMenu parent) {
		super(parent, Messages.get("info.time_before_execution"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("menu.time_before_edit"));
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().setTimeBeforeExecution(index);
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
		build.append(cmd.getTimeBeforeExecution());
		build.append(")");
		return build.toString();
	}
}
