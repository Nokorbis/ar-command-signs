package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissionsEdit extends AddonEditionLeaf {

	public MenuTemporaryPermissionsEdit(CommandsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.temporary_permissions.edit.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return "";
	}

	@Override
	public void display(final Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
		editor.sendMessage(messages.get("menu.temporary_permissions.display"));
		int cpt = 1;
		final String format = messages.get("menu.temporary_permissions.format");
		for (String perm : configurationData.getTemporarilyGrantedPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}

		String msg = messages.get("menu.temporary_permissions.edit.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
				String[] args = message.split(" ", 2);
				int index = Integer.parseInt(args[0]);
				configurationData.getTemporarilyGrantedPermissions().set(index - 1, args[1]);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
