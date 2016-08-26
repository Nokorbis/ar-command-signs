package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandsMenu extends EditionMenu {
	
	public CommandsMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.commands_title"));

		this.subMenus.put(2, new CommandsAddMenu(this));
		this.subMenus.put(3, new CommandsEditMenu(this));
		this.subMenus.put(4, new CommandsRemoveMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
				//List current commands
				editor.sendMessage(getName());
				String format = Messages.get("info.command_format");
				String msg;
				int cpt = 1;
				for (String cmds : config.getEditingData().getCommands()) {
					msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", cmds);
					config.getEditor().sendMessage(msg);
				}

				//List submenus
				editor.sendMessage(Messages.get("menu.refresh"));
				for (Entry<Integer, EditionMenu> menu : this.subMenus.entrySet()) {
					String menuFormat = Messages.get("menu.format");
					menuFormat = menuFormat.replace("{NUMBER}", String.valueOf(menu.getKey())).replace("{MENU}", menu.getValue().getName());
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
