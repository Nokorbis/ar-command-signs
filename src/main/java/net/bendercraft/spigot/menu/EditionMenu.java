package net.bendercraft.spigot.menu;

import org.bukkit.ChatColor;

public abstract class EditionMenu<T> {

    /**
     * Default color for chat message that are sent to the players
     */
    protected static final ChatColor c = ChatColor.AQUA;

    private String name;

    private EditionMenu<T> parent;

    public EditionMenu(String name) {
        this(name, null);
    }

    public EditionMenu(String name, EditionMenu<T> parent) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * Get the name of this menu
     *
     * @return
     * 		A String with the name
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Get the parent menu of this menu
     *
     * @return <code>null</code> if this is the main menu
     *         <code>An EditionMenu</code> otherwise
     */
    public final EditionMenu<T> getParent() {
        return this.parent;
    }

    /**
     * Get the name that must be shown in the menu
     *
     * @param data
     *        The edition data
     * @return
     * 		A String containing the format name to show
     */
    public String formatName(T data) {
        return this.name;
    }

    public abstract void display(EditingConfiguration<T> config);

    public abstract void input(EditingConfiguration<T> config, String message);
}