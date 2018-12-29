package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsAdd extends EditionLeaf<AddonConfigurationData> {

	public MenuRequiredPermissionsAdd(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.add"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		editor.sendMessage(Messages.get("menu.enter_permission"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
		configurationData.getRequiredPermissions().add(message);
		navigationContext.setAddonMenu(getParent());
	}
}
