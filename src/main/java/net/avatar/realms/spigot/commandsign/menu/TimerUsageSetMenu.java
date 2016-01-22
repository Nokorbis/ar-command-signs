package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class TimerUsageSetMenu extends EditionMenu {

	public TimerUsageSetMenu(EditionMenu parent) {
		super(parent, Messages.get("info.time_between_usages"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + Messages.get("menu.time_between_edit"));

	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().setTimeBetweenUsage(index);
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
		build.append(cmd.getTimeBetweenUsage());
		build.append(")");
		return build.toString();
	}

}
