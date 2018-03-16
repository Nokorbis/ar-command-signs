package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.controller.Economy;
import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

public class EconomyMenu extends EditionMenu {

	public EconomyMenu(EditionMenu parent) {
		super(parent, Messages.get("menu.economy"));
	}
	
	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		String msg = Messages.get("menu.economy_edit");
		msg = msg.replace("{PRICE}", String.valueOf(config.getEditingData().getEconomyPrice()));
		config.getEditor().sendMessage(msg);
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			String[] args = message.split(" ");
			Double value = Double.parseDouble(args[0]);
			config.getEditingData().setEconomyPrice(value);
		}
		catch (Exception ignored) {
		}
		config.setCurrentMenu(getParent());
	}

	@Override
	public String formatName(CommandBlock cmdB) {
		String msg = getName();
		msg = msg.replace("{PRICE}", Economy.getEconomy().format(cmdB.getEconomyPrice()));
		return msg;
	}
}
