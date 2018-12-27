package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus.MenuRequiredPermissionsAdd;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus.MenuRequiredPermissionsEdit;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus.MenuRequiredPermissionsRemove;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class MenuCommands extends EditionNode<AddonConfigurationData> {

	public MenuCommands() {
		super(Messages.get("menu.commands_title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuCommandsAdd(this));
		addMenu(new MenuCommandsEdit(this));
		addMenu(new MenuCommandsRemove(this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = Messages.get("info.command_format");
		int cpt = 1;
		for (String perm : configurationData.getCommands()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", perm);
			editor.sendMessage(msg);
		}

		displaySubMenus(editor, data, navigationContext);
	}
}
