package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissionsRemove extends EditionLeaf<AddonConfigurationData> {

	public MenuTemporaryPermissionsRemove(EditionMenu<AddonConfigurationData> parent) {
		super(Messages.get("menu.remove"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
		editor.sendMessage(Messages.get("info.permissions"));
		int cpt = 1;
		String format = Messages.get("info.permission_format");

		for (String perm : configurationData.getTemporarilyGrantedPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}
		editor.sendMessage(Messages.get("menu.remove_permission"));
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
			String[] args = message.split(" ", 2);
			int index = Integer.parseInt(args[0]);
			configurationData.getTemporarilyGrantedPermissions().remove(index-1);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
