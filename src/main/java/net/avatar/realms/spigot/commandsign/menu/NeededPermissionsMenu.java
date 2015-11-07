package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class NeededPermissionsMenu extends EditionMenu {

	public NeededPermissionsMenu(EditionMenu parent) {
		super(parent, "Needed permissions");

		this.subMenus.put(2, new NeededPermissionsAddMenu(this));
		this.subMenus.put(3, new NeededPermissionsEditMenu(this));
		this.subMenus.put(4, new NeededPermissionsRemoveMenu(this));
	}
	
	@Override
	public void display(EditingConf<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
				
				editor.sendMessage(c + getName() + ": ");
				//List current needed permissions
				for (int i = 0; i < cmd.getNeededPermissions().size(); i++) {
					editor.sendMessage(ChatColor.GRAY + " ---" + (i + 1) + ") " + cmd.getNeededPermissions().get(i));
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
	public void input(EditingConf<CommandBlock> config, String message) {
		// TODO Auto-generated method stub

	}

}
