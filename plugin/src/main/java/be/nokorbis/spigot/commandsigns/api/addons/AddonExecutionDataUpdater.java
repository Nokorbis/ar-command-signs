package be.nokorbis.spigot.commandsigns.api.addons;

import org.bukkit.entity.Player;


public interface AddonExecutionDataUpdater {
	void update(final AddonConfigurationData configurationData, final AddonExecutionData executionData, final Player player);
}
