package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissionsAdd extends AddonEditionLeaf {

	public MenuTemporaryPermissionsAdd(CommandsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.temporary_permissions.add.title"), parent);
	}

	@Override
	public String getDisplayString(AddonConfigurationData data) {
		return messages.get("menu.entry.display_name_only").replace("{NAME}", name);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return "";
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		String msg = messages.get("menu.temporary_permissions.add.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		if (!CANCEL_STRING.equals(message)) {
			final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
			configurationData.getTemporarilyGrantedPermissions().add(message);
		}
		navigationContext.setAddonMenu(getParent());
	}
}
