package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class NeededPermissionsRemoveMenu extends EditionMenu {
	
	public NeededPermissionsRemoveMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.remove"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + Messages.get("info.needed_permissions") + " : ");
		int cpt = 1;
		for (String perm : config.getEditingData().getNeededPermissions()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		config.getEditor().sendMessage(c + Messages.get("menu.remove_permission"));
		
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().removeNeededPermission(index - 1);
		}
		catch (Exception e) {
		}
	}
}
