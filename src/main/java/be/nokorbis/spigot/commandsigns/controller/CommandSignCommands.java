package be.nokorbis.spigot.commandsigns.controller;

import java.util.*;

import be.nokorbis.spigot.commandsigns.command.ICommand;
import be.nokorbis.spigot.commandsigns.command.subcommands.*;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import be.nokorbis.spigot.commandsigns.utils.Messages;

public class CommandSignCommands implements CommandExecutor, TabCompleter {

	private static final int LIST_SIZE = 10;

	private final List<ICommand> commands;

	public CommandSignCommands () {
		commands = new LinkedList<ICommand>();
		commands.add(new CopyCommand());
		commands.add(new CreateCommand());
		commands.add(new DeleteCommand());
		commands.add(new EditCommand());
		commands.add(new InfoCommand());
		commands.add(new ListCommand());
		commands.add(new NearCommand());
		commands.add(new PurgeCommand());
		commands.add(new TeleportCommand());
		commands.add(new VersionCommand());
		commands.add(new LoadCommand());
		commands.add(new HelpCommand(commands));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			if (sender == null) {
				return false;
			}
			if (args.length < 1) {
				return false;
			}

			List<String> argList = new LinkedList<>(Arrays.asList(args));

			for (ICommand command : this.commands) {
				if (command.isCommand(argList.get(0))) {
					if (command.hasBasePermission(sender)) {
						try {
							return command.execute(sender, argList);
						}
						catch (CommandSignsCommandException e) {
							sender.sendMessage(e.getMessage());
							return true;
						}
					}
					else {
						sender.sendMessage(Messages.get("error.no_permission"));
					}
					return true;
				}
			}
			return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> result = new ArrayList<String>();
		if (args.length == 0) {
			result.add("commandsign");
		}
		else if (args.length == 1) {
			List<String> values = new LinkedList<String>();
			for (ICommand command : this.commands) {
				values.add(command.getCommand());
			}
			result.add(autoCompleteParameter(args[0], values));
		}
		else {
			List<String> argList = new LinkedList<String>(Arrays.asList(args));
			String sub = argList.remove(0);
			for (ICommand command : this.commands) {
				if (command.isCommand(sub)) {
					result.add(autoCompleteParameter(argList.get(0), command.autoComplete(sender, argList)));
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Choose the best available value for the autocompletion
	 *
	 * @param start
	 *            What was the parameter sent
	 * @param values
	 *            What are the possible values
	 * @return the best value
	 */
	private String autoCompleteParameter(String start, List<String> values) {
		if (start == null || start.isEmpty()) {
			return " ";
		}

		List<String> valids = new LinkedList<String>();
		if (values == null) {
			return start;
		}

		for (String value : values) {
			if (value.toLowerCase().startsWith(start.toLowerCase())) {
				valids.add(value);
			}
		}
		if (valids.size() < 1) {
			return start;
		} else if (valids.size() == 1) {
			return valids.get(0);
		} else {
			String base = valids.get(0);
			valids.remove(0);
			StringBuilder builder = new StringBuilder();
			int i = 0;
			boolean done = false;
			while (!done) {
				if (i >= base.length()) {
					break;
				}
				boolean same = true;
				char c = base.charAt(i);
				for (String other : valids) {
					if (other.charAt(i) != c) {
						same = false;
						done = true;
						break;
					}
				}
				if (same) {
					builder.append(c);
				}
				i++;
			}
			return builder.toString();
		}
	}
}
