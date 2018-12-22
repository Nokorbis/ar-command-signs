package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.*;


public class CooldownAddon extends AddonBase {

	private static final String IDENTIFIER    = "ncs_cooldowns";

	private final CooldownLifecycleHooker lifecycleHooker = new CooldownLifecycleHooker();
	private final CooldownExecutionDataTransformer executionDataTransformer = new CooldownExecutionDataTransformer(this);
	private final CooldownConfigurationDataTransformer configurationDataTransformer = new CooldownConfigurationDataTransformer(this);

	public CooldownAddon() {
		super(IDENTIFIER,"Cooldowns");
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return lifecycleHooker;
	}

	@Override
	public final CooldownConfigurationData createConfigurationData() {
		return new CooldownConfigurationData(this);
	}

	@Override
	public final CooldownExecutionData createExecutionData() {
		return new CooldownExecutionData(this);
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
