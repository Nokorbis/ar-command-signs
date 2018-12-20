package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.command.Command;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by nokorbis on 1/20/16.
 */
public class PurgeCommand extends CommandRequiringManager {

	public PurgeCommand(NCommandSignsManager manager) {
		super(manager, "purge", new String[0]);
		this.basePermission = "commandsign.admin.purge";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		int cpt = 0;
		Iterator<Map.Entry<Location, CommandBlock>> cmdIte = Container.getContainer().getCommandBlocks().entrySet().iterator();
		while (cmdIte.hasNext()) {
			try {
				Map.Entry<Location, CommandBlock> cmd = cmdIte.next();
				cmd.getValue().validate();
			}
			catch (CommandSignsException e) {
				cpt += 1;
				cmdIte.remove();
			}
		}

		String msg = Messages.get("info.purged_invalid_blocks");
		msg = msg.replace("{AMOUNT}", String.valueOf(cpt));
		sender.sendMessage(msg);
		return true;
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign purge");
	}
}
