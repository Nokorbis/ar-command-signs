package be.nokorbis.spigot.commandsigns.addons.commands.menus;

import be.nokorbis.spigot.commandsigns.addons.commands.CommandsAddon;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import org.bukkit.entity.Player;


public class MenuCommandsRemove extends AddonEditionLeaf {

	public MenuCommandsRemove(CommandsAddon addon, AddonEditionMenu parent) {
		super(addon, messages.get("menu.commands.remove.title"), parent);
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		return "";
	}

	@Override
	public String getDisplayString(AddonConfigurationData data) {
		return messages.get("menu.entry.display_name_only").replace("{NAME}", name);
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
		editor.sendMessage(messages.get("menu.commands.display"));
		int cpt = 1;
		final String format = messages.get("menu.commands.format");
		for (String perm : configurationData.getCommands()) {
			String msg = format.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", perm);
			editor.sendMessage(msg);
		}

		String msg = messages.get("menu.commands.remove.edit");
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				final CommandsConfigurationData configurationData = (CommandsConfigurationData) data;
				String[] args = message.split(" ", 2);
				int index = Integer.parseInt(args[0]);
				configurationData.getCommands().remove(index-1);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
