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
		config.getEditor().sendMessage(c + Messages.get("menu.cancel_edit") + " (Yes/No)");
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		String[] args = message.split(" ");
		if (args[0].equalsIgnoreCase("Yes") || args[0].equalsIgnoreCase("Y") || args[0].equalsIgnoreCase("True")) {
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
		StringBuilder build = new StringBuilder();
		build.append(getName());
		build.append(" (");
		build.append(cmd.isCancelledOnMove() ? Messages.get("menu.yes") : Messages.get("menu.no"));
		build.append(")");
		return build.toString();
	}

}
