package be.nokorbis.spigot.commandsigns.controller;

import java.util.*;
import java.util.logging.Level;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.data.ICommandBlockSaver;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockSaver;
import be.nokorbis.spigot.commandsigns.menu.IEditionMenu;
import be.nokorbis.spigot.commandsigns.menu.MainMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsException;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

public class Container
{

	private static Container instance;

	public static Container getContainer()
	{
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
	private Map<UUID, Set<ExecuteTask>> executingTasks;
	private List<Player> infoPlayers;

	private ICommandBlockSaver commandBlockSaver;

	private IEditionMenu<CommandBlock> mainMenu;

	private Container()
	{
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		initializeDataStructures();
		this.mainMenu = new MainMenu();

		try
		{
			initializeSaver();
			plugin.getLogger().info("CommandSigns properly enabled !");
		}
		catch (Exception e)
		{
			plugin.getLogger().severe("Was not able to create the save file for command sign plugin");
			e.printStackTrace();
		}
	}

	private void initializeDataStructures()
	{
		this.playerPerms = new HashMap<>();
		this.commandBlocks = new HashMap<>();
		this.creatingConfigurations = new HashMap<>();
		this.editingConfigurations = new HashMap<>();
		this.copyingConfigurations = new HashMap<>();
		this.deletingBlocks = new HashMap<>();
		this.executingTasks = new HashMap<>();
		this.infoPlayers = new LinkedList<>();
	}

	private void initializeSaver()
	{
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

	public PermissionAttachment getPlayerPermissions(Player player)
	{
		if (this.playerPerms.containsKey(player))
		{
			return this.playerPerms.get(player);
		}

		PermissionAttachment perms = player.addAttachment(CommandSignsPlugin.getPlugin());
		this.playerPerms.put(player, perms);

		return perms;
	}

	public void handlePlayerDisconnection(Player player)
	{
		copyingConfigurations.remove(player);
		editingConfigurations.remove(player);
		creatingConfigurations.remove(player);
		infoPlayers.remove(player);
		if (executingTasks.containsKey(player.getUniqueId()))
		{
			for (ExecuteTask executeTask : executingTasks.get(player.getUniqueId()))
			{
				executeTask.cancel();
			}
			executingTasks.remove(player.getUniqueId());
		}
	}

	public Map<Location, CommandBlock> getCommandBlocks()
	{
		return this.commandBlocks;
	}

	public Map<Player, EditingConfiguration<CommandBlock>> getCreatingConfigurations()
	{
		return this.creatingConfigurations;
	}

	public Map<Player, EditingConfiguration<CommandBlock>> getEditingConfigurations()
	{
		return this.editingConfigurations;
	}

	public Map<Player, CommandBlock> getCopyingConfigurations()
	{
		return this.copyingConfigurations;
	}

	public Set<ExecuteTask> getExecutingTasks(Player player)
	{
		return this.executingTasks.computeIfAbsent(player.getUniqueId(), k -> new HashSet<>());
	}

	public boolean isPlayerExecutingCommandBlock(Player player, CommandBlock cmdblock)
	{
		Set<ExecuteTask> executingTasks = getExecutingTasks(player);
		for (ExecuteTask task : executingTasks)
		{
			if (task.getCommandBlock().getId() == cmdblock.getId())
			{
				return true;
			}
		}
		return false;
	}

	public void removeExecuteTask(ExecuteTask task)
	{
		Set<ExecuteTask> tasks = this.getExecutingTasks(task.getPlayer());
		synchronized (tasks)
		{
			tasks.remove(task);
		}
	}

	public Map<Player, Location> getDeletingBlocks()
	{
		return this.deletingBlocks;
	}

	public List<Player> getInfoPlayers()
	{
		return this.infoPlayers;
	}

	public IEditionMenu<CommandBlock> getMainMenu()
	{
		return this.mainMenu;
	}

	public CommandBlock getCommandBlockById(long id)
	{
		for (CommandBlock cmd : this.commandBlocks.values()) {
			if (cmd.getId() == id) {
				return cmd;
			}
		}
		return null;
	}

	public List<CommandBlock> getCommandBlocksByIdRange(long minId, long maxId)
	{
		List<CommandBlock> cmds = new LinkedList<>();
		for (CommandBlock cmd : this.commandBlocks.values())
		{
			if (cmd.getId() >= minId && cmd.getId() <= maxId)
			{
				cmds.add(cmd);
			}
		}
		return cmds;
	}

	public ICommandBlockSaver getSaver()
	{
		return commandBlockSaver;
	}
}
