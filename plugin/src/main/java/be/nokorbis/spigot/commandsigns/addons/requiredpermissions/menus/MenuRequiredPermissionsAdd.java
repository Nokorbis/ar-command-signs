package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsAdd extends AddonEditionLeaf {

	public MenuRequiredPermissionsAdd(RequiredPermissionsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.required_permissions.add.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return "";
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		String msg = messages.get("menu.required_permissions.add.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		if (!CANCEL_STRING.equals(message)) {
			final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
			configurationData.getRequiredPermissions().add(message);
		}
		navigationContext.setAddonMenu(getParent());
	}
}
