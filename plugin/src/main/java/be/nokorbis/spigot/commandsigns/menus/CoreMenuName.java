package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuName extends EditionLeaf<CommandBlock> {

	public CoreMenuName(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.name"), parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		String msg = Messages.get("info.name_format");
		if (data.getName() != null) {
			msg = msg.replace("{NAME}", data.getName());
		}
		else {
			msg = msg.replace("{NAME}", Messages.get("info.no_name"));
		}
		return msg;
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationResult) {
		if (editor != null) {
			String msg = Messages.get("menu.name_edit");
			msg = msg.replace("{NAME}", data.getName() == null ? Messages.get("info.no_name"):data.getName());
			editor.sendMessage(msg);
		}
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationResult) {
		if (data != null) {
			data.setName(message);
		}
		navigationResult.setCoreMenu(getParent());
	}
}
