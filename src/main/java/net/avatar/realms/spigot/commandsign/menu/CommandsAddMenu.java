package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandsAddMenu extends EditionMenu {

	public CommandsAddMenu(EditionMenu parent) {
		super(parent, "Add");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		if (config.getEditor() != null) {
			config.getEditor().sendMessage(c + "Enter the command string : ");
		}
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		if (config.getEditingData() != null) {
			config.getEditingData().addCommand(message);
		}
		config.setCurrentMenu(getParent());
	}

}
