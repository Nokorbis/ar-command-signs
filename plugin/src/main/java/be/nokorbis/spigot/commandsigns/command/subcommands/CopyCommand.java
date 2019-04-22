package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandBlockPendingInteraction;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


/**
 * Created by nokorbis on 1/20/16.
 */
public class CopyCommand extends CommandRequiringManager {

	public CopyCommand(NCommandSignsManager manager) {
		super(manager, "copy", new String[] {"cp"});
		this.basePermission = "commandsign.admin.copy";
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
		if (!(sender instanceof Player)) {
			throw new CommandSignsCommandException(commandMessages.get("error.command.player_requirement"));
		}

		Player player = (Player) sender;

		if (isPlayerAvailable(player)) {
			if (args.isEmpty()) {
				addPendingCopy(player, null);
				player.sendMessage(commandMessages.get("howto.click_to_copy"));
			}
			else {
				try {
					long id = Long.parseLong(args.get(0));
					CommandBlock commandBlock = manager.getCommandBlock(id);
					if (commandBlock == null) {
						throw new CommandSignsCommandException(commandMessages.get("error.invalid_command_id"));
					}
					addPendingCopy(player, commandBlock);
					player.sendMessage(commandMessages.get("howto.click_to_paste"));
				}
				catch (NumberFormatException ex) {
					throw new CommandSignsCommandException(commandMessages.get("error.command.number_requirement"));
				}
			}

			return true;
		}

		return false;
	}

	private void addPendingCopy(Player player, CommandBlock commandBlock) {
		CommandBlockPendingInteraction interaction = new CommandBlockPendingInteraction();
		interaction.type = CommandBlockPendingInteraction.Type.COPY;
		interaction.player = player;
		if (commandBlock != null) {
			interaction.commandBlock = commandBlock.copy();
		}
		manager.addPendingInteraction(interaction);
	}

	@Override
	public void printUsage(CommandSender sender) {
		sender.sendMessage("/commandsign copy [ID]");
	}
}
