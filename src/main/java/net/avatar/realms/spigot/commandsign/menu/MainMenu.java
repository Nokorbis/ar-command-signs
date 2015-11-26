package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.md_5.bungee.api.ChatColor;

public class MainMenu extends EditionMenu {

	public MainMenu() {
		super(null, "Main Menu");
		this.subMenus.put(2, new NameMenu(this));
		this.subMenus.put(3, new NeededPermissionsMenu(this));

		//If Vault is on the server, you can use the cost system
		if (Container.getContainer().getEconomy() != null) {
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
		editor.sendMessage(c + "1. Refresh");
		StringBuilder block = new StringBuilder();
		block.append(c);
		block.append("   Block: ");
		if (config.getEditingData().getLocation() == null) {
			block.append("None");
			if (config.isCreating()) {
				block.append(" [Set on click]");
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
		editor.sendMessage(ChatColor.GREEN + "9. Done");
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
					config.getEditor().sendMessage(ChatColor.RED + "The command block is not valid.");
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
			config.getEditor().sendMessage(ChatColor.DARK_RED + "You must enter a number to go through the menu.");
		}
	}
}
