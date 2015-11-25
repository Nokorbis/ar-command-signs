package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class NameMenu extends EditionMenu {

	public NameMenu(EditionMenu parent) {
		super(parent, "Name");
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		if (config.getEditor() != null) {
			StringBuilder builder = new StringBuilder();
			builder.append("Enter the name of the command block (currently : " );
			if (config.getEditingData().getName() == null) {
				builder.append(Messages.NO_NAME);
			}
			else {
				builder.append(config.getEditingData().getName());
			}
			builder.append(") : ");
			config.getEditor().sendMessage(builder.toString());
		}
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		if (config.getEditingData() != null) {
			config.getEditingData().setName(message);
		}
		config.setCurrentMenu(getParent());
	}

	@Override
	public String formatName(CommandBlock cmd) {
		StringBuilder builder = new StringBuilder();
		builder.append(getName());
		builder.append(" : ");
		builder.append(cmd.getName());
		return builder.toString();
	}
}
