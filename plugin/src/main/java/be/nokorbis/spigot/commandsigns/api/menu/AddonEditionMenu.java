package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonRelated;


public abstract class AddonEditionMenu extends EditionMenu<AddonConfigurationData> implements AddonRelated {

	protected final Addon addon;
	protected final AddonEditionMenu parent;

	public AddonEditionMenu(Addon addon, String name, AddonEditionMenu parent) {
		super(name, parent);
		this.addon = addon;
		this.parent = parent;
	}

	@Override
	public Addon getAddon() {
		return addon;
	}

	@Override
	public AddonEditionMenu getParent() {
		return parent;
	}
}
