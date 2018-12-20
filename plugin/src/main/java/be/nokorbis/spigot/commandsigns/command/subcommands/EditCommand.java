package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsConfigurationManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * Created by nokorbis on 1/20/16.
 */
public class EditCommand extends CommandRequiringManager {

	public EditCommand(NCommandSignsManager manager) {
		super(manager, "edit", new String[0]);
		this.basePermission = "commandsign.admin.edit";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		if (!(sender instanceof Player)) {
			throw new CommandSignsCommandException(Messages.get("error.player_command"));
		}
		Player player = (Player) sender;

		if (isPlayerAvailable(player)) {
			NCommandSignsConfigurationManager conf;
			if (args.isEmpty()) {
				conf = new NCommandSignsConfigurationManager(player);
				player.sendMessage(Messages.get("howto.click_to_edit"));
			}
			else {
				try {
					long id = Long.parseLong(args.get(1));
					CommandBlock commandBlock = manager.getCommandBlock(id);
					if (commandBlock == null) {
						throw new CommandSignsCommandException(Messages.get("error.invalid_command_id"));
					}
					conf = new NCommandSignsConfigurationManager(player);
					conf.setCommandBlock(commandBlock);
				}
				catch (NumberFormatException ex) {
					throw new CommandSignsCommandException(Messages.get("error.number_argument"));
				}
			}
			conf.setEditing(true);
			conf.setCurrentMenu(manager.getMainMenu());
			if (conf.getCommandBlock() != null) {
				conf.display();
			}

			manager.addConfigurationManager(conf);
			return true;
		}

		return false;
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign edit [ID]");
	}
}
