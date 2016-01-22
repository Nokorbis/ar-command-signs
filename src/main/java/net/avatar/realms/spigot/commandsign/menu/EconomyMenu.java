package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class EconomyMenu extends EditionMenu {

	public EconomyMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.economy_title"));
	}
	
	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		String msg = Messages.get("menu.economy_edit");
		msg = msg.replaceAll("\\{PRICE\\}", String.valueOf(config.getEditingData().getEconomyPrice()));
		config.getEditor().sendMessage(c + msg);
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			String[] args = message.split(" ");
			Double value = Double.parseDouble(args[0]);
			config.getEditingData().setEconomyPrice(value);
		}
		catch (Exception e) {
		}
		config.setCurrentMenu(getParent());
	}
}
