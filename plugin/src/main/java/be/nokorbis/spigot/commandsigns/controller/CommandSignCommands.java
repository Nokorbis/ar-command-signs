package be.nokorbis.spigot.commandsigns.controller;

import java.util.*;
import java.util.stream.Collectors;

import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.command.ICommand;
import be.nokorbis.spigot.commandsigns.command.subcommands.*;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandSignCommands implements CommandExecutor, TabCompleter {
	private final List<ICommand> commands;
	private final DisplayMessages commandsMessages = DisplayMessages.getDisplayMessages("messages/commands");

	public CommandSignCommands (NCommandSignsManager manager) {
		commands = new LinkedList<>();
		commands.add(new CopyCommand(manager));
		commands.add(new CreateCommand(manager));
		commands.add(new DeleteCommand(manager));
		commands.add(new EditCommand(manager));
		commands.add(new InfoCommand(manager));
		commands.add(new ListCommand(manager));
		commands.add(new NearCommand(manager));
		commands.add(new PurgeCommand(manager));
		commands.add(new TeleportCommand(manager));
		//commands.add(new SetCommand(manager));
		commands.add(new VersionCommand());
		commands.add(new LoadCommand(manager));
		commands.add(new DebugCommand(manager));
		commands.add(new HelpCommand(commands));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 1) {
			return false;
		}

		List<String> argList = new LinkedList<>(Arrays.asList(args));
		String sub = argList.remove(0);

		for (ICommand command : this.commands) {
			if (command.isCommand(sub)) {
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
					sender.sendMessage(commandsMessages.get("error.no_permission"));
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			return Collections.singletonList("commandsign");
		}
		else if (args.length == 1) {
			final String start = args[0].toLowerCase();
			return this.commands.stream()
					.map(ICommand::getCommand)
					.filter(value -> value.startsWith(start))
					.sorted()
					.collect(Collectors.toList());
		}
		else {
			List<String> argList =new ArrayList<>(Arrays.asList(args));
			String sub = argList.remove(0);
			for (ICommand command : this.commands) {
				if (command.isCommand(sub)) {
					return command.autoComplete(sender, argList);
				}
			}
		}
		return Collections.emptyList();
	}
}
