package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public class CoreMenuTimerCancel extends EditionLeaf<CommandBlock> {

	public CoreMenuTimerCancel(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.timer.cancel.title"), parent);
	}

	@Override
	public String getDataValue(CommandBlock data) {
		return data.isCancelledOnMove() ? messages.get("menu.value.yes") : messages.get("menu.value.no");
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		String msg = messages.get("menu.timer.cancel.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {
		if (!CANCEL_STRING.equals(message)) {
			String[] args = message.split(" ");
			String arg = args[0].toUpperCase();
			if ("YES".equals(arg) || "Y".equals(arg) || "TRUE".equals(arg)) {
				data.setCancelledOnMove(true);
			}
			else {
				data.setCancelledOnMove(false);
			}
		}

		navigationContext.setCoreMenu(getParent());
	}
}
