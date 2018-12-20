package be.nokorbis.spigot.commandsigns.menus.news;

import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationResult;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuName extends EditionLeaf<CommandBlock> {

	public CoreMenuName(EditionMenu<? extends MenuEditable> parent) {
		super(Messages.get("menu.name"), parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		return null;
	}

	@Override
	public void display(Player editor, CommandBlock data, int page) {
		if (editor != null) {
			String msg = Messages.get("menu.name_edit");
			msg = msg.replace("{NAME}", data.getName() == null ? Messages.get("info.no_name"):data.getName());
			editor.sendMessage(msg);
		}
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationResult navigationResult) {
		if (data != null) {
			data.setName(message);
		}
		navigationResult.setMenu(getParent());
	}
}
