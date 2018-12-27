package be.nokorbis.spigot.commandsigns.api.menu;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import be.nokorbis.spigot.commandsigns.utils.Messages;
import org.bukkit.entity.Player;

public abstract class EditionNode<EDITABLE extends MenuEditable> extends EditionMenu<EDITABLE> {

    protected final List<EditionMenu<EDITABLE>> menus;

    private boolean displayPageNavigation = false;
    private int entriesToDisplay = 6;

    public EditionNode(String name, EditionMenu<EDITABLE> parent) {
        super(name, parent);
        this.menus = new ArrayList<>();
        initializeSubMenus();
        initializeNavigation();
    }

    public EditionNode(String name) {
        this(name, null);
    }

    public void addMenu(EditionMenu<EDITABLE> menu) {
        menus.add(menu);
    }

    protected abstract void initializeSubMenus();

    private void initializeNavigation() {
        if (menus.size() > 8) {
            displayPageNavigation = true;
            entriesToDisplay = 6;
        }
        else {
            displayPageNavigation = false;
            entriesToDisplay = menus.size();
        }
    }

    @Override
    public void display(final Player editor, final EDITABLE data, final MenuNavigationContext navigationContext) {
        displayBreadcrumb(editor);

        displaySubMenus(editor, data, navigationContext);
    }

    protected final void displaySubMenus(Player editor, EDITABLE data, MenuNavigationContext navigationContext) {
        editor.sendMessage(Messages.get("menu.entry.refresh"));

        final int page = navigationContext.getPage();
        final int startingIndex = (page-1) * entriesToDisplay;

        ListIterator<EditionMenu<EDITABLE>> menuIterator = menus.listIterator(startingIndex);
        for (int i = 1; i <= entriesToDisplay && menuIterator.hasNext(); i++) {
            EditionMenu<EDITABLE> menu = menuIterator.next();
            ClickableMessage message = new ClickableMessage(menu.getDisplayString(data), String.valueOf(i));
            editor.spigot().sendMessage(message.asTextComponent());
        }

        if (displayPageNavigation) {
            editor.sendMessage(Messages.get("menu.entry.previous"));
            editor.sendMessage(Messages.get("menu.entry.next"));
        }
        editor.sendMessage(Messages.get("menu.entry.done"));
    }

    @Override
    public void input(final Player player, final EDITABLE data, final String message, final MenuNavigationContext navigationResult) {

    }

    /**
     * Action to take when an edition is about to end
     */
    public boolean complete(final Player player, final EDITABLE data) {
        return true;
    }
}