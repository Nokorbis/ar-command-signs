package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.Economy;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class MainMenu extends EditionMenu {

	public MainMenu() {
		super(null, Messages.get("menu.main_title"));
		this.subMenus.put(2, new NameMenu(this));
		this.subMenus.put(3, new NeededPermissionsMenu(this));

		//If Vault is on the server, you can use the cost system
		if (Economy.getEconomy() != null) {
			this.subMenus.put(4, new CostsMenu(this));
			this.subMenus.put(5, new TimerMenu(this));
			this.subMenus.put(6, new TemporaryPermissionsMenu(this));
			this.subMenus.put(7, new CommandsMenu(this));
		}
		else {
			this.subMenus.put(4, new TimerMenu(this));
			this.subMenus.put(5, new TemporaryPermissionsMenu(this));
			this.subMenus.put(6, new CommandsMenu(this));
		}
	}

	@Override
	public void display(EditingConfiguration<CommandBlock> config) {
		Player editor = config.getEditor();
		editor.sendMessage(c + "1. " + Messages.get("menu.refresh"));
		StringBuilder block = new StringBuilder();
		block.append(c);
		block.append("   " + Messages.get("info.block") + ": ");
		if (config.getEditingData().getLocation() == null) {
			block.append(Messages.get("menu.none"));
			if (config.isCreating()) {
				block.append(" " + Messages.get("menu.set_on_click"));
			}
		}
		else {
			// Block: BlockType#X:Z(Y)
			Location loc = config.getEditingData().getLocation();
			block.append(loc.getBlock().getType());
			block.append("#");
			block.append(loc.getX());
			block.append(":");
			block.append(loc.getZ());
			block.append("(");
			block.append(loc.getY());
			block.append(")");
		}
		editor.sendMessage(block.toString());
		for (Entry<Integer, EditionMenu> menu : this.subMenus.entrySet()) {
			editor.sendMessage(c + "" + menu.getKey() + ". " + menu.getValue().formatName(config.getEditingData()));
		}
		editor.sendMessage(ChatColor.GREEN + "9. " + Messages.get("menu.done"));
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
				if (config.getEditingData().validate()) {
					config.setCurrentMenu(null);
					if (config.isCreating()) {
						Container.getContainer().getCommandBlocks().put(config.getEditingData().getLocation(),
								config.getEditingData());
						Container.getContainer().getCreatingConfigurations().remove(config.getEditor());
					}
					else {
						Container.getContainer().getEditingConfigurations().remove(config.getEditor());
					}
				}
				else {
					config.getEditor().sendMessage(ChatColor.RED + Messages.get("menu.invalid_block"));
				}
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
