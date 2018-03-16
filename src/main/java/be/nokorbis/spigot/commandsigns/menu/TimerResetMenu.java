package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

public class TimerResetMenu extends EditionMenu {
	
	public TimerResetMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.reset_title"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("menu.reset_edit"));
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		String[] args = message.split(" ");
		String arg = args[0].toUpperCase();
		if (arg.equals("YES") || arg.equals("Y") || arg.equals("TRUE")) {
			config.getEditingData().setResetOnMove(true);
		}
		else {
			if (!args[0].equals("CANCEL")) {
				config.getEditingData().setResetOnMove(false);
			}
		}
		config.setCurrentMenu(getParent());
	}

	@Override
	public String formatName(CommandBlock cmd) {
		//4. Reset on move (No)
		return getName().replace("{BOOLEAN}", cmd.isResetOnMove() ? Messages.get("menu.yes") : Messages.get("menu.no"));
	}
	
}
