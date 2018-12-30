package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsEdit extends AddonEditionLeaf {

	public MenuRequiredPermissionsEdit(RequiredPermissionsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.required_permissions.edit.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return "";
	}

	@Override
	public void display(final Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
		editor.sendMessage(messages.get("menu.required_permissions.display"));
		int cpt = 1;
		String format = messages.get("menu.required_permissions.format");
		for (String perm : configurationData.getRequiredPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}
		String msg = messages.get("menu.required_permissions.edit.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
				String[] args = message.split(" ", 2);
				int index = Integer.parseInt(args[0]);
				configurationData.getRequiredPermissions().set(index - 1, args[1]);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
