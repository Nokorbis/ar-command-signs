package net.bendercraft.spigot.commandsigns.menu;

import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;

public interface IEditionMenu<T> {
	
	void display(EditingConfiguration<T> config);
	
	void input(EditingConfiguration<T> config, String message);

}
