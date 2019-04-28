package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.BlockActivationMode;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class CoreMenuActivationMode extends EditionLeaf<CommandBlock> {

	public CoreMenuActivationMode(EditionMenu<CommandBlock> parent) {
		super(messages.get("menu.activation_mode.title"), parent);
	}

	@Override
	public boolean shouldBeDisplayed(CommandBlock data) {
		Location location = data.getLocation();
		if (location != null) {
			if (CommandBlockValidator.isLever(location.getBlock())){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDataValue(CommandBlock data) {
		return data.getActivationMode().name();
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationResult) {
		String msg = messages.get("menu.activation_mode.edit");
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
				if ("ACTIVATED".equals(val)) {
					data.setActivationMode(BlockActivationMode.ACTIVATED);
				}
				else if ("DEACTIVATED".equals(val)) {
					data.setActivationMode(BlockActivationMode.DEACTIVATED);
				}
				else {
					data.setActivationMode(BlockActivationMode.BOTH);
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
