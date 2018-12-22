package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsRemove extends EditionLeaf<AddonConfigurationData> {

	public MenuRequiredPermissionsRemove(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.remove"), parent);
	}

	@Override
	public String getDataString(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
		editor.sendMessage(Messages.get("info.needed_permissions"));
		int cpt = 1;
		String format = Messages.get("info.permission_format");

		for (String perm : configurationData.getRequiredPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}
		editor.sendMessage(Messages.get("menu.remove_permission"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			configurationData.getRequiredPermissions().remove(index-1);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
