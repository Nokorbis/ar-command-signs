package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;

public interface IEditionMenu<T> {
	
	void display(EditingConfiguration<T> config);
	
	void input(EditingConfiguration<T> config, String message);

}
