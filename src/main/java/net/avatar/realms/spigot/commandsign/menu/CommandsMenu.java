package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandsMenu extends EditionMenu {
	
	public CommandsMenu(EditionMenu parent) {
		super(parent, "Commands");

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
				editor.sendMessage(c + getName() + ": ");
				for (int i = 0; i < cmd.getCommands().size(); i++) {
					editor.sendMessage(ChatColor.GRAY + " ---" + (i + 1) + ") " + cmd.getCommands().get(i));
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
