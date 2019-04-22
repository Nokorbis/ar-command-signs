package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.List;


/**
 * Created by Nokorbis on 22/08/2016.
 */
public class LoadCommand extends CommandRequiringManager {

	public LoadCommand(NCommandSignsManager manager) {
		super(manager, "reload", new String[] {"load", "unload"});
		this.basePermission = "commandsign.admin.reload";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		if (!sender.hasPermission(basePermission)) {
			throw new CommandSignsCommandException(commandMessages.get("error.no_permission"));
		}

		manager.reloadConfigurations();
		sender.sendMessage(commandMessages.get("success.reload_done"));

		return true;
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign reload");
	}
}
