package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

public class TemporaryPermissionsAddMenu extends EditionMenu {
	
	public TemporaryPermissionsAddMenu(EditionMenu parent) {
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
			config.getEditingData().addPermission(message);
		}
		config.setCurrentMenu(getParent());
	}
}
