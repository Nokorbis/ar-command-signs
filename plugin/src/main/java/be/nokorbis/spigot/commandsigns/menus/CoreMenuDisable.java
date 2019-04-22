package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;


public class CoreMenuDisable extends EditionLeaf<CommandBlock> {

	public CoreMenuDisable(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.disable.title"), parent);
	}

	@Override
	public String getDataValue(CommandBlock data) {
		if (data.isDisabled()) {
			return messages.get("menu.value.yes");
		}
		return messages.get("menu.value.no");
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationResult) {
		String msg = messages.get("menu.disable.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg, null);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(final Player player, final CommandBlock data, final String message, final MenuNavigationContext navigationResult) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				String[] args = message.split(" ");
				String val = args[0].toUpperCase();
				if ("Y".equals(val) || "YES".equals(val) || "TRUE".equals(val)) {
					data.setDisabled(true);
				}
				else {
					data.setDisabled(false);
				}
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationResult.setCoreMenu(getParent());
		}
	}
}
