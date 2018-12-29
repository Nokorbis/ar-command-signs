package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuTimerTime extends EditionLeaf<CommandBlock> {

	public CoreMenuTimerTime(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.timer.time_to_wait.title"), parent);
	}

	@Override
	public String getDataValue(CommandBlock data) {
		return String.valueOf(data.getTimeBeforeExecution());
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		String msg = messages.get("menu.timer.time_to_wait.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				String[] args = message.split(" ", 2);
				int time = Integer.parseInt(args[0]);
				data.setTimeBeforeExecution(time);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setCoreMenu(getParent());
		}
	}
}
