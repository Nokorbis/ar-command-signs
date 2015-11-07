package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.model.EditingConf;

public interface IEditionMenu<T> {

	public void display(EditingConf<T> config);

	public void input(EditingConf<T> config, String message);
}
