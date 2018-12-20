package be.nokorbis.spigot.commandsigns.api.menu;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class EditionNode<EDITABLE extends MenuEditable> extends EditionMenu<EDITABLE> {
    protected final List<EditionMenu<? extends MenuEditable>> menus;

    private boolean displayPageNavigation = false;
    private int entriesToDisplay = 6;

    public EditionNode(String name, EditionMenu parent) {
        super(name, parent);
        this.menus = new ArrayList<>();
        initializeSubMenus();
        initializeNavigation();
    }

    public EditionNode(String name) {
        this(name, null);
    }

    public void addMenu(EditionMenu menu) {
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
    public void display(final Player editor, final EDITABLE data, final int page) {
        displayBreadcrumb(editor, this);

        editor.sendMessage(mainMessages.getString("menu.entry.refresh"));

        final int startingIndex = (page-1) * entriesToDisplay;

        ListIterator<EditionMenu<? extends MenuEditable>> menuIterator = menus.listIterator(startingIndex);
        for (int i = 1; i <= entriesToDisplay && menuIterator.hasNext(); i++) {
            EditionMenu menu = menuIterator.next();
            editor.sendMessage(menu.getDisplayString(data));
        }

        if (displayPageNavigation) {
            editor.sendMessage(mainMessages.getString("menu.entry.previous"));
            editor.sendMessage(mainMessages.getString("menu.entry.next"));
        }
        editor.sendMessage(mainMessages.getString("menu.entry.done"));
    }

    @Override
    public void input(final Player player, final EDITABLE data, final String message, final MenuNavigationResult navigationResult) {
        try {
            //TODO fix paging
            int choice = Integer.parseInt(message);
            // Choice 0 ? Do nothing !
            if(0 < choice && choice < menus.size()+1) {
                navigationResult.setMenu(menus.get(choice));
            }
            else if(choice == menus.size()+1) {
                if(getParent() == null) {
                    if(complete(player, data)) {
                        navigationResult.setMenu(getParent());
                    }
                }
                else {
                    navigationResult.setMenu(getParent());
                }
            }
            else if(choice != 0) {
                throw new NumberFormatException();
            }
        }
        catch(NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Expecting a number between 0-"+(menus.size()+1)+" but got : "+message);
        }
    }

    /**
     * Action to take when an edition is about to end
     */
    public boolean complete(final Player player, final EDITABLE data) {
        return true;
    }
}