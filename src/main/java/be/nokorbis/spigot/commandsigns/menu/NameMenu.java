package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

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
