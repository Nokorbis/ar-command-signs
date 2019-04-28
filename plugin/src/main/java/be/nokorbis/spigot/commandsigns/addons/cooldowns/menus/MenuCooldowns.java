package be.nokorbis.spigot.commandsigns.addons.cooldowns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionNode;


public class MenuCooldowns extends AddonEditionNode {

	public MenuCooldowns(Addon addon) {
		super(addon, messages.get("menu.cooldowns.title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuCooldownGlobal(addon, this));
		addMenu(new MenuCooldownPlayer(addon, this));
		addMenu(new MenuCooldownGlobalOnce(addon, this));
		addMenu(new MenuCooldownPlayerOnce(addon, this));
	}
}
