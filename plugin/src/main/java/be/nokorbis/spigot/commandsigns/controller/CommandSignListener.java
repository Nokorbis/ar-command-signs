package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandBlockPendingInteraction;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import org.bukkit.Bukkit;
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
			event.getPlayer().sendMessage(messages.get("error.break_attempt_failed"));
			event.setCancelled(true);
		}
	}

	@EventHandler( ignoreCancelled = true )
	public void onInteractEvent(PlayerInteractEvent event) {

		Block touchedBlock = event.getClickedBlock();
		Player player = event.getPlayer();

		if (touchedBlock == null) {
			return;
		}

		CommandBlockPendingInteraction pendingInteraction = manager.getPendingInteraction(player);
		if (pendingInteraction != null) {
			if (Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) {
				if (pendingInteraction.type == CommandBlockPendingInteraction.Type.DELETE) {
					deleteCommandBlock(player, pendingInteraction, touchedBlock);
				}
				else if (pendingInteraction.type == CommandBlockPendingInteraction.Type.COPY) {
					copyCommandBlock(player, pendingInteraction, touchedBlock);
				}
				else if (pendingInteraction.type == CommandBlockPendingInteraction.Type.INFO) {
					info(player, touchedBlock);
				}
			}
			return;
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
			return;
		}

		if (!CommandBlockValidator.isPlate(touchedBlock) || event.getAction().equals(Action.PHYSICAL)) {
			CommandBlock commandBlock = manager.getCommandBlock(touchedBlock.getLocation());
			executeCommandBlock(player, commandBlock);
		}
	}

	private void executeCommandBlock(Player player, CommandBlock commandBlock) {
		if (commandBlock != null) {
			if (commandBlock.isDisabled()) {
				player.sendMessage(messages.get("usage.disabled"));
			}
			else {
				try {
					NCommandBlockExecutor nExecutor = new NCommandBlockExecutor(player, commandBlock);
					nExecutor.run();
				}
				catch (Exception ex) {
					player.sendMessage(messages.get("error.requirements_check"));
				}
			}
		}
	}

	@EventHandler( ignoreCancelled = true )
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (!player.isOnline() || player.isDead()) {
			return;
		}

		ExecuteTask runningExecutor = manager.getRunningExecutor(player);
		if (runningExecutor != null) {
			if (!runningExecutor.getInitialLocation().equals(player.getLocation().getBlock().getLocation())) {
				CommandBlock commandBlock = runningExecutor.getCommandBlock();
				if (commandBlock.isCancelledOnMove()) {
					Bukkit.getScheduler().cancelTask(runningExecutor.getTaskId());
					manager.removeRunningExecutor(player);
					player.sendMessage(messages.get("usage.execution_cancelled"));
				}
				else if (commandBlock.isResetOnMove()) {
					BukkitScheduler sch = Bukkit.getScheduler();
					sch.cancelTask(runningExecutor.getTaskId());

					BukkitTask task = sch.runTaskLaterAsynchronously(CommandSignsPlugin.getPlugin(), runningExecutor, runningExecutor.getCommandBlock().getTimeBeforeExecution() * 20);
					runningExecutor.setTaskId(task.getTaskId());
					runningExecutor.setInitialLocation(player.getLocation().getBlock().getLocation());

					player.sendMessage(messages.get("usage.execution_timer_reset"));
				}
			}
		}
	}

	private void info(Player player, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(messages.get("error.invalid_block_abort"));
		}
		else {
			CommandBlock commandBlock = manager.getCommandBlock(block.getLocation());
			if (commandBlock == null) {
				player.sendMessage(messages.get("error.invalid_block_abort"));
			}
			else {
				CommandSignUtils.info(player, commandBlock, manager.getAddons());
			}
		}
		manager.removePendingInteraction(player);
	}

	private void copyCommandBlock(Player player, CommandBlockPendingInteraction interaction, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(messages.get("error.invalid_block_abort"));
			manager.removePendingInteraction(player);
			return;
		}

		CommandBlock copyingBlock = interaction.commandBlock;
		CommandBlock commandBlock = manager.getCommandBlock(block.getLocation());
		if (copyingBlock == null) {
			if (commandBlock != null) {
				interaction.commandBlock = commandBlock.copy();
				player.sendMessage(messages.get("howto.click_to_paste"));
			}
			else {
				player.sendMessage(messages.get("error.invalid_block_abort"));
				manager.removePendingInteraction(player);
			}
		}
		else if (commandBlock != null) {
			player.sendMessage(messages.get("error.invalid_block_abort"));
			manager.removePendingInteraction(player);
		}
		else {
			copyingBlock.setLocation(block.getLocation());
			manager.removePendingInteraction(player);
			manager.saveCommandBlock(copyingBlock);
			player.sendMessage(messages.get("success.block_copied"));
		}
	}

	private void deleteCommandBlock(Player player, CommandBlockPendingInteraction interaction, Block block) {
		if (!CommandBlockValidator.isValidBlock(block)) {
			player.sendMessage(messages.get("error.invalid_block_abort"));
			manager.removePendingInteraction(player);
			return;
		}
		CommandBlock deletingBlock = interaction.commandBlock;
		CommandBlock commandBlock = manager.getCommandBlock(block.getLocation());
		if (deletingBlock == null) {
			/* Is it a command block ?*/
			if (commandBlock != null) {
				interaction.commandBlock = commandBlock;
				player.sendMessage(messages.get("howto.click_confirm_deletion"));
			}
			else {
				player.sendMessage(messages.get("error.invalid_block_abort"));
				manager.removePendingInteraction(player);
			}

		}
		else if (block.getLocation().equals(deletingBlock.getLocation())) {
			manager.removePendingInteraction(player);
			manager.deleteCommandBlock(commandBlock);
			player.sendMessage(messages.get("success.command_deleted"));
		}
	}
}
