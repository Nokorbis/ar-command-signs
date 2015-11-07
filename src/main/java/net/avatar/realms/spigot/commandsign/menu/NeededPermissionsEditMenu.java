package net.avatar.realms.spigot.commandsign.menu;

import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class NeededPermissionsEditMenu extends EditionMenu {
	
	public NeededPermissionsEditMenu(EditionMenu parent) {
		super(parent, "Edit");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Needed permissions : ");
		int cpt = 1;
		for (String perm : config.getEditingData().getNeededPermissions()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		config.getEditor().sendMessage(c + "Enter the index of the permission you want to edit followed by the new permission string : ");
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
