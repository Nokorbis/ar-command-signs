package be.nokorbis.spigot.commandsigns.tasks;

import be.nokorbis.spigot.commandsigns.controller.CommandBlockExecutor;
import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public class ExecuteTask implements Runnable
{
	private CommandBlockExecutor executor;
	//private int taskId;
	private Location location;
    private boolean executionCancelled;

	public ExecuteTask(CommandBlockExecutor cmd)
	{
		this.executor = cmd;
		this.executionCancelled = false;
	}

	@Override
	public void run ()
	{
		if ((this.executor.getPlayer() == null) || !this.executor.getPlayer().isOnline() || this.executor.getPlayer().isDead())
		{
			Container.getContainer().removeExecuteTask(this);
			return;
		}
		try
		{
			this.executor.waitBeforeExecution();
			if (!executionCancelled)
			{
				this.executor.checkRequirements();
				this.executor.execute();
			}
		}
		catch (CommandSignsException e)
		{
			Bukkit.getScheduler().runTask(CommandSignsPlugin.getPlugin(), () ->
			{
				this.executor.getPlayer().sendMessage(e.getMessage());
			});
		}
		finally
		{
			Container.getContainer().getExecutingTasks(getPlayer()).remove(this);
		}

	}

	public CommandBlock getCommandBlock()
	{
		return this.executor.getCommandBlock();
	}

	public Location getLocation ()
	{
		return this.location;
	}

	public void setLocation (Location location)
	{
		this.location = location;
	}

	public Player getPlayer()
	{
		return this.executor.getPlayer();
	}

	public void cancel()
	{
		this.executionCancelled = true;
		this.getPlayer().sendMessage(Messages.get("usage.execution_cancelled"));
	}

	public void resetTimer()
	{
		this.location = this.getPlayer().getLocation().getBlock().getLocation();
		getPlayer().sendMessage(Messages.get("usage.execution_timer_reset"));
		this.executor.resetTimer();
	}
}
