package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.EditingConf;

public interface IEditionMenu {

	public void display(EditingConf config);

	public void input(EditingConf config, String message);

	public String getName();
	
}
