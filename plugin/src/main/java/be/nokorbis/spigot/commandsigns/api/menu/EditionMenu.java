package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import org.bukkit.entity.Player;


public abstract class EditionMenu<EDITABLE extends MenuEditable> {

    protected static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/menu");

    protected static final int REFRESH = 0;
    protected static final int PREVIOUS = 7;
    protected static final int NEXT = 8;
    protected static final int CANCEL = 8;
    protected static final int DONE = 9;

    protected static final ClickableMessage clickableMessageRefresh  = new ClickableMessage(messages.get("menu.entry.refresh"), Integer.toString(REFRESH));
    protected static final ClickableMessage clickableMessageDone     = new ClickableMessage(messages.get("menu.entry.done"), Integer.toString(DONE));
    protected static final ClickableMessage clickableMessageCancel   = new ClickableMessage(messages.get("menu.entry.configuration_cancel"), Integer.toString(CANCEL));

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
    public EditionMenu<EDITABLE> getParent() {
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
        return messages.get("menu.entry.display_name_only").replace("{NAME}", name);
    }

    public boolean shouldBeDisplayed(EDITABLE data) {
        return true;
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

    protected boolean shouldDisplayCancel() {
        return false;
    }

    protected ClickableMessage getClickableMessagePrevious() {
        final int i= (shouldDisplayCancel()) ? PREVIOUS - 1 : PREVIOUS;
        final String index = Integer.toString(i);
        return new ClickableMessage(messages.get("menu.entry.previous").replace("{INDEX}", index), index);
    }

    protected ClickableMessage getClickableMessageNext() {
        final int i= (shouldDisplayCancel()) ? NEXT - 1 : NEXT;
        final String index = Integer.toString(i);
        return new ClickableMessage(messages.get("menu.entry.next").replace("{INDEX}", index), index);
    }

    public abstract void display(final Player editor, final EDITABLE data, final MenuNavigationContext navigationContext);

    public abstract void input(final Player player, final EDITABLE data, final String message, final MenuNavigationContext navigationContext);
}