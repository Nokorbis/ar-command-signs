package net.bendercraft.spigot.commandsigns.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import net.bendercraft.spigot.commandsigns.data.ICommandBlockSaver;
import net.bendercraft.spigot.commandsigns.data.json.JsonCommandBlockSaver;
import net.bendercraft.spigot.commandsigns.model.CommandSignsException;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.bendercraft.spigot.commandsigns.CommandSignsPlugin;
import net.bendercraft.spigot.commandsigns.menu.IEditionMenu;
import net.bendercraft.spigot.commandsigns.menu.MainMenu;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.tasks.ExecuteTask;

public class Container {

	private static Container instance;

	public static Container getContainer() {
		if (instance == null) {
			instance = new Container();
		}
		return instance;
	}

	private Map<Player, PermissionAttachment> playerPerms;
	private Map<Location, CommandBlock> commandBlocks;
	private Map<Player, EditingConfiguration<CommandBlock>> creatingConfigurations;
	private Map<Player, EditingConfiguration<CommandBlock>> editingConfigurations;
	private Map<Player, CommandBlock> copyingConfigurations;
	private Map<Player, Location> deletingBlocks;
	private Map<UUID, ExecuteTask> executingTasks;
	private List<Player> infoPlayers;

	private ICommandBlockSaver commandBlockSaver;

	private IEditionMenu<CommandBlock> mainMenu;

	private Container() {
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		initializeDataStructures();
		this.mainMenu = new MainMenu();

		try {
			initializeSaver();
			plugin.getLogger().info("CommandSigns properly enabled !");
		}
		catch (Exception e) {
			plugin.getLogger().severe("Was not able to create the save file for command sign plugin");
			e.printStackTrace();
		}
	}

	private void initializeDataStructures() {
		this.playerPerms = new HashMap<Player, PermissionAttachment>();
		this.commandBlocks = new HashMap<Location , CommandBlock>();
		this.creatingConfigurations = new HashMap<Player, EditingConfiguration<CommandBlock>>();
		this.editingConfigurations = new HashMap<Player, EditingConfiguration<CommandBlock>>();
		this.copyingConfigurations = new HashMap<Player, CommandBlock>();
		this.deletingBlocks = new HashMap<Player, Location>();
		this.executingTasks = new HashMap<UUID, ExecuteTask>();
		this.infoPlayers = new LinkedList<Player>();
	}

	private void initializeSaver() {
		commandBlockSaver = new JsonCommandBlockSaver(CommandSignsPlugin.getPlugin().getDataFolder());
		reload();
	}

	/**
	 * Reload all the config files
	 * @return The number of errors that occured
	 */
	public int reload() {
		int errorCount = 0;
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		for (CommandBlock commandBlock : commandBlockSaver.loadAll()) {
			try {
				this.commandBlocks.put(commandBlock.getLocation(), commandBlock);
				commandBlock.validate();
			}
			catch (CommandSignsException ex) {
				plugin.getLogger().warning(ex.getMessage());
				errorCount++;
			}
			catch (Exception ex) {
				plugin.getLogger().log(Level.WARNING,
						"An exception occured while validating a command block. The plugin should be able to work but here is the exception : ",
						ex);
				errorCount++;
			}
		}
		return errorCount;
	}

	public PermissionAttachment getPlayerPermissions(Player player) {
		if (this.playerPerms.containsKey(player)) {
			return this.playerPerms.get(player);
		}

		PermissionAttachment perms = player.addAttachment(CommandSignsPlugin.getPlugin());
		this.playerPerms.put(player, perms);

		return perms;
	}

	public Map<Location, CommandBlock> getCommandBlocks() {
		return this.commandBlocks;
	}

	public Map<Player, EditingConfiguration<CommandBlock>> getCreatingConfigurations() {
		return this.creatingConfigurations;
	}

	public Map<Player, EditingConfiguration<CommandBlock>> getEditingConfigurations() {
		return this.editingConfigurations;
	}

	public Map<Player, CommandBlock> getCopyingConfigurations() {
		return this.copyingConfigurations;
	}

	public Map<UUID, ExecuteTask> getExecutingTasks() {
		return this.executingTasks;
	}

	public Map<Player, Location> getDeletingBlocks() {
		return this.deletingBlocks;
	}

	public List<Player> getInfoPlayers() {
		return this.infoPlayers;
	}

	public IEditionMenu<CommandBlock> getMainMenu() {
		return this.mainMenu;
	}

	public CommandBlock getCommandBlockById(long id) {
		for (CommandBlock cmd : this.commandBlocks.values()) {
			if (cmd.getId() == id) {
				return cmd;
			}
		}
		return null;
	}

	public List<CommandBlock> getCommandBlocksByIdRange(long minId, long maxId) {
		List<CommandBlock> cmds = new LinkedList<CommandBlock>();
		for (CommandBlock cmd : this.commandBlocks.values()){
			if (cmd.getId() >= minId && cmd.getId() <= maxId) {
				cmds.add(cmd);
			}
		}
		return cmds;
	}

	public ICommandBlockSaver getSaver() {
		return commandBlockSaver;
	}
}
