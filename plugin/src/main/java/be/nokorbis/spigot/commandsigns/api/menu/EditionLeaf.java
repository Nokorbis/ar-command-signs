package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.ChatColor;

public abstract class EditionLeaf extends EditionMenu
{
    public EditionLeaf(String name, EditionMenu parent)
    {
        super(name, parent);
    }

    @Override
    public final String getDisplayString(CommandBlock data)
    {
        return super.getDisplayString(data)+" - ["+ChatColor.GOLD+getDataString(data) + "]";
    }

    public abstract String getDataString(CommandBlock data);
}
