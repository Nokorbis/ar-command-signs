package be.nokorbis.spigot.commandsigns.api.menu;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditionNode extends EditionMenu
{
    // Use treemap to make sure the display is done properly
    private final Map<Integer, EditionMenu> menus;

    public EditionNode(String name, EditionMenu parent)
    {
        super(name, parent);
        this.menus = new TreeMap<>();
    }

    public EditionNode(String name)
    {
        this(name, null);
    }

    public void addMenu(int number, EditionMenu menu)
    {
        if(menus.containsKey(number))
        {
            throw new IllegalArgumentException();
        }
        menus.put(number, menu);
    }

    @Override
    public void display(EditingConfiguration config) {
        Player editor = config.getEditor();
        editor.sendMessage(ChatColor.GRAY+"-------------------");
        StringBuilder sb = new StringBuilder();
        EditionMenu current = config.getCurrentMenu();
        int i = 0;
        while(current != null || i > 4) {
            sb.insert(0, " > "+current.getName());
            current = current.getParent();
            i++;
        }
        if(i != 0) {
            sb.delete(0, 3);
        }
        if(i == 4) {
            sb.insert(0, " ... > ");
        }
        editor.sendMessage(ChatColor.GRAY+sb.toString());
        editor.sendMessage(ChatColor.LIGHT_PURPLE + "0. Refresh");
        for (Map.Entry<Integer, EditionMenu> menu : menus.entrySet()) {
            editor.sendMessage( menu.getKey() + ". " + menu.getValue().getDisplayString(config.getEditingData()));
        }
        editor.sendMessage(ChatColor.GREEN + String.valueOf(menus.size()+1)+". Done");
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