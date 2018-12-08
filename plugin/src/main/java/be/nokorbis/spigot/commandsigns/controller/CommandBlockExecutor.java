package be.nokorbis.spigot.commandsigns.controller;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import org.bukkit.scheduler.BukkitScheduler;

public class CommandBlockExecutor
{
	private static final Pattern ALL_PATTERN = Pattern.compile("%[Aa][Ll][Ll]%");
	private static final Pattern RADIUS_PATTERN = Pattern.compile("%[Rr][Aa][Dd][Ii][Uu][Ss]=(\\d+)%");
	private static final Pattern PLAYER_PATTERN = Pattern.compile("%[Pp][Ll][Aa][Yy][Ee][Rr]%");

	private static DecimalFormat df;

	private final Player player;
	private final CommandBlock cmdBlock;

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
	}

	public Player getPlayer()
	{
		return this.player;
	}


	public boolean execute()
	{
		if (this.player == null)
		{
			return false;
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
			for (String perm : this.cmdBlock.getPermissions())
			{
				perms.unsetPermission(perm);
			}
		}, 2);


		return true;
	}

	private void handleCommand(String command)
	{
		CommandSignsPlugin plugin = CommandSignsPlugin.getPlugin();
		for (String cmd : formatCommand(command, this.player))
		{
			char special = cmd.charAt(0);
			if (special == Settings.DELAY_CHAR())
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

				if (special == Settings.OP_CHAR())
				{
					scheduler.runTask(plugin, new OpCommandTask(player, "/" + cmd.substring(1)));
				}
				else if (special == Settings.SERVER_CHAR())
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
