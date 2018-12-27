package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuTemporaryPermissions extends EditionNode<AddonConfigurationData> {

	public MenuTemporaryPermissions() {
		super(Messages.get("menu.temporary_permissions_title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuTemporaryPermissionsAdd(this));
		addMenu(new MenuTemporaryPermissionsEdit(this));
		addMenu(new MenuTemporaryPermissionsRemove(this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = Messages.get("info.permission_format");
		int cpt = 1;
		for (String perm : configurationData.getTemporarilyGrantedPermissions()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm);
			editor.sendMessage(msg);
		}

		displaySubMenus(editor, data, navigationContext);
	}
}
