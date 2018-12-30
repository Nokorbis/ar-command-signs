package be.nokorbis.spigot.commandsigns.addons.cooldowns.menus;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.data.CooldownConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuCooldownGlobal extends AddonEditionLeaf {

	public MenuCooldownGlobal(final Addon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.cooldowns.global.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		CooldownConfigurationData configurationData = (CooldownConfigurationData) data;
		return String.valueOf(configurationData.getGlobalCooldown());
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final String msg = messages.get("menu.cooldowns.global.edit");
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
				long duration = Long.parseLong(args[0]);
				configurationData.setGlobalCooldown(duration);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
