package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.*;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class CoreMenuRequirements extends CoreAddonSubmenusHandler {
	public CoreMenuRequirements(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.requirements.title"), parent);
	}
}
