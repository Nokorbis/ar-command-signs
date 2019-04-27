package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.List;


public class DebugCommand extends CommandRequiringManager {

	public DebugCommand(NCommandSignsManager manager) {
		super(manager, "debug", new String[]{"dbg"});
		this.basePermission = "commandsign.admin.debug";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		manager.debug(sender);
		return true;
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign debug");
	}
}
