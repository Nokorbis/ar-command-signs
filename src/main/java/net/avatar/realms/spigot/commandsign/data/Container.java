package net.avatar.realms.spigot.commandsign.data;

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
import org.bukkit.plugin.RegisteredServiceProvider;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.menu.IEditionMenu;
import net.avatar.realms.spigot.commandsign.menu.MainMenu;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.tasks.ExecuteTask;
import net.avatar.realms.spigot.commandsign.tasks.SaverTask;
import net.milkbowl.vault.economy.Economy;

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
	public List<Player> infoPlayers;

	private IBlockSaver	blockSaver;
	private SaverTask saver;

	private Economy	economy;

	private IEditionMenu<CommandBlock> mainMenu;

	private Container() {
		CommandSign plugin = CommandSign.getPlugin();
		initializeDataStructures();
		initializeEconomy();
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

	private void initializeEconomy() {
		CommandSign plugin = CommandSign.getPlugin();
		if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
			if (rsp != null) {
				this.economy = rsp.getProvider();
				if (this.economy != null) {
					plugin.getLogger().info("Vault economy detected for command signs ! ");
				}
			}
		}
	}

	private void initializeSaver() throws Exception {
		CommandSign plugin = CommandSign.getPlugin();
		this.blockSaver = new JsonBlockSaver(plugin.getDataFolder());
		loadData();
		this.saver = new SaverTask(this);
		long delay = 20 * 60 * 10; //Server ticks
		long period = 20 * 60 * 5; // Server ticks
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this.saver, delay, period);
	}

	public PermissionAttachment getPlayerPermissions(Player player) {
		if (this.playerPerms.containsKey(player)) {
			return this.playerPerms.get(player);
		}

		PermissionAttachment perms = player.addAttachment(CommandSign.getPlugin());
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

	public Economy getEconomy() {
		return this.economy;
	}

	private void loadData() {
		Collection<CommandBlock> data = this.blockSaver.load();
		if (data == null) {
			return;
		}
		for (CommandBlock block : data) {
			this.commandBlocks.put(block.getLocation(), block);
			if (!block.validate()) {
				CommandSign.getPlugin().getLogger().warning("Invalid command block (" + block.getId() + ") detected at " + block.blockSummary()+ ". You should delete it.");
			}
		}
	}

	public void saveData() {
		if (this.blockSaver != null) {
			this.blockSaver.save(this.commandBlocks.values());
		}
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
}
