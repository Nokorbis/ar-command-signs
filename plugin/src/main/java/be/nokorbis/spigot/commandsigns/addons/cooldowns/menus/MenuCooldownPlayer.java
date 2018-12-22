package be.nokorbis.spigot.commandsigns.addons.cooldowns.menus;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuCooldownPlayer extends EditionLeaf<AddonConfigurationData> {

	public MenuCooldownPlayer(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.cooldowns_player_title"), parent);
	}

	@Override
	public String getDataString(AddonConfigurationData data) {
		CooldownConfigurationData configurationData = (CooldownConfigurationData) data;
		return name.replace("{TIME}", String.valueOf(configurationData.getPlayerCooldown()));
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		editor.sendMessage(Messages.get("menu.cooldown_player_time"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			CooldownConfigurationData configurationData = (CooldownConfigurationData) data;
			String[] args = message.split(" ", 2);
			long duration = Long.parseLong(args[0]);
			configurationData.setPlayerCooldown(duration);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
