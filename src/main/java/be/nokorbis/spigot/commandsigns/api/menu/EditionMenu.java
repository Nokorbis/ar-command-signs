package be.nokorbis.spigot.commandsigns.api.menu;


import be.nokorbis.spigot.commandsigns.model.CommandBlock;

public abstract class EditionMenu
{
    private String name;
    private EditionMenu parent;

    public EditionMenu(String name, EditionMenu parent)
    {
        this.parent = parent;
        this.name = name;
    }

    public EditionMenu(String name)
    {
        this(name, null);
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
    public final EditionMenu getParent() {
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
    public String getDisplayString(CommandBlock data)
    {
        return this.name;
    }

    public abstract void display(EditingConfiguration config);

    public abstract void input(EditingConfiguration config, String message);
}