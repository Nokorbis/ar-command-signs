package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class TimerResetMenu extends EditionMenu {
	
	public TimerResetMenu(EditionMenu parent) {
		super(parent, "Reset on move");
	}

	@Override
	public void display(EditingConf<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Should the timer be reset when the player moves ? (Yes/No)");
	}
	
	@Override
	public void input(EditingConf<CommandBlock> config, String message) {
		String[] args = message.split(" ");
		if (args[0].equalsIgnoreCase("Yes") || args[0].equalsIgnoreCase("Y") || args[0].equalsIgnoreCase("True")) {
			config.getEditingData().setResetOnMove(true);
		}
		else {
			if (!args[0].equals("CANCEL")) {
				config.getEditingData().setResetOnMove(false);
			}
		}
		config.setCurrentMenu(getParent());
	}

	@Override
	public String formatName(CommandBlock cmd) {
		//4. Reset on move (No)
		StringBuilder build = new StringBuilder();
		build.append(getName());
		build.append(" (");
		build.append(cmd.isResetOnMove() ? "Yes" : "No");
		build.append(")");
		return build.toString();
	}
	
}
