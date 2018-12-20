package be.nokorbis.spigot.commandsigns.api.menu;

import org.bukkit.entity.Player;

import java.util.ResourceBundle;


public abstract class EditionMenu<EDITABLE extends MenuEditable> {

    protected static final ResourceBundle mainMessages = ResourceBundle.getBundle("messages/menu");

    protected final String name;
    private EditionMenu<? extends MenuEditable> parent;

    public EditionMenu(String name, EditionMenu<? extends MenuEditable> parent) {
        this.parent = parent;
        this.name = name;
    }

    public EditionMenu(String name) {
        this(name, null);
    }


    /**
     * Get the name of this menu. <br>
     * Will be displayed in the breadcrumb at the top of each submenu.
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
    public final EditionMenu<? extends MenuEditable> getParent() {
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
    public String getDisplayString(EDITABLE data) {
        return this.name;
    }

    protected final static void displayBreadcrumb(final Player editor, EditionMenu currentMenu) {
        String divider = mainMessages.getString("breadcrumb.divider");
        String nameColor = mainMessages.getString("breadcrumb.name_color");
        StringBuilder sb = new StringBuilder();

        int i = 0;
        while(currentMenu != null && i < 4) {
            sb.insert(0, divider + nameColor + currentMenu.getName());
            currentMenu = currentMenu.getParent();
            i++;
        }

        if(i != 0) {
            sb.delete(0, 3);
        }

        if(i == 4) {
            sb.insert(0, divider);
        }

        editor.sendMessage(sb.toString());
    }

    public abstract void display(final Player editor, final EDITABLE data, final int page);

    public abstract void input(final Player player, final EDITABLE data, final String message, final MenuNavigationResult navigationResult);
}