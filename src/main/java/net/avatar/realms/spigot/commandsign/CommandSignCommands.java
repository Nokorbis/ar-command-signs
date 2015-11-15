package net.avatar.realms.spigot.commandsign;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsException;
import net.avatar.realms.spigot.commandsign.model.EditingConfiguration;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class CommandSignCommands implements CommandExecutor{

	private CommandSign plugin;

	public CommandSignCommands (CommandSign plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		try {
			if (!(sender instanceof Player)) {
				throw new CommandSignsException(Messages.PLAYER_COMMAND);
			}

			if (args.length < 1 ) {
				throw new CommandSignsException(Messages.COMMAND_NEEDS_ARGUMENTS);
			}
			String subCmd = args[0].toUpperCase();
			if (subCmd.equals("CREATE") || subCmd.equals("CR")
					|| subCmd.equals("MK") || subCmd.equals("MAKE")) {
				return create((Player) sender);
			}
			else if (subCmd.equals("EDIT")) {
				return edit((Player) sender);
			}
			else if (subCmd.equals("DELETE") || subCmd.equals("DEL")
					|| subCmd.equals("REMOVE") || subCmd.equals("REM")) {
				return delete ((Player) sender);
			}
			else if (subCmd.equals("COPY") || subCmd.equals("CP")) {
				return copy ((Player) sender);
			}
			else if (subCmd.equals("INFO")) {
				return info ((Player) sender);
			}
			else if (subCmd.equals("PURGE")) {
				return purge((Player) sender);
			}
			else if (subCmd.equals("VERSION") || subCmd.equals("V")) {
				sender.sendMessage(ChatColor.AQUA + "CommandSign version : " + CommandSign.getPlugin().getDescription().getVersion());
				return true;
			}
			else {
				sender.sendMessage(ChatColor.RED + "Invalid Subcommand. Must be : version, info, copy, create, edit, delete or purge");
				return false;
			}
		}
		catch (CommandSignsException ex) {
			sender.sendMessage(ChatColor.DARK_RED + ex.getMessage());
			return true;
		}
	}

	/*
	 * These commands are only initiating command block creation/edition/deletion.
	 * The real configuration is made in the listener.
	 */

	private boolean info(Player player) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.info")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (!isPlayerAvailable(player)) {
			return false;
		}

		this.plugin.getInfoPlayers().add(player);
		player.sendMessage(ChatColor.GOLD + "Click on command block whose you want information");
		return true;
	}

	private boolean create (Player player) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.create")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			CommandBlock cmdBlock = new CommandBlock();

			EditingConfiguration<CommandBlock> ecf = new EditingConfiguration<CommandBlock>(player, cmdBlock, true);
			ecf.setCurrentMenu(CommandSign.getPlugin().getMainMenu());
			ecf.display();
			this.plugin.getCreatingConfigurations().put(player, ecf);

			return true;
		}
		return false;
	}

	private boolean edit (Player player) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.edit")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			EditingConfiguration<CommandBlock> conf = new EditingConfiguration<CommandBlock>(player, false);
			conf.setCurrentMenu(CommandSign.getPlugin().getMainMenu());
			this.plugin.getEditingConfigurations().put(player, conf);
			player.sendMessage(ChatColor.GOLD + "Click on the block you want to edit");

			return true;
		}

		return false;
	}

	private boolean delete (Player player) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.delete")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			this.plugin.getDeletingBlocks().put(player, null);
			player.sendMessage(ChatColor.GOLD + "Click on the command block you want to delete.");
			return true;
		}

		return false;
	}

	private boolean copy (Player player) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.copy")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			this.plugin.getCopyingConfigurations().put(player, null);
			player.sendMessage(ChatColor.GOLD + "Click on the command block you want to copy.");

			return true;
		}

		return false;
	}

	private boolean purge(Player sender) throws CommandSignsException {
		if (!sender.hasPermission("commandsign.admin.*")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		LinkedList<Location> toRemove = new LinkedList<Location>();
		for (CommandBlock cmd : this.plugin.getCommandBlocks().values()) {
			if (!cmd.validate()) {
				toRemove.add(cmd.getLocation());
			}
		}

		for (Location loc : toRemove) {
			this.plugin.getCommandBlocks().remove(loc);
		}

		sender.sendMessage(ChatColor.GREEN + "Purged " + toRemove.size() + " invalid command blocks.");
		return true;
	}

	/**
	 * Checks if the player is already doing some creation/edition/deletion about a configuration.
	 * @param player
	 * @return <code>true</code> if the player isn't doing anything
	 * <code>false</code> if the player is already doing something
	 * @throws CommandSignsException 
	 */
	private boolean isPlayerAvailable(Player player) throws CommandSignsException {
		if (this.plugin.getCreatingConfigurations().containsKey(player)) {
			throw new CommandSignsException(Messages.ALREADY_CREATING_CONFIGURATION);
		}

		if (this.plugin.getEditingConfigurations().containsKey(player)) {
			throw new CommandSignsException(Messages.ALREADY_EDITING_CONFIGURATION);
		}

		if (this.plugin.getDeletingBlocks().containsKey(player)) {
			throw new CommandSignsException(Messages.ALREADY_DELETING_CONFIGURATION);
		}

		if (this.plugin.getCopyingConfigurations().containsKey(player)) {
			throw new CommandSignsException(Messages.ALREADY_COPYING_CONFIGURATION);
		}

		if (this.plugin.getInfoPlayers().contains(player)) {
			throw new CommandSignsException(Messages.ALREADY_INFO_CONFIGURATION);
		}
		return true;
	}

}
