package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class NameMenu extends EditionMenu {

	public NameMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.name"));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		if (config.getEditor() != null) {
			String msg = Messages.get("menu.name_edit");
			msg = msg.replace("{NAME}", config.getEditingData().getName() == null ? Messages.get("info.no_name"): config.getEditingData().getName());
			config.getEditor().sendMessage(msg);
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
		String msg = Messages.get("info.name_format");
		if (cmd.getName() != null) {
			msg = msg.replace("{NAME}", cmd.getName());
		}
		else {
			msg = msg.replace("{NAME}", Messages.get("info.no_name"));
		}
		return msg;
	}
}
