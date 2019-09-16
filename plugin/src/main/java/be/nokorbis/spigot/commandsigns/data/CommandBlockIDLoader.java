package be.nokorbis.spigot.commandsigns.data;

import org.bukkit.Location;

import java.util.Map;

public interface CommandBlockIDLoader {

    long getLastID();

    Map<Location, Long> loadAllIdsPerLocations();
}
