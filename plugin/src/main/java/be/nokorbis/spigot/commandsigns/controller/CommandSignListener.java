package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.menus.old.CommandsAddMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.Bukkit;
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


public class CommandSignListener implements Listener
{
	private NCommandSignsManager manager;

	public CommandSignListener(NCommandSignsManager manager)
	{
		this.manager = manager;
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event)
	{
		NCommandSignsConfigurationManager configurationManager = this.manager.getConfigurationManager(event.getPlayer());
		if (configurationManager != null)
		{
			boolean treated = configurationManager.handleCommandInput(event.getMessage());
			if (treated)
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		if (this.manager.hasCommandSignsAdjacentToBlock(event.getBlock()))
		{
			event.getPlayer().sendMessage(Messages.get("error.break_attempt_failed"));
			event.setCancelled(true);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onChatEvent(AsyncPlayerChatEvent event)
	{
		NCommandSignsConfigurationManager configurationManager = this.manager.getConfigurationManager(event.getPlayer());
		if (configurationManager != null)
		{
			boolean treated = configurationManager.handleChatInput(event.getMessage());
			if (treated)
			{
				event.getRecipients().clear();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		manager.handlePlayerExit(event.getPlayer());
	}

	@EventHandler (ignoreCancelled = true)
	public void onInteractEvent(PlayerInteractEvent event)
	{

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		if (block == null)
		{
			return;
		}
		if (block.getLocation() == null)
		{
			return;
		}

		/* Do we have to delete this command block ? */
		if (Container.getContainer().getDeletingBlocks().containsKey(player))
		{
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				return;
			}
			deleteCommandBlock(player, block);
		}

		/* Do we have to edit the command block configuration ? */
		else if (Container.getContainer().getEditingConfigurations().containsKey(player))
		{
			if (!CommandBlockValidator.isValidBlock(block))
			{
				return;
			}

			EditingConfiguration<CommandBlock> conf = Container.getContainer().getEditingConfigurations().get(player);
			CommandBlock commandBlock = conf.getEditingData();

			// We want to select the block to edit.
			if (commandBlock == null)
			{
				// The block we hit is a valid block
				if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
				{
					CommandBlock editingBlock = Container.getContainer().getCommandBlocks().get(block.getLocation());
					conf.setEditingData(editingBlock);
					conf.display();
				}
				else
				{
					player.sendMessage(ChatColor.DARK_RED + Messages.get("error.invalid_block_abort"));
					Container.getContainer().getEditingConfigurations().remove(player);
				}
			}
			// We've already selected the block we want to edit
			else
			{
				// Nothing to do, I think
			}
		}

		/* Do we have to create the command block configuration ? */
		else if (Container.getContainer().getCreatingConfigurations().containsKey(player))
		{
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				return;
			}
			createCommandBlock(player, block);
		}

		/* Do we have to copy the command block configuration ? */
		else if (Container.getContainer().getCopyingConfigurations().containsKey(player))
		{
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				return;
			}
			copyCommandBlock(player, block);
		}

		else if (Container.getContainer().getInfoPlayers().contains(player))
		{
			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			{
				return;
			}
			info(player, block);
		}

		/* Is that a block that we can execute ? */
		else if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
		{
			if (CommandBlockValidator.isPlate(block) && (!event.getAction().equals(Action.PHYSICAL)))
			{
				return;
			}
			executeCommandBlock(player, block);
		}
	}

	private void executeCommandBlock(Player player, Block block)
	{
		CommandBlock cmd = Container.getContainer().getCommandBlocks().get(block.getLocation());

		if (cmd != null)
		{
			if (cmd.isDisabled())
			{
				player.sendMessage(Messages.get("usage.disabled"));
			}
			else
			{
				try
				{
					CommandBlockExecutor executor = new CommandBlockExecutor(player, cmd);
					long time = 0;
					if (cmd.hasTimer() && !player.hasPermission("commandsign.timer.bypass"))
					{
						executor.checkRequirements();
						time = cmd.getTimeBeforeExecution();
						String msg = Messages.get("info.timer_delayed");
						msg = msg.replace("{TIME}", String.valueOf(time));
						player.sendMessage(msg);
					}

					ExecuteTask exe = new ExecuteTask(executor);
					exe.setLocation(player.getLocation().getBlock().getLocation());
					Container.getContainer().getExecutingTasks().put(player.getUniqueId(), exe);

					BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

					BukkitTask task = scheduler.runTaskLaterAsynchronously(CommandSignsPlugin.getPlugin(), exe, time * 20);
					exe.setTaskId(task.getTaskId());
				}
				catch (CommandSignsException ex)
				{
					player.sendMessage(ChatColor.DARK_RED + ex.getMessage());
				}
				catch (Exception ex)
				{
					player.sendMessage(ChatColor.DARK_RED + Messages.get("error.requirements_check"));
				}
			}
		}
	}

	@EventHandler (ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		if ((player == null) || !player.isOnline() || player.isDead())
		{
			// Yes, there is no reason to be here
			return;
		}

		if (Container.getContainer().getExecutingTasks().containsKey(player.getUniqueId()))
		{
			ExecuteTask exe = Container.getContainer().getExecutingTasks().get(player.getUniqueId());

			if (player.getLocation().getBlock().getLocation().equals(exe.getLocation()))
			{
				return;
			}

			if (exe.getCommandBlock().isCancelledOnMove())
			{
				CommandSignsPlugin.getPlugin().getServer().getScheduler().cancelTask(exe.getTaskId());
				Container.getContainer().getExecutingTasks().remove(player.getUniqueId());
				exe.getPlayer().sendMessage(Messages.get("usage.execution_cancelled"));
				return;
			}
			if (exe.getCommandBlock().isResetOnMove())
			{
				BukkitScheduler sch = CommandSignsPlugin.getPlugin().getServer().getScheduler();
				sch.cancelTask(exe.getTaskId());
				Container.getContainer().getExecutingTasks().remove(player.getUniqueId());
				BukkitTask task = sch.runTaskLater(CommandSignsPlugin.getPlugin(), exe, exe.getCommandBlock().getTimeBeforeExecution() * 20);
				exe.setTaskId(task.getTaskId());
				exe.setLocation(player.getLocation().getBlock().getLocation());
				Container.getContainer().getExecutingTasks().put(player.getUniqueId(), exe);
				exe.getPlayer().sendMessage(Messages.get("usage.execution_timer_reset"));
			}
		}
	}

	private void info(Player player, Block block)
	{
		if (!CommandBlockValidator.isValidBlock(block))
		{
			player.sendMessage(ChatColor.RED + Messages.get("error.invalid_block_abort"));
			Container.getContainer().getInfoPlayers().remove(player);
			return;
		}

		if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
		{
			CommandSignUtils.info(player, Container.getContainer().getCommandBlocks().get(block.getLocation()));
			Container.getContainer().getInfoPlayers().remove(player);
		}
		else
		{
			player.sendMessage(ChatColor.RED + Messages.get("error.invalid_block_abort"));
			Container.getContainer().getInfoPlayers().remove(player);
		}
	}

	private void createCommandBlock(Player player, Block block)
	{
		if (!CommandBlockValidator.isValidBlock(block))
		{
			return;
		}

		EditingConfiguration<CommandBlock> conf = Container.getContainer().getCreatingConfigurations().get(player);
		CommandBlock commandBlock = conf.getEditingData();
		Location creatingBlock = commandBlock.getLocation();

		if (creatingBlock == null)
		{
			if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
			{
				player.sendMessage(Messages.get("creation.already_command"));
			}
			else
			{
				commandBlock.setLocation(block.getLocation());
				player.sendMessage(Messages.get("creation.block_set"));
			}
		}
		else
		{
			player.sendMessage(Messages.get("creation.already_command"));
		}
	}

	private void copyCommandBlock(Player player, Block block)
	{
		if (!CommandBlockValidator.isValidBlock(block))
		{
			player.sendMessage(ChatColor.RED + Messages.get("error.invalid_block_abort"));
			Container.getContainer().getCopyingConfigurations().remove(player);
			return;
		}

		CommandBlock copyingBlock = Container.getContainer().getCopyingConfigurations().get(player);
		if (copyingBlock == null)
		{
			if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
			{
				copyingBlock = Container.getContainer().getCommandBlocks().get(block.getLocation());
				Container.getContainer().getCopyingConfigurations().put(player, copyingBlock.copy());
				player.sendMessage(Messages.get("howto.click_to_paste"));
			}
			else
			{
				player.sendMessage(Messages.get("error.invalid_block_abort"));
				Container.getContainer().getCopyingConfigurations().remove(player);
			}
		}
		else if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
		{
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getCopyingConfigurations().remove(player);
		}
		else
		{
			copyingBlock.setLocation(block.getLocation());
			Container.getContainer().getCommandBlocks().put(block.getLocation(), copyingBlock);
			Container.getContainer().getCopyingConfigurations().remove(player);
			Container.getContainer().getSaver().save(copyingBlock);
			player.sendMessage(Messages.get("info.block_copied"));
		}
	}

	private void deleteCommandBlock(Player player, Block block)
	{
		if (!CommandBlockValidator.isValidBlock(block))
		{
			player.sendMessage(Messages.get("error.invalid_block_abort"));
			Container.getContainer().getDeletingBlocks().remove(player);
			return;
		}
		Location deletingBlock = Container.getContainer().getDeletingBlocks().get(player);
		if (deletingBlock == null)
		{
			/* Is it a command block ?*/
			if (Container.getContainer().getCommandBlocks().containsKey(block.getLocation()))
			{
				Container.getContainer().getDeletingBlocks().put(player, block.getLocation());
				player.sendMessage(Messages.get("howto.click_confirm_deletion"));
			}
			else
			{
				player.sendMessage(Messages.get("error.invalid_block_abort"));
				Container.getContainer().getDeletingBlocks().remove(player);
			}

		}
		else if (block.getLocation().equals(deletingBlock))
		{
			CommandBlock tmp = Container.getContainer().getCommandBlocks().remove(block.getLocation());
			Container.getContainer().getDeletingBlocks().remove(player);
			Container.getContainer().getSaver().delete(tmp.getId());
			player.sendMessage(ChatColor.GREEN + Messages.get("info.command_deleted"));
		}
	}
}
