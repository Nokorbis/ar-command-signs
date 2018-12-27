package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;


public class CoreMenuTimer extends EditionNodeCore {

	public CoreMenuTimer(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.timer"), parent);
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new CoreMenuTimerTime(this));
		addMenu(new CoreMenuTimerCancel(this));
		addMenu(new CoreMenuTimerReset(this));
	}
}
