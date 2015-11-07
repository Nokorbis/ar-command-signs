package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class TemporaryPermissionsAddMenu extends EditionMenu {
	
	public TemporaryPermissionsAddMenu(EditionMenu parent) {
		super(parent, "Add");
	}

	@Override
	public void display(EditingConf<CommandBlock> config) {
		if (config.getEditor() != null) {
			config.getEditor().sendMessage(c + "Enter the needed permission string : ");
		}
	}
	
	@Override
	public void input(EditingConf<CommandBlock> config, String message) {
		if (config.getEditingData() != null) {
			config.getEditingData().addPermission(message);
		}
		config.setCurrentMenu(getParent());
	}
}
