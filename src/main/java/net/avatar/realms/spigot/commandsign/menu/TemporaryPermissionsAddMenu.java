package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class TemporaryPermissionsAddMenu extends EditionMenu {
	
	public TemporaryPermissionsAddMenu(EditionMenu parent) {
		super(parent, "Add");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		if (config.getEditor() != null) {
			config.getEditor().sendMessage(c + "Enter the needed permission string : ");
		}
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		if (config.getEditingData() != null) {
			config.getEditingData().addPermission(message);
		}
		config.setCurrentMenu(getParent());
	}
}
