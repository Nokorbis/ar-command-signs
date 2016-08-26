package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class NeededPermissionsAddMenu extends EditionMenu {

	public NeededPermissionsAddMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.add"));
	}
	
	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		if (config.getEditor() != null) {
			config.getEditor().sendMessage(Messages.get("menu.enter_permission"));
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
