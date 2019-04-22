package be.nokorbis.spigot.commandsigns.api.addons;


public class AddonExecutionData extends AddonData {

	public AddonExecutionData(final Addon addon) {
		super(addon);
	}

	/**
	 * This method is called when a admin copy a command block via the <code>/commandsign copy</code> command.
	 * @return a deep copy of this AddonData
	 */
	public AddonExecutionData copy() {
		return null;
	}
}
