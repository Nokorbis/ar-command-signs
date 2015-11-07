package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class CommandsAddMenu extends EditionMenu {

	public CommandsAddMenu(EditionMenu parent) {
		super(parent, "Add");
	}
	
	@Override
	public void display(EditingConf<CommandBlock> config) {
		if (config.getEditor() != null) {
			config.getEditor().sendMessage(c + "Enter the command string : ");
		}
	}

	@Override
	public void input(EditingConf<CommandBlock> config, String message) {
		if (config.getEditingData() != null) {
			config.getEditingData().addCommand(message);
		}
		config.setCurrentMenu(getParent());
	}

}
