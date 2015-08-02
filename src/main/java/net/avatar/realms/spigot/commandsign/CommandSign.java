package net.avatar.realms.spigot.commandsign;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandSign extends JavaPlugin{
	
	private static CommandSign plugin;
	
	private Map<Player, PermissionAttachment> 	playerPerms;
	private Map<Block, CommandBlock> 			commandBlocks;
	private Map<Player, CommandBlock> 			creatingConfigurations;
	private Map<Player, CommandBlock> 			editingConfigurations;
	private Map<Player, CommandBlock>			copyingConfigurations;
	private Map<Player, Block> 					deletingBlocks;
	
	public static final List<Material> VALID_MATERIALS = new LinkedList<Material>() {

		private static final long serialVersionUID = 5828774578373884657L;

		{
			add(Material.WALL_SIGN);
			add(Material.SIGN_POST);
			add(Material.STONE_BUTTON);
			add(Material.WOOD_BUTTON);
			add(Material.STONE_PLATE);
			add(Material.WOOD_PLATE);
			add(Material.GOLD_PLATE);
			add(Material.IRON_PLATE);
		}
	};
	
	public static final List<Material> PLATES = new LinkedList<Material>() {

		private static final long serialVersionUID = -7382953117406089790L;

		{
			add(Material.STONE_PLATE);
			add(Material.WOOD_PLATE);
			add(Material.GOLD_PLATE);
			add(Material.IRON_PLATE);
		}
	};
	
	@Override
	public void onEnable() {
		plugin = this;
		
		playerPerms = new HashMap<Player, PermissionAttachment>();
		commandBlocks = new HashMap<Block , CommandBlock>();
		creatingConfigurations = new HashMap<Player, CommandBlock>();
		editingConfigurations = new HashMap<Player, CommandBlock>();
		copyingConfigurations = new HashMap<Player, CommandBlock>();
		deletingBlocks = new HashMap<Player, Block>();
		
		this.getCommand("commandsign").setExecutor(new CommandSignCommands(this));
		this.getServer().getPluginManager().registerEvents(new CommandSignListener(this), this);
	}
	
	@Override
	public void onDisable() {
		plugin = null;
	}
	
	public static CommandSign getPlugin() {
		return plugin;
	}
	
	public PermissionAttachment getPlayerPermissions(Player player) {
		
		if (playerPerms.containsKey(player)) {
			return playerPerms.get(player);
		}
		
		PermissionAttachment perms = player.addAttachment(this);
		playerPerms.put(player, perms);
		
		return perms;
	}
	
	public Map<Block, CommandBlock> getCommandBlocks() {
		return commandBlocks;
	}
	
	public Map<Player, CommandBlock> getCreatingConfigurations() {
		return creatingConfigurations;
	}
	
	public Map<Player, CommandBlock> getEditingConfigurations() {
		return editingConfigurations;
	}
	
	public Map<Player, CommandBlock> getCopyingConfigurations() {
		return copyingConfigurations;
	}
	
	public Map<Player, Block> getDeletingBlocks() {
		return deletingBlocks;
	}
}
