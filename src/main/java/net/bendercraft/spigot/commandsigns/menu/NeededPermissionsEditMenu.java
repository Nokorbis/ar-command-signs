package net.bendercraft.spigot.commandsigns.menu;

import net.bendercraft.spigot.commandsigns.utils.Messages;

import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;

public class NeededPermissionsEditMenu extends EditionMenu {
	
	public NeededPermissionsEditMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.edit"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(Messages.get("info.needed_permissions") + " : ");
		int cpt = 1;
		String format = Messages.get("info.permission_format");
		String msg;
		for (String perm : config.getEditingData().getNeededPermissions()) {
			msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			config.getEditor().sendMessage(msg);
		}
		config.getEditor().sendMessage(Messages.get("menu.edit_permission"));
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().editNeededPermission(index - 1, args[1]);
		}
		catch (Exception e) {
		}
	}
}
