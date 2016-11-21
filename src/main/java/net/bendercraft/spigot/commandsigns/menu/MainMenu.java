package net.bendercraft.spigot.commandsigns.menu;

import java.util.Map.Entry;

import net.bendercraft.spigot.commandsigns.model.CommandSignsException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.controller.Economy;
import net.bendercraft.spigot.commandsigns.controller.EditingConfiguration;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;

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
		editor.sendMessage(Messages.get("menu.refresh"));
		String msg = Messages.get("info.block_format");
		if (config.getEditingData().getLocation() == null) {
			if (config.isCreating()) {
				msg = msg.replace("{POSITION}", Messages.get("menu.set_on_click"));
			}
			else {
				msg = msg.replace("{POSITION}", Messages.get("menu.none"));
			}
		}
		else {
			// Block: BlockType#X:Z(Y)
			msg = msg.replace("{POSITION}", config.getEditingData().blockSummary());
		}
		editor.sendMessage(msg);
		for (Entry<Integer, EditionMenu> menu : this.subMenus.entrySet()) {
			String menuFormat = Messages.get("menu.format");
			menuFormat = menuFormat.replace("{NUMBER}", String.valueOf(menu.getKey())).replace("{MENU}", menu.getValue().formatName(config.getEditingData()));
			editor.sendMessage(menuFormat);
		}
		editor.sendMessage(Messages.get("menu.done"));
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
				try {
					config.getEditingData().validate();
					config.setCurrentMenu(null);
					if (config.isCreating()) {
						Container.getContainer().getCommandBlocks().put(config.getEditingData().getLocation(), config.getEditingData());
						Container.getContainer().getCreatingConfigurations().remove(config.getEditor());
					}
					else {
						Container.getContainer().getEditingConfigurations().remove(config.getEditor());
					}
					Container.getContainer().getSaver().save(config.getEditingData());
				}
				catch (CommandSignsException ex) {
					config.getEditor().sendMessage(Messages.get("menu.invalid_block"));

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
			config.getEditor().sendMessage(Messages.get("menu.number_needed"));
		}
	}
}
