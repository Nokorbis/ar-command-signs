package net.bendercraft.spigot.menu;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditionNode<T> extends EditionMenu<T> {

    // Use treemap to make sure the display is done properly
    private final Map<Integer, EditionMenu<T>> menus = new TreeMap<Integer, EditionMenu<T>>();


    public EditionNode(String name, EditionMenu<T> parent) {
        super(name, parent);
    }

    public EditionNode(String name) {
        super(name);
    }

    public void addMenu(int number, EditionMenu<T> menu) {
        if(menus.containsKey(number)) {
            throw new IllegalArgumentException();
        }
        menus.put(number, menu);
    }

    @Override
    public void display(EditingConfiguration<T> config) {
        Player editor = config.getEditor();
        editor.sendMessage(ChatColor.GRAY+"-------------------");
        StringBuilder sb = new StringBuilder();
        EditionMenu<T> current = config.getCurrentMenu();
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
        for (Entry<Integer, EditionMenu<T>> menu : menus.entrySet()) {
            editor.sendMessage(c + "" + menu.getKey() + ". " + menu.getValue().formatName(config.getEditingData()));
        }
        editor.sendMessage(ChatColor.GREEN + String.valueOf(menus.size()+1)+". Done");
    }

    @Override
    public void input(EditingConfiguration<T> config, String message) {
        try {
            int choice = Integer.parseInt(message);
            // Choice 0 ? Do nothing !
            if(0 < choice && choice < menus.size()+1) {
                config.setCurrentMenu(menus.get(choice));
            } else if(choice == menus.size()+1) {
                if(getParent() == null) {
                    if(complete(config)) {
                        config.setCurrentMenu(getParent());
                    }
                } else {
                    config.setCurrentMenu(getParent());
                }
            } else if(choice != 0) {
                throw new NumberFormatException();
            }
        } catch(NumberFormatException e) {
            config.getEditor().sendMessage(ChatColor.RED+"Expecting a number between 0-"+(menus.size()+1)+" but got : "+message);
        }
    }

    /**
     * Action to take when an edition is about to end
     * @param config
     */
    public boolean complete(EditingConfiguration<T> config) {
        return true;
    }
}