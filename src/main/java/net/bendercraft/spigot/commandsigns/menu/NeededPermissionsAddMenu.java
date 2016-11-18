package net.bendercraft.spigot.commandsigns.menu;

import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.utils.Messages;

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
