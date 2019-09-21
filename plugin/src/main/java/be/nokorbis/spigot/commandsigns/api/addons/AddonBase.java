package be.nokorbis.spigot.commandsigns.api.addons;

import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.Objects;


public abstract class AddonBase implements Addon {

	protected final Plugin plugin;
	private final   String identifier;
	protected final String name;

	public AddonBase(Plugin plugin, String identifier, String name) {
		this.plugin = plugin;
		this.identifier = identifier;
		this.name = name;
	}

	@Override
	public void onEnable() {
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final Plugin getPlugin() {
		return plugin;
	}

	@Override
	public boolean shouldAddonBeHooked() {
		return true;
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return null;
	}

	@Override
	public AddonSubmenuHolder getSubmenus() {
		return null;
	}

	@Override
	public final boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		AddonBase addonBase = (AddonBase) o;
		return identifier.equals(addonBase.identifier);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(identifier);
	}

	@Override
	public AddonConfigurationData createConfigurationData() {
		return null;
	}

	@Override
	public AddonExecutionData createExecutionData() {
		return null;
	}

	@Override
	public JsonSerializer<? extends AddonExecutionData> getExecutionDataSerializer() {
		return null;
	}

	@Override
	public JsonDeserializer<? extends AddonExecutionData> getExecutionDataDeserializer() {
		return null;
	}

	@Override
	public JsonSerializer<? extends AddonConfigurationData> getConfigurationDataSerializer() {
		return null;
	}

	@Override
	public JsonDeserializer<? extends AddonConfigurationData> getConfigurationDataDeserializer() {
		return null;
	}

	@Override
	public Class<? extends AddonExecutionData> getExecutionDataClass() {
		return null;
	}

	@Override
	public Class<? extends AddonConfigurationData> getConfigurationDataClass() {
		return null;
	}

	@Override
	public Map<String, AddonConfigurationDataEditor> getDataEditors() {
		return null;
	}
}
