package be.nokorbis.spigot.commandsigns.menus.news;

import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuTimerTime extends EditionLeaf<CommandBlock> {

	public CoreMenuTimerTime(EditionMenu<CommandBlock> parent) {
		super(Messages.get("info.time_before_execution"), parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		return name.replace("{TIME}", String.valueOf(data.getTimeBeforeExecution()));
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		editor.sendMessage(Messages.get("menu.time_before_edit"));
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {
		try {
			String[] args = message.split(" ", 2);
			int time = Integer.parseInt(args[0]);
			data.setTimeBeforeExecution(time);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setCoreMenu(getParent());
		}
	}
}
