package be.nokorbis.spigot.commandsigns.controller;

import org.bukkit.entity.Player;


public class NCommandSignsConfigurationManager {
	private Player editor;
	private boolean isCreating;

	public NCommandSignsConfigurationManager(Player player) {
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

	public boolean handleCommandInput(String command) {
		return false;
	}

	public boolean handleChatInput(String message) {
		return false;
	}
}
