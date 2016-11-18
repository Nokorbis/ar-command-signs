package net.bendercraft.spigot.commandsigns.menu;

import java.util.Map;

import net.bendercraft.spigot.commandsigns.utils.Messages;
import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;

public class TimerMenu extends EditionMenu {

	public TimerMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.timer"));
		this.subMenus.put(2, new TimerExecutionSetMenu(this));
		this.subMenus.put(3, new TimerCancelMenu(this));
		this.subMenus.put(4, new TimerResetMenu(this));
		this.subMenus.put(5, new TimerUsageSetMenu(this));
		this.subMenus.put(6, new TimerPlayerUsageSetMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
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
