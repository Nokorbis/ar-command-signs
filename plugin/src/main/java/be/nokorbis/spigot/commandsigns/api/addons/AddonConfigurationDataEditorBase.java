package be.nokorbis.spigot.commandsigns.api.addons;

public abstract class AddonConfigurationDataEditorBase implements AddonConfigurationDataEditor {

    protected final Addon addon;

    protected AddonConfigurationDataEditorBase(final Addon addon) {
        this.addon = addon;
    }

    protected final boolean parseBooleanValue(String value) {
        value = value.toUpperCase();
        if ("Y".equals(value) || "YES".equals(value) || "TRUE".equals(value)) {
            return true;
        }
        return false;
    }

    @Override
    public Addon getAddon() {
        return addon;
    }
}
