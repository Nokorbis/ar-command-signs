package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class TemporaryPermissionsMenu extends EditionMenu {
	
	public TemporaryPermissionsMenu(EditionMenu parent) {
		super(parent, "Temporary Permissions");
		this.subMenus.put(2, new TemporaryPermissionsAddMenu(this));
		this.subMenus.put(3, new TemporaryPermissionsEditMenu(this));
		this.subMenus.put(4, new TemporaryPermissionsRemoveMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
				//List current permissions
				editor.sendMessage(c + getName() + ": ");
				for (int i = 0; i < cmd.getPermissions().size(); i++) {
					editor.sendMessage(ChatColor.GRAY + " ---" + (i + 1) + ") " + cmd.getPermissions().get(i));
				}

				//List submenus
				editor.sendMessage(c + "1. Refresh");
				for (Entry<Integer, EditionMenu> menu : this.subMenus.entrySet()) {
					editor.sendMessage(c + "" + menu.getKey() + ". " + menu.getValue().getName());
				}
				editor.sendMessage(ChatColor.GREEN + "9. Done");
			}
		}
	}
	
	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			String[] args = message.split(" ");
			if (args.length == 0) {
				//No parameters, let's do nothing so that he receives the display message again
				return;
			}
			int index = Integer.parseInt(args[0]);
			if (index == 9) {
				config.setCurrentMenu(getParent());
			}
			else if (this.subMenus.containsKey(index)) {
				IEditionMenu<CommandBlock> newMenu = this.subMenus.get(index);
				config.setCurrentMenu(newMenu);
			}
			else {
				// let's do nothing so that he receives the display message again
			}
		}
		catch (NumberFormatException ex) {
			config.getEditor().sendMessage(ChatColor.DARK_RED + "You must enter a number to go through the menu.");
		}
	}
}
