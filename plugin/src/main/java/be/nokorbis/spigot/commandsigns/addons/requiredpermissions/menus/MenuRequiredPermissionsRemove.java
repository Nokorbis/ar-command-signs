package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuRequiredPermissionsRemove extends AddonEditionLeaf {

	public MenuRequiredPermissionsRemove(RequiredPermissionsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.required_permissions.remove.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return name;
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
		editor.sendMessage(messages.get("menu.required_permissions.display"));
		int cpt = 1;
		final String format = messages.get("menu.required_permissions.format");

		for (String perm : configurationData.getRequiredPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}
		String msg = messages.get("menu.required_permissions.remove.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;
				String[] args = message.split(" ", 2);
				int index = Integer.parseInt(args[0]);
				configurationData.getRequiredPermissions().remove(index-1);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
