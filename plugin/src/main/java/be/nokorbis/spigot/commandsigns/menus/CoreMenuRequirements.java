package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;

import java.util.List;
import java.util.Map;


public class CoreMenuRequirements extends CoreAddonSubmenusHandler {

	private Map<Addon, List<EditionMenu<AddonConfigurationData>>> subMenusByAddon = null;

	public CoreMenuRequirements(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.requirements_title"), parent);
	}

}
