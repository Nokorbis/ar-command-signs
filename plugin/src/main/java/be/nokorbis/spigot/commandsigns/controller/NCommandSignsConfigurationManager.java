package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.addons.commands.menus.MenuCommandsAdd;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class NCommandSignsConfigurationManager {
	private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/menu");
	private final NCommandSignsManager 	manager;
	private final Player                editor;
	private       CommandBlock          commandBlock;
	private       boolean               isCreating;
	private       MenuNavigationContext navigationContext;

	public NCommandSignsConfigurationManager(final Player player, final NCommandSignsManager manager) {
		this.editor = player;
		this.manager = manager;
		this.navigationContext = new MenuNavigationContext();
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

	public void setCurrentMenu(EditionMenu<CommandBlock> menu) {
		this.navigationContext.setCoreMenu(menu);
	}

	public void display() {
		EditionMenu<CommandBlock> coreMenu = this.navigationContext.getCoreMenu();
		if (coreMenu != null) {
			coreMenu.display(editor, commandBlock, this.navigationContext);
		}
		else {
			this.manager.saveCommandBlock(commandBlock);
			if (this.isCreating) {
				this.editor.sendMessage(messages.get("menu.creation_completed"));
			}
			else {
				this.editor.sendMessage(messages.get("menu.edition_completed"));
			}
			this.manager.removeConfigurationManager(this.editor);
		}
	}

	public boolean handleCommandInput(String command) {
		EditionMenu<CommandBlock> coreMenu = navigationContext.getCoreMenu();
		EditionMenu<AddonConfigurationData> addonMenu = navigationContext.getAddonMenu();
		if (coreMenu != null && addonMenu instanceof MenuCommandsAdd) {
			coreMenu.input(editor, commandBlock, command, navigationContext);
			return true;
		}
		return false;
	}

	public boolean handleChatInput(String message) {
		EditionMenu<CommandBlock> coreMenu = navigationContext.getCoreMenu();
		if (coreMenu != null) {
			coreMenu.input(editor, commandBlock, message, navigationContext);
			return true;
		}
		return false;
	}
}
