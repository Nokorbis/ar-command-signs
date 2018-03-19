package be.nokorbis.spigot.commandsigns.menu;

import be.nokorbis.spigot.commandsigns.controller.EditingConfiguration;

public interface IEditionMenu<T> {
	
	void display(EditingConfiguration<T> config);
	
	void input(EditingConfiguration<T> config, String message);

}
