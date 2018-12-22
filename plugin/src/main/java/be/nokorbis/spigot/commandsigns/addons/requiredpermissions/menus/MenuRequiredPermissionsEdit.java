package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsEdit extends EditionLeaf<AddonConfigurationData> {

	public MenuRequiredPermissionsEdit(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.edit"), parent);
	}

	@Override
	public String getDataString(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(final Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
		editor.sendMessage(Messages.get("info.needed_permissions"));
		int cpt = 1;
		String format = Messages.get("info.permission_format");
		String msg;
		for (String perm : configurationData.getRequiredPermissions()) {
			msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}
		editor.sendMessage(Messages.get("menu.edit_permission"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			configurationData.getRequiredPermissions().set(index - 1, args[1]);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
