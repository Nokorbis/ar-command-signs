package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;


public abstract class EditionMenu<EDITABLE extends MenuEditable> {

    protected static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/menu");

    protected final String name;
    private EditionMenu<EDITABLE> parent;

    public EditionMenu(String name, EditionMenu<EDITABLE> parent) {
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
    public final EditionMenu<EDITABLE> getParent() {
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

    protected final  void displayBreadcrumb(final Player editor) {
        final String divider = messages.get("breadcrumb.divider");
        final String nameColor = messages.get("breadcrumb.name_color");
        StringBuilder sb = new StringBuilder();

        EditionMenu<EDITABLE> currentMenu = this;

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

    public abstract void display(final Player editor, final EDITABLE data, final MenuNavigationContext navigationContext);

    public abstract void input(final Player player, final EDITABLE data, final String message, final MenuNavigationContext navigationContext);
}