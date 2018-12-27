package be.nokorbis.spigot.commandsigns.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.data.CommandBlockSaver;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockSaver;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

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
	private Map<Player, CommandBlock> copyingConfigurations;
	private Map<Player, Location> deletingBlocks;
	private final Map<UUID, ExecuteTask> executingTasks = new HashMap<>();
	private List<Player> infoPlayers;

	private CommandBlockSaver commandBlockSaver;

	private Container() {
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		initializeDataStructures();

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
		this.playerPerms = new HashMap<>();
		this.commandBlocks = new HashMap<>();
		this.copyingConfigurations = new HashMap<>();
		this.deletingBlocks = new HashMap<>();
		this.infoPlayers = new LinkedList<>();
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
				commandBlock.validate();
				this.commandBlocks.put(commandBlock.getLocation(), commandBlock);
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

	public CommandBlock getCommandBlockById(long id) {
		for (CommandBlock cmd : this.commandBlocks.values()) {
			if (cmd.getId() == id) {
				return cmd;
			}
		}
		return null;
	}


	public CommandBlockSaver getSaver() {
		return commandBlockSaver;
	}
}
