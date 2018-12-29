package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;


public class CoreMenuName extends EditionLeaf<CommandBlock> {

	public CoreMenuName(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.name.title"), parent);
	}

	@Override
	public String getDataValue(CommandBlock data) {
		if (data.getName() == null) {
			return messages.get("menu.name.no_name");
		}

		return data.getName();
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationResult) {
		if (editor != null) {
			String msg = messages.get("menu.name.edit").replace("{NAME}", getDataValue(data));
			ClickableMessage clickableMessage = new ClickableMessage(msg);
			clickableMessage.add(CLICKABLE_CANCEL);
			clickableMessage.sendToPlayer(editor);
		}
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationResult) {
		if (!CANCEL_STRING.equals(message)) {
			if (data != null) {
				data.setName(message);
			}
		}
		navigationResult.setCoreMenu(getParent());
	}
}
