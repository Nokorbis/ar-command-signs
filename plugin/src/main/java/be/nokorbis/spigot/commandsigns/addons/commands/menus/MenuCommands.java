package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import org.bukkit.entity.Player;


public class MenuCommands extends AddonEditionNode {

	public MenuCommands(CommandsAddon addon) {
		super(addon, messages.get("menu.commands.title"));
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new MenuCommandsAdd((CommandsAddon) addon, this));
		addMenu(new MenuCommandsEdit((CommandsAddon) addon, this));
		addMenu(new MenuCommandsRemove((CommandsAddon) addon, this));
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;

		displayBreadcrumb(editor);

		final String format = messages.get("menu.commands.format");
		int cpt = 1;
		for (String perm : configurationData.getCommands()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", perm);
			editor.sendMessage(msg);
		}

		displayMenus(editor, data, navigationContext);
	}
}
