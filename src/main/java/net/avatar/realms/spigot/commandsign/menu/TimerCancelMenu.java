package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class TimerCancelMenu extends EditionMenu {

	public TimerCancelMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.cancel_title"));
	}
	
	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("menu.cancel_edit"));
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		String[] args = message.split(" ");
		String arg = args[0].toUpperCase();
		if (arg.equals("YES") || arg.equals("Y") || arg.equals("TRUE")) {
			config.getEditingData().setCancelledOnMove(true);
		}
		else {
			if (!args[0].equals("CANCEL")) {
				config.getEditingData().setCancelledOnMove(false);
			}
		}
		config.setCurrentMenu(getParent());
	}
	
	@Override
	public String formatName(CommandBlock cmd) {
		//3. Cancel on move (Yes)
		return getName().replace("{BOOLEAN}", cmd.isCancelledOnMove() ? Messages.get("menu.yes") : Messages.get("menu.no"));
	}

}
