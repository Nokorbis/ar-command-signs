package be.nokorbis.spigot.menu;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditingConfiguration<T> {

    private Player editor;
    private T editingData;
    private EditionMenu<T> currentMenu;
    private boolean creating;

    /**
     * @param player
     *        The player that is editing the configuration
     * @param editingData
     *        The data whose configuration is being edited
     * @param creating
     *        Is the player creating a new command block or editing an existing one ?
     */
    public EditingConfiguration(Player player, T editingData, boolean creating) {
        this.editor = player;
        this.editingData = editingData;
        this.creating = creating;
    }

    public void display() {
        if (this.currentMenu != null) {
            this.currentMenu.display(this);
        } else {
            if (this.creating) {
                this.editor.sendMessage(ChatColor.GREEN + "Creation complete!");
            }
            else {
                this.editor.sendMessage(ChatColor.GREEN + "Edition complete!");
            }
        }
    }

    public void input(String message) {
        if ((message != null) && (this.currentMenu != null)) {
            message = message.trim();
            if (!message.equals("")) {
                this.currentMenu.input(this, message);
            }
        }
    }

    public void setEditingData(T data) {
        this.editingData = data;
    }

    public Player getEditor() {
        return this.editor;
    }

    public T getEditingData() {
        return this.editingData;
    }

    public void setCurrentMenu(EditionMenu<T> newMenu) {
        this.currentMenu = newMenu;
    }

    public boolean isCreating() {
        return this.creating;
    }

    public EditionMenu<T> getCurrentMenu() {
        return this.currentMenu;
    }
}