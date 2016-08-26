package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class TimerPlayerUsageSetMenu extends EditionMenu {

	public TimerPlayerUsageSetMenu(EditionMenu parent) {
		super(parent, Messages.get("info.time_between_player_usage"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("menu.time_between_player_edit"));

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
		return getName().replace("{TIME}", String.valueOf(cmd.getTimeBetweenPlayerUsage()));
	}

}
