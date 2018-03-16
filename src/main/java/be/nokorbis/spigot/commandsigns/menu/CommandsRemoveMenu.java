package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;

import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

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
		catch (Exception ignored) {
		}
	}

}
