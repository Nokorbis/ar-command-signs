package net.avatar.realms.spigot.commandsign.model;

import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.menu.IEditionMenu;

public class EditingConf<T> {

	private Player editor;
	private T editingData;
	private IEditionMenu<T> currentMenu;
	private boolean creating;

	/**
	 * @param player
	 *        The player that is editing the configuration
	 * @param editingData
	 *        The data whose configuration is being edited
	 * @param creating
	 *        Is the player creating a new command block or editing an existing one ?
	 */
	public EditingConf(Player player, T editingData, boolean creating) {
		this.editor = player;
		this.editingData = editingData;
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
	
	public T getEditingData() {
		return this.editingData;
	}

	public void setCurrentMenu(IEditionMenu<T> newMenu) {
		this.currentMenu = newMenu;
	}
	
	public boolean isCreating() {
		return this.creating;
	}
}
