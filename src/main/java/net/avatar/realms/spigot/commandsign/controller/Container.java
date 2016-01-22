package net.avatar.realms.spigot.commandsign.controller;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.avatar.realms.spigot.commandsign.data.ICommandBlockSaver;
import net.avatar.realms.spigot.commandsign.data.json.JsonBlockSaver;
import net.avatar.realms.spigot.commandsign.data.json.JsonCommandBlockSaver;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.data.IBlockSaver;
import net.avatar.realms.spigot.commandsign.menu.IEditionMenu;
import net.avatar.realms.spigot.commandsign.menu.MainMenu;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.tasks.ExecuteTask;

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
	private ICommandBlockSaver commandBlockSaver;

	private IEditionMenu<CommandBlock> mainMenu;

	private Container() {
		CommandSign plugin = CommandSign.getPlugin();
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

	public void initializeDataStructures() {
		this.playerPerms = new HashMap<Player, PermissionAttachment>();
		this.commandBlocks = new HashMap<Location , CommandBlock>();
		this.creatingConfigurations = new HashMap<Player, EditingConfiguration<CommandBlock>>();
		this.editingConfigurations = new HashMap<Player, EditingConfiguration<CommandBlock>>();
		this.copyingConfigurations = new HashMap<Player, CommandBlock>();
		this.deletingBlocks = new HashMap<Player, Location>();
		this.executingTasks = new HashMap<UUID, ExecuteTask>();
		this.infoPlayers = new LinkedList<Player>();
	}

	private void initializeSaver() throws Exception {
		CommandSign plugin = CommandSign.getPlugin();
		commandBlockSaver = new JsonCommandBlockSaver(plugin.getDataFolder());
		for (CommandBlock commandBlock : commandBlockSaver.loadAll()) {
			this.commandBlocks.put(commandBlock.getLocation(), commandBlock);
			if (!commandBlock.validate()) {
				plugin.getLogger().warning("A Command Block is invalid. You may think about deleting it. ID : " + commandBlock.getId());
			}
		}

		File old = new File(plugin.getDataFolder(), JsonBlockSaver.FILENAME);
		if (old.exists() && old.isFile()) {
			plugin.getLogger().info("Detected old version of data... starting conversion...");
			this.blockSaver = new JsonBlockSaver(plugin.getDataFolder());
			for (CommandBlock commandBlock : blockSaver.load()) {
				this.commandBlocks.put(commandBlock.getLocation(), commandBlock);
				if (!commandBlock.validate()) {
					plugin.getLogger().warning("A Command Block is invalid. You may think about deleting it. ID : " + commandBlock.getId());
				}
			}
			commandBlockSaver.saveAll(commandBlocks.values());
			old.renameTo(new File(plugin.getDataFolder(), "old_data-to_delete.json"));
			plugin.getLogger().info("Conversion of old data done !");
		}

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
