package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

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
			long duration = Long.parseLong(args[0]);
			config.getEditingData().setTimeBetweenPlayerUsage(duration);
		}
		catch (Exception ignored) {
		}

	}
	@Override
	public String formatName(CommandBlock cmd) {
		//2. Time (30)
		return getName().replace("{TIME}", String.valueOf(cmd.getTimeBetweenPlayerUsage()));
	}

}
