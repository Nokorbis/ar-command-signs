package be.nokorbis.spigot.commandsigns.menus.old;

import java.util.Map;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;
import org.bukkit.entity.Player;

public class NeededPermissionsMenu extends EditionMenu {
	
	public NeededPermissionsMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.needed_perms_title"));
		
		this.subMenus.put(2, new NeededPermissionsAddMenu(this));
		this.subMenus.put(3, new NeededPermissionsEditMenu(this));
		this.subMenus.put(4, new NeededPermissionsRemoveMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
				//List current needed permissions
				editor.sendMessage(getName());
				String format = Messages.get("info.permission_format");
				String msg;
				int cpt = 1;
				for (String perm : config.getEditingData().getNeededPermissions()) {
					msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
					config.getEditor().sendMessage(msg);
				}
				
				//List submenus
				editor.sendMessage(Messages.get("menu.refresh"));
				for (Map.Entry<Integer, EditionMenu> menu : this.subMenus.entrySet()) {
					String menuFormat = Messages.get("menu.format");
					menuFormat = menuFormat.replace("{NUMBER}", String.valueOf(menu.getKey())).replace("{MENU}", menu.getValue().formatName(config.getEditingData()));
					editor.sendMessage(menuFormat);
				}
				editor.sendMessage(Messages.get("menu.done"));
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
			config.getEditor().sendMessage(Messages.get("menu.number_needed"));
		}
	}
	
}
