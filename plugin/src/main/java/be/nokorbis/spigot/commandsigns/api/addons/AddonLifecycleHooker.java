package be.nokorbis.spigot.commandsigns.api.addons;

import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import org.bukkit.entity.Player;


public interface AddonLifecycleHooker {
	void onStarted			(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
	void onRequirementCheck	(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException;
	void onCostWithdraw		(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
	void onPreExecution		(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
	void onExecution		(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
	void onPostExecution	(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
	void onCompleted		(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
}
