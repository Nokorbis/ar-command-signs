package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditingConfiguration<T>
{
    private final Player editor;
    private final T dataBeingEdited;
    private EditionMenu currentMenu;
    private boolean creating;
    private int page;

    /**
     * @param player
     *        The player that is editing the configuration
     * @param dataBeingEdited
     *        The data whose configuration is being edited
     * @param creating
     *        Is the player creating a new command block or editing an existing one ?
     */
    public EditingConfiguration(Player player, T dataBeingEdited, boolean creating)
    {
        this.editor = player;
        this.dataBeingEdited = dataBeingEdited;
        this.creating = creating;
    }

    public void display()
    {
        if (this.currentMenu != null)
        {
            this.currentMenu.display(this);
        }
        else
        {
            if (this.creating)
            {
                this.editor.sendMessage(ChatColor.GREEN + "Creation complete!");
            }
            else
            {
                this.editor.sendMessage(ChatColor.GREEN + "Edition complete!");
            }
        }
    }

    public void input(String message)
    {
        if ((message != null) && (this.currentMenu != null))
        {
            message = message.trim();
            if (!message.equals(""))
            {
                this.currentMenu.input(this, message);
            }
        }
    }

    public Player getEditor()
    {
        return this.editor;
    }

    public T getDataBeingEdited()
    {
        return this.dataBeingEdited;
    }

    public void setCurrentMenu(EditionMenu newMenu)
    {
        this.currentMenu = newMenu;
    }

    public boolean isCreating()
    {
        return this.creating;
    }

    public EditionMenu getCurrentMenu()
    {
        return this.currentMenu;
    }

    public int getPage()
    {
        return this.page;
    }

    public void setPage(int page)
    {
        if (page < 1)
        {
            this.page = 1;
        }
        else
        {
            this.page = page;
        }
    }
}