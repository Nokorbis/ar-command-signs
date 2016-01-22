package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandsRemoveMenu extends EditionMenu {

	public CommandsRemoveMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.remove"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + Messages.get("info.commands") +" : ");
		int cpt = 1;
		for (String command : config.getEditingData().getCommands()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + command);
		}
		config.getEditor().sendMessage(c + Messages.get("menu.delete_command"));
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().removeCommand(index - 1);
		}
		catch (Exception e) {
		}
	}

}
