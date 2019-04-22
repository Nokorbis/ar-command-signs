package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsConfigurationManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
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
			throw new CommandSignsCommandException(commandMessages.get("error.command.player_requirement"));
		}
		Player player = (Player) sender;

		if (isPlayerAvailable(player)) {
			if (args.isEmpty()) {
				addNewEditionManager(player, null);
				player.sendMessage(commandMessages.get("howto.click_to_edit"));
			}
			else {
				try {
					long id = Long.parseLong(args.get(1));
					CommandBlock commandBlock = manager.getCommandBlock(id);
					if (commandBlock == null) {
						throw new CommandSignsCommandException(commandMessages.get("error.invalid_command_id"));
					}
					addNewEditionManager(player, commandBlock);
				}
				catch (NumberFormatException ex) {
					throw new CommandSignsCommandException(commandMessages.get("error.command.number_requirement"));
				}
			}

			return true;
		}

		return false;
	}

	private void addNewEditionManager(Player player, CommandBlock commandBlock) {
		NCommandSignsConfigurationManager conf = new NCommandSignsConfigurationManager(player, manager);
		conf.setEditing(true);
		conf.setCurrentMenu(manager.getMainMenu());
		if (commandBlock != null) {
			conf.setCommandBlock(commandBlock);
			conf.display();
		}
		manager.addConfigurationManager(conf);
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign edit [ID]");
	}
}
