package be.nokorbis.spigot.commandsigns.command;

import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;


/**
 * Created by Nokorbis on 1/20/16.
 */
public abstract class Command implements ICommand {
	protected final String   command;
	protected final String[] aliases;
	protected       String   basePermission;

	protected Command(String command, String[] aliases) {
		this.command = command;
		this.aliases = aliases;
	}

	@Override
	public final boolean isCommand(String label) {
		label = label.toLowerCase();
		if (this.command.equals(label)) {
			return true;
		}

		for (String alias : this.aliases) {
			if (alias.equals(label)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void printUsage(CommandSender sender, boolean permission) throws CommandSignsCommandException {
		if (sender.hasPermission(basePermission)) {
			printUsage(sender);
		}
		else if (permission) {
			throw new CommandSignsCommandException(Messages.get("error.no_permission"));
		}
	}

	@Override
	public final String getCommand() {
		return this.command;
	}

	@Override
	public List<String> autoComplete(CommandSender sender, List<String> args) {
		return Collections.emptyList();
	}

	@Override
	public final boolean hasBasePermission(CommandSender sender) {
		return sender.hasPermission(this.basePermission);
	}

}
