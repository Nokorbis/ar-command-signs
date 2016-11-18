package net.bendercraft.spigot.commandsigns.menu;

import net.bendercraft.spigot.commandsigns.utils.Messages;

import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;

public class CommandsEditMenu extends EditionMenu {

	public CommandsEditMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.edit"));
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
		config.getEditor().sendMessage(Messages.get("menu.edit_command"));
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().editCommand(index - 1, args[1]);
		}
		catch (Exception e) {
		}
	}

}
