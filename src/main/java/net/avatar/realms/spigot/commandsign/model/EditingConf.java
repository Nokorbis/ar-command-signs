package net.avatar.realms.spigot.commandsign.model;

import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.menu.IEditionMenu;
import net.avatar.realms.spigot.commandsign.menu.MainMenu;

public class EditingConf {
	
	private Player editor;
	private CommandBlock commandBlock;
	private IEditionMenu currentMenu;
	private boolean creating;
	
	/**
	 * @param player
	 *        The player that is editing the configuration
	 * @param commandBlock
	 *        The command block whose configuration is being edited
	 * @param creating
	 *        Is the player creating a new command block or editing an existing one ?
	 */
	public EditingConf(Player player, CommandBlock commandBlock, boolean creating) {
		this.editor = player;
		this.commandBlock = commandBlock;
		this.currentMenu = new MainMenu();
	}
	
	public void display() {
		if (this.currentMenu != null) {
			this.currentMenu.display(this);
		}
	}

	public void input(String message) {
		if ((message != null) && (this.currentMenu != null)) {
			message = message.trim();
			this.currentMenu.input(this, message);
		}
	}
	
	public Player getEditor() {
		return this.editor;
	}

	public CommandBlock getCommandBlock() {
		return this.commandBlock;
	}
	
	public void setCurrentMenu(IEditionMenu newMenu) {
		this.currentMenu = newMenu;
	}

	public boolean isCreating() {
		return this.creating;
	}
}
