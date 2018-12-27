package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;


public class CoreMenuExecutions extends CoreAddonSubmenusHandler {

	public CoreMenuExecutions(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.executions_title"), parent);
	}

}
