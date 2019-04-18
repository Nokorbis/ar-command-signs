package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import be.nokorbis.spigot.commandsigns.utils.Messages;
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


public class CommandSignListener implements Listener {

	private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/events");

	private NCommandSignsManager manager;

	public CommandSignListener(NCommandSignsManager manager) {
		this.manager = manager;
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		NCommandSignsConfigurationManager configurationManager = this.manager.getPlayerConfigurationManager(event.getPlayer());
		if (configurationManager != null) {
			boolean treated = configurationManager.handleCommandInput(event.getMessage());
			if (treated) {
				event.setCancelled(true);
				configurationManager.display();
			}
		}
	}

	@EventHandler( priority = EventPriority.LOWEST )
	public void onChatEvent(AsyncPlayerChatEvent event) {
		NCommandSignsConfigurationManager configurationManager = this.manager.getPlayerConfigurationManager(event.getPlayer());
		if (configurationManager != null) {
			boolean treated = configurationManager.handleChatInput(event.getMessage());
			if (treated) {
				event.getRecipients().clear();
				event.setCancelled(true);
				configurationManager.display();
			}
		}
	}

	@EventHandler( priority = EventPriority.LOW )
	public void onPlayerLeave(PlayerQuitEvent event) {
		manager.handlePlayerExit(event.getPlayer());
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		if (this.manager.hasCommandSignsAdjacentToBlock(event.getBlock())) {
			event.getPlayer().sendMessage(Messages.get("error.break_attempt_failed"));
			event.setCancelled(true);
		}
	}

	@EventHandler( ignoreCancelled = true )
	public void onInteractEvent(PlayerInteractEvent event) {

		Block touchedBlock = event.getClickedBlock();
		Player player = event.getPlayer();

		if (touchedBlock == null || touchedBlock.getLocation() == null) {
			return;
		}

		/* Do we have to delete this command block ? */
		if (Container.getContainer().getDeletingBlocks().containsKey(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			deleteCommandBlock(player, touchedBlock);
		}

		NCommandSignsConfigurationManager configurationManager = manager.getPlayerConfigurationManager(player);
		if (configurationManager != null) {
			if (CommandBlockValidator.isValidBlock(touchedBlock)) {
				if (configurationManager.isEditing()) {
					if (configurationManager.getCommandBlock() == null) {
						CommandBlock commandBlock = manager.getCommandBlock(touchedBlock.getLocation());
						if (commandBlock == null) {
							player.sendMessage(messages.get("error.invalid_block_abort"));
							manager.removeConfigurationManager(player);
						}
						else {
							configurationManager.setCommandBlock(commandBlock);
							configurationManager.display();
						}
					}
				}
				else {
					CommandBlock commandBlock = configurationManager.getCommandBlock();
					Location location = commandBlock.getLocation();
					if (location == null) {
						if (manager.isCommandBlock(touchedBlock)) {
							player.sendMessage(messages.get("creation.already_configured"));
						}
						else {
							commandBlock.setLocation(touchedBlock.getLocation());
							player.sendMessage(messages.get("creation.block_set"));
						}
					}
					else {
						player.sendMessage(messages.get("creation.already_positioned"));
					}
				}
			}
		}

		/* Do we have to copy the command block configuration ? */
		else if (Container.getContainer().getCopyingConfigurations().containsKey(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			copyCommandBlock(player, touchedBlock);
		}

		else if (Container.getContainer().getInfoPlayers().contains(player)) {
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
			info(player, touchedBlock);
		}

		/* Is that a block that we can execute ? */
		else if (Container.getContainer().getCommandBlocks().containsKey(touchedBlock.getLocation())) {
			if (CommandBlockValidator.isPlate(touchedBlock) && (!event.getAction().equals(Action.PHYSICAL))) {
				return;
			}
			executeCommandBlock(player, touchedBlock);
		}
	}

	private void executeCommandBlock(Player player, Block block) {
		CommandBlock cmd = Container.getContainer().getCommandBlocks().get(block.getLocation());

		if (cmd != null) {
			if (cmd.isDisabled()) {
				player.sendMessage(Messages.get("usage.disabled"));
			}
			else {
				try {
					NCommandBlockExecutor nExecutor = new NCommandBlockExecutor(player, cmd);
					nExecutor.run();
				}
				catch (Exception ex) {
					player.sendMessage(Messages.get("error.requirements_check"));
				}
			}
		}
	}

	@EventHandler( ignoreCancelled = true )
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ((player == null) || !player.isOnline() || player.isDead()) {
			// Yes, there is no reason to be here
			return;
		}

		if (Container.getContainer().getExecutingTasks().containsKey(player.getUniqueId())) {
			ExecuteTask exe = Container.getContainer().getExecutingTasks().get(player.getUniqueId());

			if (player.getLocation().getBlock().getLocation().equals(exe.getInitialLocation())) {
				return;
			}

			if (exe.getCommandBlock().isCancelledOnMove()) {
				CommandSignsPlugin.getPlugin().getServer().getScheduler().cancelTask(exe.getTaskId());
				Container.getContainer().getExecutingTasks().remove(player.getUniqueId());
				exe.getPlayer().sendMessage(Messages.get("usage.execution_cancelled"));
				return;
			}
			if (exe.getCommandBlock().isResetOnMove()) {
				BukkitScheduler sch = CommandSignsPlugin.getPlugin().getServer().getScheduler();
				sch.cancelTask(exe.getTaskId());
				Container.getContainer().getExecutingTasks().remove(player.getUniqueId());
				BukkitTask task = sch.runTaskLater(CommandSignsPlugin.getPlugin(), exe, exe.getCommandBlock().getTimeBeforeExecution() * 20);
				exe.setTaskId(task.getTaskId());
				exe.setInitialLocation(player.getLocation().getBlock().getLocation());
				Container.getContainer().getExecutingTasks().put(player.getUniqueId(), exe);
				exe.getPlayer().sendMessage(Messages.get("usage.execution_timer_reset"));
			}
		}
	}

	private void info(Player player, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getInfoPlayers().remove(player);
			return;
		}

		if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation())) {
			CommandSignUtils.info(player, Container.getContainer().getCommandBlocks().get(block.getLocation()));
			Container.getContainer().getInfoPlayers().remove(player);
		}
		else {
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getInfoPlayers().remove(player);
		}
	}

	private void copyCommandBlock(Player player, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(ChatColor.RED + Messages.get("error.invalid_block_abort"));
			Container.getContainer().getCopyingConfigurations().remove(player);
			return;
		}

		CommandBlock copyingBlock = Container.getContainer().getCopyingConfigurations().get(player);
		if (copyingBlock == null) {
			if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation())) {
				copyingBlock = Container.getContainer().getCommandBlocks().get(block.getLocation());
				Container.getContainer().getCopyingConfigurations().put(player, copyingBlock.copy());
				player.sendMessage(Messages.get("howto.click_to_paste"));
			}
			else {
				player.sendMessage(Messages.get("error.invalid_block_abort"));
				Container.getContainer().getCopyingConfigurations().remove(player);
			}
		}
		else if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation())) {
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getCopyingConfigurations().remove(player);
		}
		else {
			copyingBlock.setLocation(block.getLocation());
			Container.getContainer().getCommandBlocks().put(block.getLocation(), copyingBlock);
			Container.getContainer().getCopyingConfigurations().remove(player);
			Container.getContainer().getSaver().save(copyingBlock);
			player.sendMessage(Messages.get("info.block_copied"));
		}
	}

	private void deleteCommandBlock(Player player, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getDeletingBlocks().remove(player);
			return;
		}
		Location deletingBlock = Container.getContainer().getDeletingBlocks().get(player);
		if (deletingBlock == null) {
			/* Is it a command block ?*/
			if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation())) {
				Container.getContainer().getDeletingBlocks().put(player, block.getLocation());
				player.sendMessage(Messages.get("howto.click_confirm_deletion"));
			}
			else {
				player.sendMessage(Messages.get("error.invalid_block_abort"));
				Container.getContainer().getDeletingBlocks().remove(player);
			}

		}
		else if (block.getLocation().equals(deletingBlock)) {
			CommandBlock tmp = Container.getContainer().getCommandBlocks().remove(block.getLocation());
			Container.getContainer().getDeletingBlocks().remove(player);
			Container.getContainer().getSaver().delete(tmp.getId());
			player.sendMessage(ChatColor.GREEN + Messages.get("info.command_deleted"));
		}
	}
}
