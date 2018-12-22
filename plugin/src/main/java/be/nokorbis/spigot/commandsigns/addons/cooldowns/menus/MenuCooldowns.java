package be.nokorbis.spigot.commandsigns.addons.cooldowns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.utils.Messages;


public class MenuCooldowns extends EditionNode<AddonConfigurationData> {

	public MenuCooldowns(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.cooldowns_title"), parent);
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuCooldownGlobal(this));
		addMenu(new MenuCooldownPlayer(this));
	}
}
