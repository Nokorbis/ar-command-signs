package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;

import java.util.LinkedList;
import java.util.List;


public class AddonSubmenuHolder {

	public final List<EditionMenu<AddonConfigurationData>> requirementSubmenus = new LinkedList<>();
	public final List<EditionMenu<AddonConfigurationData>> costSubmenus = new LinkedList<>();
	public final List<EditionMenu<AddonConfigurationData>> executionSubmenus = new LinkedList<>();

}
