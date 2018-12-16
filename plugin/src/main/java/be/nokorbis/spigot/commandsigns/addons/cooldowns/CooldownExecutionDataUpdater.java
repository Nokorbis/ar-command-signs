package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionDataUpdater;
import org.bukkit.entity.Player;


public class CooldownExecutionDataUpdater implements AddonExecutionDataUpdater {

	@Override
	public void update(final AddonConfigurationData configurationData, final AddonExecutionData executionData, final Player player) {
		final CooldownExecutionData data = (CooldownExecutionData) executionData;

		data.refresh((CooldownConfigurationData) configurationData);
		data.addPlayerUsage(player);
	}
}
