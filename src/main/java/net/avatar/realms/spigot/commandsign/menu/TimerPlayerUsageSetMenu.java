package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class TimerPlayerUsageSetMenu extends EditionMenu {

	public TimerPlayerUsageSetMenu(EditionMenu parent) {
		super(parent, "Time between usage per player");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(
				c + "Enter the amount of time (in seconds) that the player must wait between each usage:");

	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().setTimeBetweenPlayerUsage(index);
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
		build.append(cmd.getTimeBetweenPlayerUsage());
		build.append(")");
		return build.toString();
	}

}
