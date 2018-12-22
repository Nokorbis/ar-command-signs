package be.nokorbis.spigot.commandsigns.api.addons;

import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import org.bukkit.entity.Player;


public class AddonLifecycleHookerBase implements AddonLifecycleHooker {

	@Override
	public void onStarted(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}

	@Override
	public void onRequirementCheck(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) throws CommandSignsRequirementException {

	}

	@Override
	public void onCostWithdraw(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}

	@Override
	public void onPreExecution(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}

	@Override
	public void onExecution(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}

	@Override
	public void onPostExecution(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}

	@Override
	public void onCompleted(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {

	}
}
