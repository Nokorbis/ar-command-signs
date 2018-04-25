package be.nokorbis.spigot.commandsigns.data;

import org.bukkit.Location;

import java.util.Map;

public interface CommandBlockIDLoader
{
    public Map<Location, Long> loadAllIdsForLocations();
}
