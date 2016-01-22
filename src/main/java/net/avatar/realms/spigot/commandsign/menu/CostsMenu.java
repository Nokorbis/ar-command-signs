package net.avatar.realms.spigot.commandsign.menu;

import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.Economy;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CostsMenu extends EditionMenu {

	public CostsMenu(EditionMenu parent) {
		super(parent, Messages.get("info.costs"));
		this.subMenus.put(2, new EconomyMenu(this));
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		if (editor != null) {
			CommandBlock cmd = config.getEditingData();
			if (cmd != null) {
				editor.sendMessage(c + "1. " + Messages.get("menu.refresh"));
				String msg = Messages.get("menu.economy");
				msg = msg.replaceAll("\\{PRICE\\}", Economy.getEconomy().format(config.getEditingData().getEconomyPrice()));
				editor.sendMessage(c + "2. " + msg);
				editor.sendMessage(ChatColor.GREEN + "9. " + Messages.get("menu.done"));
			}
		}
	}

	@Override
	public void input(EditingConfiguration<CommandBlock> config, String message) {
		try {
			String[] args = message.split(" ");
			if (args.length == 0) {
				//No parameters, let's do nothing so that he receives the display message again
				return;
			}
			int index = Integer.parseInt(args[0]);
			if (index == 9) {
				config.setCurrentMenu(getParent());
			}
			else if (this.subMenus.containsKey(index)) {
				IEditionMenu<CommandBlock> newMenu = this.subMenus.get(index);
				config.setCurrentMenu(newMenu);
			}
			else {
				// let's do nothing so that he receives the display message again
			}
		}
		catch (NumberFormatException ex) {
			config.getEditor().sendMessage(ChatColor.DARK_RED + Messages.get("menu.number_needed"));
		}
	}
}
