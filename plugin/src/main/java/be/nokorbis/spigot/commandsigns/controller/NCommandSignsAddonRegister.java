package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.api.AddonRegister;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;


public class NCommandSignsAddonRegister implements AddonRegister {

	private final NCommandSignsManager manager;

	public NCommandSignsAddonRegister(NCommandSignsManager manager) {
		this.manager = manager;
	}

	public void registerAddon(final Addon addon) {
		if (addon != null) {
			this.manager.registerAddon(addon);
		}
	}
}
