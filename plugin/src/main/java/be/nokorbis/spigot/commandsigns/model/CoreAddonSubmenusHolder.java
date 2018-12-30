package be.nokorbis.spigot.commandsigns.model;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CoreAddonSubmenusHolder {

	public final Map<Addon, List<AddonEditionMenu>> requirementSubmenus = new LinkedHashMap<>();
	public final Map<Addon, List<AddonEditionMenu>> costSubmenus        = new LinkedHashMap<>();
	public final Map<Addon, List<AddonEditionMenu>> executionSubmenus   = new LinkedHashMap<>();

}
