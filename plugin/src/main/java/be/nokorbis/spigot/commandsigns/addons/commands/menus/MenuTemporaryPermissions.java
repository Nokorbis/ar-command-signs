package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissions extends AddonEditionNode {

	public MenuTemporaryPermissions(CommandsAddon addon) {
		super(addon, messages.get("menu.temporary_permissions.title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuTemporaryPermissionsAdd((CommandsAddon) addon, this));
		addMenu(new MenuTemporaryPermissionsEdit((CommandsAddon) addon, this));
		addMenu(new MenuTemporaryPermissionsRemove((CommandsAddon) addon, this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = messages.get("menu.temporary_permissions.format");
		int cpt = 1;
		for (String perm : configurationData.getTemporarilyGrantedPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}

		displayMenus(editor, data, navigationContext);
	}
}
