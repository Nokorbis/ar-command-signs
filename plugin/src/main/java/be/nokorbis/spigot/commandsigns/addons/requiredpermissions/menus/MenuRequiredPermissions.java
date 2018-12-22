package be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus;

import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuRequiredPermissions extends EditionNode<AddonConfigurationData> {

	public MenuRequiredPermissions() {
		super(Messages.get("menu.required_permissions_title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuRequiredPermissionsAdd(this));
		addMenu(new MenuRequiredPermissionsEdit(this));
		addMenu(new MenuRequiredPermissionsRemove(this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final RequiredPermissionsConfigurationData configurationData = (RequiredPermissionsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = Messages.get("info.permission_format");
		int cpt = 1;
		for (String perm : configurationData.getRequiredPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}

		displaySubMenus(editor, data, navigationContext);
	}
}
