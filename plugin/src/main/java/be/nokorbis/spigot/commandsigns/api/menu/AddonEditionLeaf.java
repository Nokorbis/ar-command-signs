package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;


public abstract class AddonEditionLeaf extends AddonEditionMenu {
	protected static final String           CANCEL_STRING    = "</{!(C@NCEL)!}\\>";
	protected static final ClickableMessage CLICKABLE_CANCEL = new ClickableMessage(messages.get("menu.entry.cancel"), CANCEL_STRING);

	public AddonEditionLeaf(final Addon addon, final String name, final AddonEditionMenu parent) {
		super(addon, name, parent);
	}

	@Override
	public String getDisplayString(AddonConfigurationData data) {
		return messages.get("menu.entry.display")
					   .replace("{NAME}", name)
					   .replace("{VALUE}", getDataValue(data));
	}

	public abstract String getDataValue(AddonConfigurationData data);
}
