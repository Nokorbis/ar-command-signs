package net.avatar.realms.spigot.commandsign;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.data.IBlockSaver;
import net.avatar.realms.spigot.commandsign.data.JsonBlockSaver;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.tasks.ExecuteTask;
import net.avatar.realms.spigot.commandsign.tasks.SaverTask;

public class CommandSign extends JavaPlugin{

	private static CommandSign plugin;

	private Map<Player, PermissionAttachment> 	playerPerms;
	private Map<Location, CommandBlock> 		commandBlocks;
	private Map<Player, EditingConfiguration> 	creatingConfigurations;
	private Map<Player, EditingConfiguration> 	editingConfigurations;
	private Map<Player, CommandBlock>			copyingConfigurations;
	private Map<Player, Location> 				deletingBlocks;
	private Map<UUID, ExecuteTask>				executingTasks;
	public List<Player> 						infoPlayers;

	private IBlockSaver							blockSaver;
	private SaverTask 							saver;

	@Override
	public void onEnable() {
		plugin = this;

		this.playerPerms = new HashMap<Player, PermissionAttachment>();
		this.commandBlocks = new HashMap<Location , CommandBlock>();
		this.creatingConfigurations = new HashMap<Player, EditingConfiguration>();
		this.editingConfigurations = new HashMap<Player, EditingConfiguration>();
		this.copyingConfigurations = new HashMap<Player, CommandBlock>();
		this.deletingBlocks = new HashMap<Player, Location>();
		this.executingTasks = new HashMap<UUID, ExecuteTask>();
		this.infoPlayers = new LinkedList<Player>();

		this.getCommand("commandsign").setExecutor(new CommandSignCommands(this));
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(this), this);

		try {
			this.blockSaver = new JsonBlockSaver(this.getDataFolder());
			loadData();
			this.saver = new SaverTask(this);
			long delay = 20 * 60 * 10; //Server ticks
			long period = 20 * 60 * 5; // Server ticks
			Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this.saver, delay, period);
		} catch (Exception e) {
			getLogger().severe("Was not able to create the save file for command sign plugin");
			e.printStackTrace();
		}
		getLogger().info("CommandSigns properly enabled !");
	}

	@Override
	public void onDisable() {
		plugin = null;
		if (this.blockSaver != null) {
			saveData();
		}
	}

	public static CommandSign getPlugin() {
		return plugin;
	}

	public PermissionAttachment getPlayerPermissions(Player player) {

		if (this.playerPerms.containsKey(player)) {
			return this.playerPerms.get(player);
		}

		PermissionAttachment perms = player.addAttachment(this);
		this.playerPerms.put(player, perms);

		return perms;
	}

	public Map<Location, CommandBlock> getCommandBlocks() {
		return this.commandBlocks;
	}

	public Map<Player, EditingConfiguration> getCreatingConfigurations() {
		return this.creatingConfigurations;
	}

	public Map<Player, EditingConfiguration> getEditingConfigurations() {
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

	private void loadData() {
		Collection<CommandBlock> data = this.blockSaver.load();
		if (data == null) {
			return;
		}
		for (CommandBlock block : data) {
			this.commandBlocks.put(block.getLocation(), block);
		}
	}

	public void saveData() {
		this.blockSaver.save(this.commandBlocks.values());
	}

}
