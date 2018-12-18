package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.*;

import java.util.Objects;


public class CooldownAddon implements Addon {

	private final String IDENTIFIER = "ncs_cooldowns";

	private final CooldownExecutionDataUpdater updater = new CooldownExecutionDataUpdater();

	private final CooldownRequirementHandler handler = new CooldownRequirementHandler();

	private final CooldownExecutionDataTransformer executionDataTransformer = new CooldownExecutionDataTransformer(this);
	private final CooldownConfigurationDataTransformer configurationDataTransformer = new CooldownConfigurationDataTransformer(this);

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getName() {
		return "Cooldowns";
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

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		Addon that = (Addon) o;
		return Objects.equals(IDENTIFIER, that.getIdentifier());
	}

	@Override
	public int hashCode() {
		return Objects.hash(IDENTIFIER);
	}
}
