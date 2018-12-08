package be.nokorbis.spigot.commandsigns.api.addons;

public abstract class AddonData {

	protected final Addon addon;

	protected AddonData(final Addon addon) {
		this.addon = addon;
	}

	public final Addon getAddon() {
		return addon;
	}

}
