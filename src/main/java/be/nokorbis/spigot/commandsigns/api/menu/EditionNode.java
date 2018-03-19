package be.nokorbis.spigot.commandsigns.api.menu;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditionNode extends EditionMenu
{
    protected final List<EditionMenu> menus;

    public EditionNode(String name, EditionMenu parent)
    {
        super(name, parent);
        this.menus = new ArrayList<>();
    }

    public EditionNode(String name)
    {
        this(name, null);
    }

    public void addMenu(EditionMenu menu)
    {
        menus.add(menu);
    }

    @Override
    public void display(EditingConfiguration config)
    {
        Player editor = config.getEditor();

        displayBreadcrumb(editor, config.getCurrentMenu());

        editor.sendMessage(mainMessages.getString("menu.entry.refresh"));

        boolean displayPageNavigation = false;
        int entriesToDisplay = 6;
        int page = config.getPage();
        int startingIndex = 0;
        if (menus.size() > 8)
        {
            displayPageNavigation = true;
            startingIndex = (page-1) * entriesToDisplay;
        }
        else
        {
            entriesToDisplay = menus.size();
        }

        ListIterator<EditionMenu> menuIterator = menus.listIterator(startingIndex);
        for (int i = 1; i <= entriesToDisplay && menuIterator.hasNext(); i++)
        {
            EditionMenu menu = menuIterator.next();
            editor.sendMessage(menu.getDisplayString(config.getEditingData()));
        }

        if (displayPageNavigation)
        {
            editor.sendMessage(mainMessages.getString("menu.entry.previous"));
            editor.sendMessage(mainMessages.getString("menu.entry.next"));
        }
        editor.sendMessage(mainMessages.getString("menu.entry.done"));
    }

    @Override
    public void input(EditingConfiguration config, String message) {
        try
        {
            int choice = Integer.parseInt(message);
            // Choice 0 ? Do nothing !
            if(0 < choice && choice < menus.size()+1)
            {
                config.setCurrentMenu(menus.get(choice));
            }
            else if(choice == menus.size()+1)
            {
                if(getParent() == null)
                {
                    if(complete(config))
                    {
                        config.setCurrentMenu(getParent());
                    }
                }
                else
                {
                    config.setCurrentMenu(getParent());
                }
            }
            else if(choice != 0)
            {
                throw new NumberFormatException();
            }
        }
        catch(NumberFormatException e)
        {
            config.getEditor().sendMessage(ChatColor.RED+"Expecting a number between 0-"+(menus.size()+1)+" but got : "+message);
        }
    }

    /**
     * Action to take when an edition is about to end
     * @param config
     */
    public boolean complete(EditingConfiguration config) {
        return true;
    }
}