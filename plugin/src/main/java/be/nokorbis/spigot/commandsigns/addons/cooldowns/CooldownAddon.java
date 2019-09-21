package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.addons.cooldowns.data.*;
import be.nokorbis.spigot.commandsigns.addons.cooldowns.menus.MenuCooldowns;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;

import java.util.HashMap;
import java.util.Map;


public class CooldownAddon extends AddonBase {

	private static final String IDENTIFIER = "ncs_cooldowns";

	private final CooldownLifecycleHooker lifecycleHooker = new CooldownLifecycleHooker(this);
	private final MenuCooldowns           editionMenu     = new MenuCooldowns(this);

	private final CooldownExecutionDataPersister     executionDataTransformer     = new CooldownExecutionDataPersister(this);
	private final CooldownConfigurationDataPersister configurationDataTransformer = new CooldownConfigurationDataPersister(this);

	public CooldownAddon(CommandSignsPlugin plugin) {
		super(plugin, IDENTIFIER, "Cooldowns");
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return lifecycleHooker;
	}

	@Override
	public AddonSubmenuHolder getSubmenus() {
		AddonSubmenuHolder holder = new AddonSubmenuHolder();
		holder.requirementSubmenus.add(editionMenu);
		return holder;
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
	public Class<CooldownExecutionData> getExecutionDataClass() {
		return CooldownExecutionData.class;
	}

	@Override
	public CooldownExecutionDataPersister getExecutionDataSerializer() {
		return executionDataTransformer;
	}

	@Override
	public CooldownExecutionDataPersister getExecutionDataDeserializer() {
		return executionDataTransformer;
	}

	@Override
	public Class<CooldownConfigurationData> getConfigurationDataClass() {
		return CooldownConfigurationData.class;
	}

	@Override
	public CooldownConfigurationDataPersister getConfigurationDataSerializer() {
		return configurationDataTransformer;
	}

	@Override
	public CooldownConfigurationDataPersister getConfigurationDataDeserializer() {
		return configurationDataTransformer;
	}

	@Override
	public Map<String, AddonConfigurationDataEditor> getDataEditors() {
		HashMap<String, AddonConfigurationDataEditor> editors = new HashMap<>(4);

		editors.put("ncs.global_cooldown", new GlobalCooldownDataEditor(this));
		editors.put("ncs.player_cooldown", new PlayerCooldownDataEditor(this));
		editors.put("ncs.global_once", new GlobalOnceDataEditor(this));
		editors.put("ncs_player_once", new PlayerOnceDataEditor(this));

		return editors;
	}
}
