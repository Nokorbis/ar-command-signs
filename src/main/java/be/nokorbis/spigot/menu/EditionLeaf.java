package be.nokorbis.spigot.menu;

import org.bukkit.ChatColor;

public abstract class EditionLeaf<T> extends EditionMenu<T> {

    public EditionLeaf(String name, EditionMenu<T> parent) {
        super(name, parent);
    }

    @Override
    public final String formatName(T data) {
        return super.formatName(data)+" - ["+ChatColor.GOLD+getDataString(data)+ c +"]";
    }

    public abstract String getDataString(T data);
}
