package be.nokorbis.spigot.commandsigns.command;

import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsConfigurationManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public abstract class CommandRequiringManager extends Command{

	protected final NCommandSignsManager manager;

	protected CommandRequiringManager(NCommandSignsManager manager, String command, String[] aliases) {
		super(command, aliases);
		this.manager = manager;
	}

	public NCommandSignsManager getManager() {
		return manager;
	}

	/**
	 * Checks if the player is already doing some creation/edition/deletion about a configuration.
	 *
	 * @param player
	 *
	 * @return <code>true</code> if the player isn't doing anything
	 * <code>false</code> if the player is already doing something
	 *
	 * @throws CommandSignsException
	 */
	protected boolean isPlayerAvailable(Player player) throws CommandSignsCommandException {
		NCommandSignsConfigurationManager configurationManager = manager.getPlayerConfigurationManager(player);
		if (configurationManager != null) {
			if (configurationManager.isCreating()) {
				throw new CommandSignsCommandException(Messages.get("warning.already_creating_configuration"));
			}
			throw new CommandSignsCommandException(Messages.get("warning.already_editing_configuration"));
		}

		if (Container.getContainer().getDeletingBlocks().containsKey(player)) {
			throw new CommandSignsCommandException(Messages.get("warning.already_deleting_configuration"));
		}

		if (Container.getContainer().getCopyingConfigurations().containsKey(player)) {
			throw new CommandSignsCommandException(Messages.get("warning.already_copying_configuration"));
		}

		if (Container.getContainer().getInfoPlayers().contains(player)) {
			throw new CommandSignsCommandException(Messages.get("warning.already_info_configuration"));
		}
		return true;
	}

}
