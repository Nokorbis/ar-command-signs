package be.nokorbis.spigot.commandsigns.model;

public enum BlockActivationMode {
	ACTIVATED,
	DEACTIVATED,
	BOTH;

	public static BlockActivationMode fromName(String name) {
		name = name.toUpperCase();
		for (BlockActivationMode value : values()) {
			if (value.name().equals(name)) {
				return value;
			}
		}
		return BOTH;
	}
}
