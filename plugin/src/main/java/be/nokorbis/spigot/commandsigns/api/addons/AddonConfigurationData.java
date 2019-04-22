package be.nokorbis.spigot.commandsigns.api.addons;


import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import org.bukkit.entity.Player;


public class AddonConfigurationData extends AddonData implements MenuEditable {

	protected static final DisplayMessages addonMessages = DisplayMessages.getDisplayMessages("messages/addons");

	public AddonConfigurationData(final Addon addon) {
		super(addon);
	}

	/**
	 * This method is called when a admin copy a command block via the <code>/commandsign copy</code> command.
	 * @return a deep copy of this AddonData
	 */
	public AddonConfigurationData copy() {
		return null;
	}

	public void info(Player player) {

	}
}
