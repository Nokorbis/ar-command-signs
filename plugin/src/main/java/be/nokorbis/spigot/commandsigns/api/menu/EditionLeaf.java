package be.nokorbis.spigot.commandsigns.api.menu;

public abstract class EditionLeaf <EDITABLE extends MenuEditable> extends EditionMenu<EDITABLE> {

	protected static final String           CANCEL_STRING    = "</{!(C@NCEL)!}\\>";
	protected static final ClickableMessage CLICKABLE_CANCEL = new ClickableMessage(messages.get("menu.entry.cancel"), CANCEL_STRING);

	public EditionLeaf(final String name, final EditionMenu<EDITABLE> parent) {
		super(name, parent);
	}

	@Override
	public String getDisplayString(EDITABLE data) {
		return messages.get("menu.entry.display")
					   .replace("{NAME}", name)
					   .replace("{VALUE}", getDataValue(data));
	}

	public abstract String getDataValue(EDITABLE data);
}
