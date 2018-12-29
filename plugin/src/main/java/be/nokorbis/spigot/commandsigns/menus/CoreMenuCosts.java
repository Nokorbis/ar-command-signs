package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class CoreMenuCosts extends CoreAddonSubmenusHandler {

	public CoreMenuCosts(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.costs.title"), parent);
	}

}
