package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConf;

public class EconomyMenu extends EditionMenu {

	public EconomyMenu(EditionMenu parent) {
		super(parent, "Economy");
	}
	
	@Override
	public void display(EditingConf<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Enter the new economy needed for this command block (currently : "
				+ config.getEditingData().getEconomyPrice() + ") :");
	}

	@Override
	public void input(EditingConf<CommandBlock> config, String message) {
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
