package be.nokorbis.spigot.commandsigns.api.menu;

import org.bukkit.ChatColor;

public abstract class EditionLeaf<EDITABLE extends MenuEditable> extends EditionMenu<EDITABLE> {

    public EditionLeaf(final String name, final EditionMenu<EDITABLE> parent) {
        super(name, parent);
    }

    @Override
    public final String getDisplayString(EDITABLE data) {
        return super.getDisplayString(data)+" - ["+ChatColor.GOLD+getDataString(data) + "]";
    }

    public abstract String getDataString(EDITABLE data);
}
