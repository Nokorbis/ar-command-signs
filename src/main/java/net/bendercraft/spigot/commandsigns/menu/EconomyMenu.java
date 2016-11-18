package net.bendercraft.spigot.commandsigns.menu;

import net.bendercraft.spigot.commandsigns.controller.Economy;
import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.utils.Messages;

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
		catch (Exception e) {
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
