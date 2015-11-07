package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class NeededPermissionsAddMenu extends EditionMenu {

	public NeededPermissionsAddMenu(EditionMenu parent) {
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
			config.getEditingData().addNeededPermission(message);
		}
		config.setCurrentMenu(getParent());
	}

}
