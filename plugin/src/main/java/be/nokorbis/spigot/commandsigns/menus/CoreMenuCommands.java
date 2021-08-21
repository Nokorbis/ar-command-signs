package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;


public class CoreMenuCommands extends EditionNodeCore {

	public CoreMenuCommands(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.commands.title"), parent);
	}

	@Override
	protected void initializeSubMenus() {
		addMenu(new CoreMenuCommandsAdd(this));
		addMenu(new CoreMenuCommandsEdit(this));
		addMenu(new CoreMenuCommandsRemove(this));
	}

	@Override
	public void display(final Player editor, final CommandBlock data, final MenuNavigationContext navigationContext) {

		displayBreadcrumb(editor);

		final String format = messages.get("menu.commands.format");
		int cpt = 1;
		for (String command : data.getCommands()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", command);
			editor.sendMessage(msg);
		}

		displayMenus(editor, data, navigationContext);
	}
}
