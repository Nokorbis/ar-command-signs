package be.nokorbis.spigot.commandsigns.addons.cooldowns.menus;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.data.CooldownConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import org.bukkit.entity.Player;


public class MenuCooldownPlayerOnce extends AddonEditionLeaf {

	public MenuCooldownPlayerOnce(Addon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.cooldowns.player_once.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		CooldownConfigurationData configurationData = (CooldownConfigurationData) data;
		if (configurationData.isPlayerOnlyOnce()) {
			return messages.get("menu.value.yes");
		}
		return messages.get("menu.value.no");
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final String msg = messages.get("menu.cooldowns.player_once.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				CooldownConfigurationData configurationData = (CooldownConfigurationData) data;
				String[] args = message.split(" ", 2);
				String val = args[0].toUpperCase();
				if ("Y".equals(val) || "YES".equals(val) || "TRUE".equals(val)) {
					configurationData.setPlayerOnlyOnce(true);
				}
				else {
					configurationData.setPlayerOnlyOnce(false);
				}
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
