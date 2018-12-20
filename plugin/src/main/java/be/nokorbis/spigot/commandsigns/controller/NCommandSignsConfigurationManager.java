package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;


public class NCommandSignsConfigurationManager {
	private final Player editor;
	private CommandBlock commandBlock;
	private boolean isCreating;
	private EditionMenu<? extends MenuEditable> currentMenu= null;

	public NCommandSignsConfigurationManager(final Player player) {
		this.editor = player;
	}

	public Player getEditor() {
		return this.editor;
	}

	public boolean isCreating() {
		return this.isCreating;
	}

	public boolean isEditing() {
		return !this.isCreating();
	}

	public void setCreating(boolean creating) {
		this.isCreating = creating;
	}

	public void setEditing(boolean editing) {
		this.setCreating(!editing);
	}

	public CommandBlock getCommandBlock() {
		return commandBlock;
	}

	public void setCommandBlock(CommandBlock commandBlock) {
		this.commandBlock = commandBlock;
	}

	public void setCurrentMenu(EditionMenu<? extends MenuEditable> menu) {
		this.currentMenu = menu;
	}

	public void display() {

	}

	public boolean handleCommandInput(String command) {
		return false;
	}

	public boolean handleChatInput(String message) {
		return false;
	}
}
