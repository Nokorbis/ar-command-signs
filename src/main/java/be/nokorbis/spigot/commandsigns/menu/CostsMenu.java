package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;
import org.bukkit.entity.Player;

import java.util.Map;

public class CostsMenu extends EditionMenu {

	public CostsMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.costs_title"));
		this.subMenus.put(2, new EconomyMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
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
