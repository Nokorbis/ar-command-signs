package net.avatar.realms.spigot.commandsign;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public class CommandSign extends JavaPlugin{
	
	private static CommandSign plugin;
	
	private Map<Player, PermissionAttachment> playerPerms;
	private Map<Block, CommandBlock> commandBlocks;
	private Map<Player, CommandBlock> waitingConfigurations;
	
	@Override
	public void onEnable() {
		plugin = this;
		
		playerPerms = new HashMap<Player, PermissionAttachment>();
		commandBlocks = new HashMap<Block , CommandBlock>();
		waitingConfigurations = new HashMap<Player, CommandBlock>();
		
		this.getCommand("commandsign").setExecutor(new CommandSignCommands(this));
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
	
	public Map<Player, CommandBlock> getWaitingConfigurations() {
		return waitingConfigurations;
	}
	
	
}
