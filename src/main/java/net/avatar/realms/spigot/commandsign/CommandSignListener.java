package net.avatar.realms.spigot.commandsign;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import net.avatar.realms.spigot.commandsign.controller.CommandBlockExecutor;
import net.avatar.realms.spigot.commandsign.controller.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.menu.CommandsAddMenu;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsException;
import net.avatar.realms.spigot.commandsign.tasks.ExecuteTask;
import net.avatar.realms.spigot.commandsign.utils.CommandSignUtils;


public class CommandSignListener implements Listener{

	private CommandSign plugin;

	public CommandSignListener (CommandSign plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerCommand (PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.getCreatingConfigurations().containsKey(player) || this.plugin.getEditingConfigurations().containsKey(player)) {
			EditingConfiguration<CommandBlock> conf = this.plugin.getCreatingConfigurations().get(player);
			if (conf == null) {
				conf = this.plugin.getEditingConfigurations().get(player);
			}
			if (conf.getCurrentMenu() instanceof CommandsAddMenu) {
				String msg = event.getMessage();
				conf.input(msg);
				conf.display();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreakEvent (BlockBreakEvent event) {
		Block block =  event.getBlock();
		// This is a command block, so this should not be delete
		if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
			Player player = event.getPlayer();
			if (player != null) {
				player.sendMessage(ChatColor.RED + "This block is a command block. You must remove the commands before deleting it.");
			}
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.HIGH)
	public void onChatEvent (AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();

		if (!(this.plugin.getCreatingConfigurations().containsKey(player) || this.plugin.getEditingConfigurations().containsKey(player))) {
			return;
		}
		EditingConfiguration<CommandBlock> conf = this.plugin.getCreatingConfigurations().get(player);
		if (conf == null) {
			conf = this.plugin.getEditingConfigurations().get(player);
		}

		String str = event.getMessage();
		conf.input(str);
		conf.display();
		event.setCancelled(true);
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (this.plugin.getCopyingConfigurations().containsKey(player)) {
			this.plugin.getCopyingConfigurations().remove(player);
		}
		if (this.plugin.getEditingConfigurations().containsKey(player)) {
			this.plugin.getEditingConfigurations().remove(player);
		}
		if (this.plugin.getCreatingConfigurations().containsKey(player)) {
			this.plugin.getCreatingConfigurations().remove(player);
		}
		if (this.plugin.getInfoPlayers().contains(player)) {
			this.plugin.getInfoPlayers().remove(player);
		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onInteractEvent (PlayerInteractEvent event) {

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		if (block == null) {
			return;
		}
		if (block.getLocation() == null) {
			return;
		}

		/* Do we have to delete this command block ? */
		if (this.plugin.getDeletingBlocks().containsKey(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			deleteCommandBlock(player, block);
		}

		/* Do we have to edit the command block configuration ? */
		else if (this.plugin.getEditingConfigurations().containsKey(player)) {
			if (!CommandSignUtils.isValidBlock(block)) {
				return;
			}

			EditingConfiguration<CommandBlock> conf = this.plugin.getEditingConfigurations().get(player);
			CommandBlock commandBlock = conf.getEditingData();

			// We want to select the block to edit.
			if (commandBlock == null) {
				// The block we hit is a valid block
				if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
					CommandBlock editingBlock = this.plugin.getCommandBlocks().get(block.getLocation());
					conf.setEditingData(editingBlock);
					conf.display();
				}
				else {
					player.sendMessage(ChatColor.DARK_RED + "The selected block is not valid, aborting...");
					this.plugin.getEditingConfigurations().remove(player);
				}
			}
			// We've already selected the block we want to edit
			else {
				// Nothing to do, I think
			}
		}

		/* Do we have to create the command block configuration ? */
		else if (this.plugin.getCreatingConfigurations().containsKey(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			createCommandBlock(player, block);
		}

		/* Do we have to copy the command block configuration ? */
		else if (this.plugin.getCopyingConfigurations().containsKey(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			copyCommandBlock(player, block);
		}

		else if (this.plugin.getInfoPlayers().contains(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			info (player, block);
		}

		/* Is that a block that we can execute ? */
		else if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
			if (CommandSignUtils.isPlate(block) && (!event.getAction().equals(Action.PHYSICAL))){
				return;
			}
			executeCommandBlock(player, block);
		}
	}

	private void executeCommandBlock(Player player, Block block) {
		CommandBlock cmd = this.plugin.getCommandBlocks().get(block.getLocation());

		if (cmd != null) {
			try {
				CommandBlockExecutor executor = new CommandBlockExecutor(player, cmd);
				executor.checkRequirements();
				if (!cmd.hasTimer() || player.hasPermission("commandsign.timer.bypass")) {
					executor.execute();
				}
				else {
					ExecuteTask exe = new ExecuteTask(executor);
					exe.setLocation(player.getLocation().getBlock().getLocation());
					CommandSign.getPlugin().getExecutingTasks().put(player.getUniqueId(), exe);
					BukkitTask task = CommandSign.getPlugin().getServer().getScheduler().runTaskLater(CommandSign.getPlugin(),
							exe, cmd.getTimer() * 20);
					exe.setTaskId(task.getTaskId());
					player.sendMessage(
							"Command sign execution delayed by a timer. Please wait " + cmd.getTimer() + " seconds.");
				}
			}
			catch (CommandSignsException ex) {
				player.sendMessage(ChatColor.DARK_RED + ex.getMessage());
			}
			catch (Exception ex) {
				player.sendMessage(ChatColor.DARK_RED + "An error occured while checking requirements.");
			}

		}
	}

	@EventHandler(ignoreCancelled=true)
	public void onPlayerMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ((player == null) || !player.isOnline() || player.isDead()) {
			// Yes, there is no reason to be here
			return;
		}

		if (CommandSign.getPlugin().getExecutingTasks().containsKey(player.getUniqueId())) {
			ExecuteTask exe = CommandSign.getPlugin().getExecutingTasks().get(player.getUniqueId());

			if (player.getLocation().getBlock().getLocation().equals(exe.getLocation())) {
				return;
			}

			if (exe.getCommandBlock().isCancelledOnMove()) {
				CommandSign.getPlugin().getServer().getScheduler().cancelTask(exe.getTaskId());
				CommandSign.getPlugin().getExecutingTasks().remove(player.getUniqueId());
				exe.getPlayer().sendMessage(ChatColor.RED + "Command sign execution cancelled.");
				return;
			}
			if (exe.getCommandBlock().isResetOnMove()) {
				BukkitScheduler sch = CommandSign.getPlugin().getServer().getScheduler();
				sch.cancelTask(exe.getTaskId());
				CommandSign.getPlugin().getExecutingTasks().remove(player.getUniqueId());
				BukkitTask task = sch.runTaskLater(CommandSign.getPlugin(), exe, exe.getCommandBlock().getTimer() * 20);
				exe.setTaskId(task.getTaskId());
				exe.setLocation(player.getLocation().getBlock().getLocation());
				CommandSign.getPlugin().getExecutingTasks().put(player.getUniqueId(), exe);
				exe.getPlayer().sendMessage(ChatColor.RED + "Command sign execution timer reset");
			}
		}
	}

	private void info(Player player, Block block) {
		if (!CommandSignUtils.isValidBlock(block)) {
			player.sendMessage(ChatColor.RED + "Invalid block. Aborting info command");
			this.plugin.getInfoPlayers().remove(player);
			return;
		}

		if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
			this.plugin.getCommandBlocks().get(block.getLocation()).info(player, ChatColor.DARK_GREEN);
			this.plugin.getInfoPlayers().remove(player);
		}
		else {
			player.sendMessage(ChatColor.RED + "Invalid block. Aborting info command");
			this.plugin.getInfoPlayers().remove(player);
		}
	}

	private void createCommandBlock(Player player, Block block) {
		if (!CommandSignUtils.isValidBlock(block)) {
			return;
		}

		EditingConfiguration<CommandBlock> conf = this.plugin.getCreatingConfigurations().get(player);
		CommandBlock commandBlock = conf.getEditingData();
		Location creatingBlock = commandBlock.getLocation();

		if (creatingBlock == null) {
			if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
				player.sendMessage(ChatColor.RED + "This block is already a command block.");
			}
			else {
				commandBlock.setLocation(block.getLocation());
				player.sendMessage(ChatColor.GREEN + "Block set to command block configuration");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "This configuration already has a block.");
		}
	}

	private void copyCommandBlock(Player player, Block block) {
		if (!CommandSignUtils.isValidBlock(block)) {
			player.sendMessage(ChatColor.RED + "Not a valid block. Aborting copying.");
			this.plugin.getCopyingConfigurations().remove(player);
			return;
		}

		CommandBlock copyingBlock = this.plugin.getCopyingConfigurations().get(player);
		if (copyingBlock == null) {
			if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
				copyingBlock = this.plugin.getCommandBlocks().get(block.getLocation());
				this.plugin.getCopyingConfigurations().put(player, copyingBlock.copy());
				player.sendMessage(ChatColor.GOLD + "Block copied. Click on another block to paste the configuration.");
			}
			else {
				player.sendMessage(ChatColor.RED + "This is not a command block. Aborting copying.");
				this.plugin.getCopyingConfigurations().remove(player);
			}
		}
		else if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
			player.sendMessage(ChatColor.RED + "This block is already a command block. Aborting copying.");
			this.plugin.getCopyingConfigurations().remove(player);
		}
		else {
			copyingBlock.setLocation(block.getLocation());
			this.plugin.getCommandBlocks().put(block.getLocation(), copyingBlock);
			this.plugin.getCopyingConfigurations().remove(player);
			player.sendMessage(ChatColor.GREEN + "Block properly copied.");
		}
	}

	private void deleteCommandBlock(Player player, Block block) {
		if (!CommandSignUtils.isValidBlock(block)) {
			player.sendMessage(ChatColor.RED + "Not a valid block. Aborting deletion.");
			this.plugin.getDeletingBlocks().remove(player);
			return;
		}
		Location deletingBlock = this.plugin.getDeletingBlocks().get(player);
		if (deletingBlock == null) {
			/* Is it a command block ?*/
			if (this.plugin.getCommandBlocks().containsKey(block.getLocation())) {
				this.plugin.getDeletingBlocks().put(player, block.getLocation());
				player.sendMessage(ChatColor.GOLD + "Block selected. Click on it again to accept deletion.");
			}
			else {
				player.sendMessage(ChatColor.RED + "This is not a command block. Aborting deletion.");
				this.plugin.getDeletingBlocks().remove(player);
			}

		}
		else if (block.getLocation().equals(deletingBlock)){
			this.plugin.getCommandBlocks().remove(block.getLocation());
			this.plugin.getDeletingBlocks().remove(player);
			player.sendMessage(ChatColor.GREEN + "Command block properly deleted");
		}
	}
}
