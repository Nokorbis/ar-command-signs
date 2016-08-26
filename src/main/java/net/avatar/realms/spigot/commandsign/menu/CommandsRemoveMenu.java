package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.utils.Messages;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandsRemoveMenu extends EditionMenu {

	public CommandsRemoveMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.remove"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("info.commands"));
		int cpt = 1;
		String format = Messages.get("info.command_format");
		String msg;
		for (String cmd : config.getEditingData().getCommands()) {
			msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", cmd);
			config.getEditor().sendMessage(msg);
		}
		config.getEditor().sendMessage(Messages.get("menu.delete_command"));
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().removeCommand(index - 1);
		}
		catch (Exception e) {
		}
	}

}
