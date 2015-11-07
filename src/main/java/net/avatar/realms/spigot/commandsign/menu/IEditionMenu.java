package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;

public interface IEditionMenu<T> {
	
	public void display(EditingConfiguration<T> config);
	
	public void input(EditingConfiguration<T> config, String message);

}
