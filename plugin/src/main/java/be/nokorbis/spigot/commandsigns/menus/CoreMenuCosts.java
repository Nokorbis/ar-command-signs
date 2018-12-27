package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;


public class CoreMenuCosts extends CoreAddonSubmenusHandler {

	public CoreMenuCosts(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.costs_title"), parent);
	}

}
