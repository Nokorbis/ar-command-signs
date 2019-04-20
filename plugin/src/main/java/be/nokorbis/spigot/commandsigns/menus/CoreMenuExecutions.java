package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class CoreMenuExecutions extends EditionNodeCore {

	public CoreMenuExecutions(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.executions.title"), parent);
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new CoreMenuTemporaryPermissions(this));
		addMenu(new CoreMenuCommands(this));
	}
}
