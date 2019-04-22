package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import org.bukkit.command.CommandSender;

import java.util.List;


/**
 * Created by nokorbis on 1/20/16.
 */
public class PurgeCommand extends CommandRequiringManager {

	public PurgeCommand(NCommandSignsManager manager) {
		super(manager, "purge", new String[0]);
		this.basePermission = "commandsign.admin.purge";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		int cpt = manager.purgeCommandBlocks();

		String msg = commandMessages.get("info.purged_invalid_blocks");
		msg = msg.replace("{AMOUNT}", String.valueOf(cpt));
		sender.sendMessage(msg);
		return true;
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign purge");
	}
}
