package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissionsAdd extends EditionLeaf<AddonConfigurationData> {

	public MenuTemporaryPermissionsAdd(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.add"), parent);
	}

	@Override
	public String getDataString(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		editor.sendMessage(Messages.get("menu.enter_permission"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
		configurationData.getTemporarilyGrantedPermissions().add(message);
		navigationContext.setAddonMenu(getParent());
	}
}
