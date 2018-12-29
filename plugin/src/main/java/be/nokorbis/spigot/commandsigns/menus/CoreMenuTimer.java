package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class CoreMenuTimer extends EditionNodeCore {

	public CoreMenuTimer(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.timer.title"), parent);
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new CoreMenuTimerTime(this));
		addMenu(new CoreMenuTimerCancel(this));
		addMenu(new CoreMenuTimerReset(this));
	}
}
