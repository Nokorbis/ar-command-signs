package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuTimerReset extends EditionLeaf<CommandBlock> {

	public CoreMenuTimerReset(EditionMenu<CommandBlock> parent) {
		super(Messages.get("menu.reset_title"), parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		return name.replace("{BOOLEAN}", data.isResetOnMove() ? Messages.get("menu.yes") : Messages.get("menu.no"));
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		editor.sendMessage(Messages.get("menu.reset_edit"));
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {
		String[] args = message.split(" ");
		String arg = args[0].toUpperCase();
		if (arg.equals("YES") || arg.equals("Y") || arg.equals("TRUE")) {
			data.setResetOnMove(true);
		}
		else {
			if (!args[0].equals("CANCEL")) {
				data.setResetOnMove(false);
			}
		}
		navigationContext.setCoreMenu(getParent());
	}
}
