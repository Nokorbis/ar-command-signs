package net.avatar.realms.spigot.commandsign.menu;

import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class CommandsEditMenu extends EditionMenu {

	public CommandsEditMenu(EditionMenu parent) {
		super(parent, "Edit");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String cmd : config.getEditingData().getCommands()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
		}
		config.getEditor()
		.sendMessage(c + "Enter the index of the command you want to edit followed by the new command string : ");
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			config.setCurrentMenu(getParent());
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			config.getEditingData().editCommand(index - 1, args[1]);
		}
		catch (Exception e) {
		}
	}

}
