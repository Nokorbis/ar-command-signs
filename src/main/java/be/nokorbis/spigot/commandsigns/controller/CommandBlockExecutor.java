package be.nokorbis.spigot.commandsigns.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.scheduler.BukkitScheduler;

public class CommandBlockExecutor
{
	private static final Pattern ALL_PATTERN = Pattern.compile("%[Aa][Ll][Ll]%");
	private static final Pattern RADIUS_PATTERN = Pattern.compile("%[Rr][Aa][Dd][Ii][Uu][Ss]=(\\d+)%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%[Pp][Ll][Aa][Yy][Ee][Rr]%");

	private static DecimalFormat df;

	private final Player player;
	private final CommandBlock cmdBlock;

	private long timeBeforeExecution;
	private long resetTime;

	public CommandBlockExecutor (Player player, CommandBlock cmdBlock)
	{
		this.player = player;
		this.cmdBlock = cmdBlock;
		if (df == null)
		{
			df = new DecimalFormat();
			DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
			symbols.setGroupingSeparator(' ');
			df.setDecimalFormatSymbols(symbols);
			df.setMaximumFractionDigits(2);
		}
		this.resetTime = System.currentTimeMillis();
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public CommandBlock getCommandBlock()
	{
		return this.cmdBlock;
	}

	public void waitBeforeExecution()
	{
		if (cmdBlock.hasTimer() && !player.hasPermission("commandsign.timer.bypass"))
		{
			try
			{
				timeBeforeExecution = cmdBlock.getTimeBeforeExecution();
				final String msg = Messages.get("info.timer_delayed").replace("{TIME}", String.valueOf(timeBeforeExecution));
				Bukkit.getScheduler().runTask(CommandSignsPlugin.getPlugin(), () ->
				{
					player.sendMessage(msg);
				});
				timeBeforeExecution *= 1000;
				while (timeBeforeExecution > 0)
				{
					Thread.sleep(timeBeforeExecution);
					timeBeforeExecution = (cmdBlock.getTimeBeforeExecution()*1000)-(System.currentTimeMillis()-resetTime);
				}
			}
			catch (InterruptedException e)
			{
				CommandSignsPlugin.getPlugin().getLogger()
						.warning(String.format("An interruption occured while waiting before the execution of a command sign (id : %s, player: %s", cmdBlock.getId(), player.getName()));
			}
		}
	}

	public void checkRequirements() throws CommandSignsException
	{
		if (this.player == null || this.player.isDead() || !this.player.isOnline())
		{
			throw new CommandSignsException(Messages.get("usage.invalid_player"));
		}

		for (String needed : this.cmdBlock.getNeededPermissions())
		{
			if (!this.player.hasPermission(needed))
			{
				String err = Messages.get("usage.miss_needed_permission");
				err = err.replace("{NEEDED_PERM}", needed);
				throw new CommandSignsException(err);
			}
		}

		if (this.cmdBlock.getTimeBetweenUsage() > 0)
		{
			long now = System.currentTimeMillis();
			long toWait = this.cmdBlock.getLastTimeUsed() + (this.cmdBlock.getTimeBetweenUsage()*1000) - now;
			if (toWait > 0)
			{
				if (!this.player.hasPermission("commandsign.timer.bypass"))
				{
					String msg = Messages.get("usage.general_cooldown");
					msg = msg.replace("{TIME}", df.format(this.cmdBlock.getTimeBetweenUsage() - (toWait/1000.0)));
					msg = msg.replace("{REMAINING}", df.format(toWait/1000.0));
					throw new CommandSignsException(msg);
				}
			}
		}

		if (this.cmdBlock.getTimeBetweenPlayerUsage() > 0)
		{
			if (this.cmdBlock.hasPlayerRecentlyUsed(this.player))
			{
				long now = System.currentTimeMillis();
				long lastUsage = this.cmdBlock.getLastTimePlayerRecentlyUsed(this.player);
				long toWait = lastUsage + (this.cmdBlock.getTimeBetweenPlayerUsage()*1000) - now;
				if (!player.hasPermission("commandsign.timer.bypass"))
				{
					String msg = Messages.get("usage.player_cooldown");
					msg = msg.replace("{TIME}", df.format((now-lastUsage)/1000.0));
					msg = msg.replace("{REMAINING}", df.format(toWait/1000.0));
					throw new CommandSignsException(msg);
				}
			}
		}

		if ((Economy.getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0))
		{
			if (!Economy.getEconomy().has(this.player, this.cmdBlock.getEconomyPrice()) && !this.player.hasPermission("commandsign.costs.bypass"))
			{
				String err = Messages.get("usage.not_enough_money");
				err = err.replace("{PRICE}", Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
				throw new CommandSignsException(err);
			}
		}

		if (!this.player.hasPermission("commandsign.timer.bypass"))
		{
			this.cmdBlock.refreshLastTime();
		}
	}

	public boolean execute()
	{
		if (this.player == null)
		{
			return false;
		}

		if ((Economy.getEconomy() != null) && (this.cmdBlock.getEconomyPrice() > 0))
		{
			if (!this.player.hasPermission("commandsign.costs.bypass"))
			{
				if (Economy.getEconomy().has(this.player, this.cmdBlock.getEconomyPrice()))
				{
					Economy.getEconomy().withdrawPlayer(this.player, this.cmdBlock.getEconomyPrice());
					String msg = Messages.get("usage.you_paied");
					msg = msg.replace("{PRICE}",Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
					this.player.sendMessage(msg);
				}
				else
				{
					String err = Messages.get("usage.not_enough_money");
					err = err.replace("{PRICE}", Economy.getEconomy().format(this.cmdBlock.getEconomyPrice()));
					this.player.sendMessage(err);
					return false;
				}
			}
		}

		if (this.cmdBlock.getTimeBetweenPlayerUsage() > 0)
		{
			if (this.cmdBlock.hasPlayerRecentlyUsed(this.player))
			{
				if (!player.hasPermission("commandsign.timer.bypass"))
				{
					this.player.sendMessage(Messages.get("usage.player_cooldown"));
					return false;
				}
			}
		}

		PermissionAttachment perms = Container.getContainer().getPlayerPermissions(this.player);
		for (String perm : this.cmdBlock.getPermissions())
		{
			if (!this.player.hasPermission(perm))
			{
				perms.setPermission(perm, true);
			}
		}

		for (String command : this.cmdBlock.getCommands())
		{
			handleCommand(command);
		}

		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.runTaskLater(plugin, () ->
		{
			this.cmdBlock.addUsage(this.player);
			for (String perm : this.cmdBlock.getPermissions())
			{
				perms.unsetPermission(perm);
			}
		}, 1);


		return true;
	}

	private void handleCommand(String command)
	{
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		for (String cmd : formatCommand(command, this.player))
		{
			char special = cmd.charAt(0);
			if (special == Settings.delayChar)
			{
				try
				{
					int sec = Integer.parseInt(cmd.substring(1).trim());
					Thread.sleep(sec * 1000);
				}
				catch (NumberFormatException e)
				{
					plugin.getLogger().warning("A command sign is using a delay that isn't a number.");
				} catch (InterruptedException e)
				{
					plugin.getLogger().warning("Interrupted exception while delaying a command");
				}
			}
			else
			{
				BukkitScheduler scheduler = plugin.getServer().getScheduler();

				if (special == Settings.opChar)
				{
					scheduler.runTask(plugin, new OpCommandTask(player, "/" + cmd.substring(1)));
				}
				else if (special == Settings.serverChar)
				{
					scheduler.runTask(plugin, new ServerCommandTask(cmd.substring(1)));
				}
				else
				{
					scheduler.runTask(plugin, new DefaultCommandTask(player, cmd));
				}
			}
		}
	}

	public void resetTimer()
	{
		this.resetTime = System.currentTimeMillis();
	}

	private List<String> formatCommand (String command, Player player)
	{
		List<String> cmds = new LinkedList<>();
		String cmd = command;

		Matcher m = PLAYER_PATTERN.matcher(cmd);
		if (m.find())
		{
			cmd = m.replaceAll(player.getName());
		}
		m = ALL_PATTERN.matcher(cmd);
		if (m.find())
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				cmds.add(m.replaceAll(p.getName()));
			}
		}
		else
		{
			m = RADIUS_PATTERN.matcher(cmd);
			if (m.find())
			{
				try
				{
					String str = m.group(1);
					int radius = Integer.parseInt(str);
					if (radius > 0)
					{
						for (Player p : Bukkit.getOnlinePlayers())
						{
							if (p.getWorld().equals(player.getWorld()) && p.getLocation().distance(player.getLocation()) <= radius)
							{
								cmds.add(m.replaceAll(p.getName()));
							}
						}
					}
				}
				catch (Exception ignored)
				{
				}
			}
			else
			{
				cmds.add(cmd);
			}
		}

		return cmds;
	}

	private static class OpCommandTask implements Runnable
	{
		private Player player;
		private String command;

		public OpCommandTask(Player player, String command)
		{
			this.player = player;
			this.command = command;
		}

		@Override
		public void run()
		{
			if (!this.player.isOp())
			{
				this.player.setOp(true);
				this.player.chat(command);
				this.player.setOp(false);
			}
			else
			{
				this.player.chat(command);
			}
		}
	}

	private static class DefaultCommandTask implements Runnable
	{
		private Player player;
		private String command;

		public DefaultCommandTask(Player player, String command)
		{
			this.player = player;
			this.command = command;
		}

		@Override
		public void run()
		{
			this.player.chat(command);
		}
	}

	private static class ServerCommandTask implements Runnable
	{
		private String command;

		public ServerCommandTask(String command)
		{
			this.command = command;
		}

		@Override
		public void run()
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
		}
	}
}
