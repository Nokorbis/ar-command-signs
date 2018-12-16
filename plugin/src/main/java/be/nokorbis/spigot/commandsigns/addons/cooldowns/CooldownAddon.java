package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.*;


public class CooldownAddon implements Addon {

	private final String NAME = "ncs_cooldowns";

	private final CooldownExecutionDataUpdater updater = new CooldownExecutionDataUpdater();

	private final CooldownRequirementHandler handler = new CooldownRequirementHandler();

	private final CooldownExecutionDataTransformer executionDataTransformer = new CooldownExecutionDataTransformer(this);
	private final CooldownConfigurationDataTransformer configurationDataTransformer = new CooldownConfigurationDataTransformer(this);

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public RequirementHandler getRequirementHandler() {
		return handler;
	}

	@Override
	public CostHandler getCostHandler() {
		return null;
	}

	@Override
	public CooldownConfigurationData createConfigurationData() {
		return new CooldownConfigurationData(this);
	}

	@Override
	public CooldownExecutionData createExecutionData() {
		return new CooldownExecutionData(this);
	}

	@Override
	public AddonExecutionDataUpdater getAddonExecutionDataUpdater() {
		return this.updater;
	}

	@Override
	public CooldownExecutionDataTransformer getExecutionDataSerializer() {
		return executionDataTransformer;
	}

	@Override
	public CooldownExecutionDataTransformer getExecutionDataDeserializer() {
		return executionDataTransformer;
	}

	@Override
	public CooldownConfigurationDataTransformer getConfigurationDataSerializer() {
		return configurationDataTransformer;
	}

	@Override
	public CooldownConfigurationDataTransformer getConfigurationDataDeserializer() {
		return configurationDataTransformer;
	}
}
