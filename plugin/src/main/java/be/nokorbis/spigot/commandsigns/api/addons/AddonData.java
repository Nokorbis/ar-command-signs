package be.nokorbis.spigot.commandsigns.api.addons;

public abstract class AddonData implements AddonRelated {

	protected final Addon addon;

	protected AddonData(final Addon addon) {
		this.addon = addon;
	}

	@Override
	public final Addon getAddon() {
		return addon;
	}

}
