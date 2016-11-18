package net.bendercraft.spigot.commandsigns.controller;

import net.bendercraft.spigot.commandsigns.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.bendercraft.spigot.commandsigns.menu.IEditionMenu;

public class EditingConfiguration<T> {

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
	public EditingConfiguration(Player player, T editingData, boolean creating) {
		this.editor = player;
		this.editingData = editingData;
		this.creating = creating;
	}

	public EditingConfiguration(Player player, boolean creating) {
		this.editor = player;
		this.creating = creating;
		this.editingData = null;
	}

	public void display() {
		if (this.editingData == null) {
			this.editor.sendMessage(ChatColor.DARK_RED + Messages.get("error.no_block_selected"));
			return;
		}
		if (this.currentMenu != null) {
			this.currentMenu.display(this);
		}
		else {
			if (this.creating) {
				this.editor.sendMessage(Messages.get("info.success_creation"));
			}
			else {
				this.editor.sendMessage(Messages.get("info.success_edition"));
			}
		}
	}

	public void input(String message) {
		if ((message != null) && (this.currentMenu != null)) {
			message = message.trim();
			if (!message.equals("")) {
				this.currentMenu.input(this, message);
			}
		}
	}

	public void setEditingData(T data) {
		this.editingData = data;
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

	public IEditionMenu<T> getCurrentMenu() {
		return this.currentMenu;
	}
}
