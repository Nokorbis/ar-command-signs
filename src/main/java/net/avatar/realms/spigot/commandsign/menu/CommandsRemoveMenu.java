package net.avatar.realms.spigot.commandsign.menu;

import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class CommandsRemoveMenu extends EditionMenu {

	public CommandsRemoveMenu(EditionMenu parent) {
		super(parent, "Remove");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String command : config.getEditingData().getCommands()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + command);
		}
		config.getEditor().sendMessage(c + "Enter the index of the command you want to remove : ");
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
