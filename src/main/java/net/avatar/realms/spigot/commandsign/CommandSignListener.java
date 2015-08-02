package net.avatar.realms.spigot.commandsign;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;


public class CommandSignListener implements Listener{

	private CommandSign plugin;
	
	public CommandSignListener (CommandSign plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChatEvent (AsyncPlayerChatEvent event) {
		
	}
	
	@EventHandler
	public void onInteractEvent (PlayerInteractEvent event) {
		
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		Block block = event.getClickedBlock();
		Player player = event.getPlayer();
		
		/* Do we have to delete this command block ? */
		if (plugin.getDeletingBlocks().containsKey(player)) {
			deleteCommandBlock(player, block);
		}
		
		/* Do we have to edit the command block configuration ? */
		else if (plugin.getEditingConfigurations().containsKey(player)) {
			
		}
		
		/* Do we have to create the command block configuration ? */
		else if (plugin.getCreatingConfigurations().containsKey(player)) {
			
		}
		
		/* Do we have to copy the command block configuration ? */
		else if (plugin.getCopyingConfigurations().containsKey(player)) {
			copyCommandBlock(player, block);
		}
		
		/* Is that a block that we can execute ? */
		else if (plugin.getCommandBlocks().containsKey(block)) {
			plugin.getCommandBlocks().get(block).execute(player);
		}
	}

	private void copyCommandBlock(Player player, Block block) {
		if (!CommandSign.VALID_MATERIALS.contains(block.getType())) {
			player.sendMessage(ChatColor.RED + "Not a valid block. Aborting copying.");
			plugin.getCopyingConfigurations().remove(player);
			return;
		}
		
		CommandBlock copyingBlock = plugin.getCopyingConfigurations().get(player);
		if (copyingBlock == null) {
			if (plugin.getCommandBlocks().containsKey(block)) {
				copyingBlock = plugin.getCommandBlocks().get(block);
				plugin.getCopyingConfigurations().put(player, copyingBlock.copy());
				player.sendMessage(ChatColor.GOLD + "Block copied. Click on another block to paste the configuration.");
			}
			else {
				player.sendMessage(ChatColor.RED + "This is not a command block. Aborting copying.");
				plugin.getCopyingConfigurations().remove(player);
			}
		}
		else if (plugin.getCommandBlocks().containsKey(block)) {
			player.sendMessage(ChatColor.RED + "This block is already a command block. Aborting copying.");
			plugin.getCopyingConfigurations().remove(player);
		}
		else {
			copyingBlock.setBlock(block);
			plugin.getCommandBlocks().put(block, copyingBlock);
			plugin.getCopyingConfigurations().remove(player);
			player.sendMessage(ChatColor.GREEN + "Block properly copied.");
		}
	}

	private void deleteCommandBlock(Player player, Block block) {
		if (!CommandSign.VALID_MATERIALS.contains(block.getType())) {
			player.sendMessage(ChatColor.RED + "Not a valid block. Aborting deletion.");
			plugin.getDeletingBlocks().remove(player);
			return;
		}
		Block deletingBlock = plugin.getDeletingBlocks().get(player);
		if (deletingBlock == null) {
			/* Is it a command block ?*/
			if (plugin.getCommandBlocks().containsKey(block)) {
				plugin.getDeletingBlocks().put(player, block);
				player.sendMessage(ChatColor.GOLD + "Block selected. Click on it again to accept deletion.");
			}
			else {
				player.sendMessage(ChatColor.RED + "This is not a command block. Aborting deletion.");
				plugin.getDeletingBlocks().remove(player);
			}
			
		}
		else if (block.equals(deletingBlock)){
			plugin.getCommandBlocks().remove(block);
			plugin.getDeletingBlocks().remove(player);
			player.sendMessage(ChatColor.GREEN + "Command block properly deleted");
		}
	}
}
