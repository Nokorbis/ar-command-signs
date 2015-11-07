package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public class EconomyMenu extends EditionMenu {

	public EconomyMenu(EditionMenu parent) {
		super(parent, "Economy");
	}
	
	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		config.getEditor().sendMessage(c + "Enter the new economy needed for this command block (currently : "
				+ config.getEditingData().getEconomyPrice() + ") :");
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
