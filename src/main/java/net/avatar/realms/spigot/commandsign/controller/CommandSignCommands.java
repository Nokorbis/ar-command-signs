package net.avatar.realms.spigot.commandsign.controller;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.avatar.realms.spigot.commandsign.CommandSign;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsException;
import net.avatar.realms.spigot.commandsign.utils.CommandSignUtils;
import net.avatar.realms.spigot.commandsign.utils.Messages;

public class CommandSignCommands implements CommandExecutor{

	private static final int LIST_SIZE = 10;

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
				return edit((Player) sender, args);
			}
			else if (subCmd.equals("DELETE") || subCmd.equals("DEL")
					|| subCmd.equals("REMOVE") || subCmd.equals("REM")) {
				return delete ((Player) sender);
			}
			else if (subCmd.equals("COPY") || subCmd.equals("CP")) {
				return copy ((Player) sender, args);
			}
			else if (subCmd.equals("INFO")) {
				return info ((Player) sender, args);
			}
			else if (subCmd.equals("PURGE")) {
				return purge((Player) sender);
			}
			else if (subCmd.equals("NEAR") || subCmd.equals("AROUND")) {
				return near((Player) sender, args);
			}
			else if (subCmd.equals("LIST")) {
				return list((Player) sender, args);
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

	private boolean list(Player sender, String[] args) throws CommandSignsException {
		if (!sender.hasPermission("commandsign.admin.*") && !sender.hasPermission("commandsign.admin.list")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		int index = 1;
		if (args.length > 2) {
			try {
				index = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException ex) {
			}
		}
		sender.sendMessage(ChatColor.AQUA + "Command signs list : " + index);
		int max = index * LIST_SIZE;
		for (index = max - LIST_SIZE ; index < max; index++) {
			CommandBlock cmd = this.plugin.getCommandBlockById(index);
			StringBuilder builder = new StringBuilder();
			builder.append(ChatColor.AQUA);
			builder.append(cmd.blockSummary());
			builder.append(" --- ");
			if (cmd.getName() != null) {
				builder.append(cmd.getName());
			}
			else {
				builder.append(Messages.NO_NAME);
			}
			builder.append(cmd.getId());
			sender.sendMessage(builder.toString());
		}
		return true;
	}

	private boolean info(Player player, String[] args) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.info")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}
		if (args.length < 2) {
			if (!isPlayerAvailable(player)) {
				return false;
			}

			this.plugin.getInfoPlayers().add(player);
			player.sendMessage(ChatColor.GOLD + "Click on command block whose you want information");
		}
		else {
			try {
				long id = Long.parseLong(args[1]);
				CommandBlock cmd = this.plugin.getCommandBlockById(id);
				if (cmd == null) {
					throw new CommandSignsException(Messages.INVALID_COMMAND_ID);
				}
				cmd.info(player, ChatColor.DARK_GREEN);
			}
			catch (NumberFormatException ex) {
				throw new CommandSignsException(Messages.NUMBER_ARGUMENT);
			}
		}

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

	private boolean edit (Player player, String[] args) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.edit")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			EditingConfiguration<CommandBlock> conf = null;
			if (args.length < 2) {
				conf = new EditingConfiguration<CommandBlock>(player, false);
				player.sendMessage(ChatColor.GOLD + "Click on the block you want to edit");
			}
			else {
				try {
					long id = Long.parseLong(args[1]);
					CommandBlock cmd = this.plugin.getCommandBlockById(id);
					if (cmd == null) {
						throw new CommandSignsException(Messages.INVALID_COMMAND_ID);
					}
					conf = new EditingConfiguration<CommandBlock>(player, cmd, false);
				}
				catch (NumberFormatException ex) {
					throw new CommandSignsException(Messages.NUMBER_ARGUMENT);
				}
			}

			conf.setCurrentMenu(CommandSign.getPlugin().getMainMenu());
			if (conf.getEditingData() != null) {
				conf.display();
			}
			this.plugin.getEditingConfigurations().put(player, conf);
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

	private boolean copy (Player player, String[] args) throws CommandSignsException {
		if (!player.hasPermission("commandsign.admin.*") && !player.hasPermission("commandsign.admin.copy")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (isPlayerAvailable(player)) {
			if (args.length < 2) {
				this.plugin.getCopyingConfigurations().put(player, null);
				player.sendMessage(ChatColor.GOLD + "Click on the command block you want to copy.");
			}
			else {
				try {
					long id = Long.parseLong(args[1]);
					CommandBlock cmd = this.plugin.getCommandBlockById(id);
					if (cmd == null) {
						throw new CommandSignsException(Messages.INVALID_COMMAND_ID);
					}
					this.plugin.getCopyingConfigurations().put(player, cmd.copy());
					player.sendMessage(ChatColor.GOLD + "Block copied. Click on another block to paste the configuration.");
				}
				catch (NumberFormatException ex) {
					throw new CommandSignsException(Messages.NUMBER_ARGUMENT);
				}
			}
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

	private boolean near(Player sender, String[] args) throws CommandSignsException {
		if (!sender.hasPermission("commandsign.admin.*") && !sender.hasPermission("commandsign.admin.near")) {
			throw new CommandSignsException(Messages.NO_PERMISSION);
		}

		if (args.length < 2) {
			throw new CommandSignsException(Messages.COMMAND_NEEDS_RADIUS);
		}

		try {
			int radius = Integer.parseInt(args[1]);
			LinkedList<CommandBlock> cmds = new LinkedList<CommandBlock>();
			for (Location loc : CommandSignUtils.getLocationsAroundPoint(sender.getLocation(), radius)) {
				if (this.plugin.getCommandBlocks().containsKey(loc))  {
					cmds.add(this.plugin.getCommandBlocks().get(loc));
				}
			}
			for (CommandBlock cmd : cmds) {
				sender.sendMessage("Block [" + cmd.getId() +"] at " + cmd.blockSummary());
			}
		}
		catch (NumberFormatException ex) {
			throw new CommandSignsException(Messages.NUMBER_ARGUMENT);
		}


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
