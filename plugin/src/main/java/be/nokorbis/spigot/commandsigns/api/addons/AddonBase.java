package be.nokorbis.spigot.commandsigns.api.addons;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.util.Objects;


public abstract class AddonBase implements Addon {

	protected final String identifier;
	protected final String name;

	public AddonBase(String identifier, String name) {
		this.identifier = identifier;
		this.name = name;
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
	public boolean shouldAddonBeHooked() {
		return true;
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
}
