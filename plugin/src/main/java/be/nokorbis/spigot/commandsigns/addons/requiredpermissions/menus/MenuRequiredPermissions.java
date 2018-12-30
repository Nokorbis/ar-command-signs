package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.RequiredPermissionsAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import org.bukkit.entity.Player;


public class MenuRequiredPermissions extends AddonEditionNode {

	public MenuRequiredPermissions(RequiredPermissionsAddon addon) {
		super(addon, messages.get("menu.required_permissions.title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuRequiredPermissionsAdd((RequiredPermissionsAddon) addon, this));
		addMenu(new MenuRequiredPermissionsEdit((RequiredPermissionsAddon) addon, this));
		addMenu(new MenuRequiredPermissionsRemove((RequiredPermissionsAddon) addon, this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = messages.get("menu.required_permissions.format");
		int cpt = 1;
		for (String perm : configurationData.getRequiredPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}

		displayMenus(editor, data, navigationContext);
	}
}
