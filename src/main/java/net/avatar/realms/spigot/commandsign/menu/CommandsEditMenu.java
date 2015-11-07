package net.avatar.realms.spigot.commandsign.menu;

import org.bukkit.ChatColor;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class CommandsEditMenu extends EditionMenu {
	
	public CommandsEditMenu(EditionMenu parent) {
		super(parent, "Edit");
	}

	@Override
	public void display(EditingConf<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Commands : ");
		int cpt = 1;
		for (String perm : config.getEditingData().getNeededPermissions()) {
			config.getEditor().sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
		}
		config.getEditor()
				.sendMessage(c + "Enter the index of the command you want to edit followed by the new command string : ");
	}
	
	@Override
	public void input(EditingConf<CommandBlock> config, String message) {
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
