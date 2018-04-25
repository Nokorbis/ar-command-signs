package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Settings;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class NCSManager
{
    private final Logger logger;

    private final Map<Location, Long> locationsToIds = new HashMap<>();
    private final LoadingCache<Long, CommandBlock> cache;

    public NCSManager(CommandSignsPlugin plugin)
    {
        this.logger = plugin.getLogger();

        this.cache = Caffeine.newBuilder()
                .maximumSize(Settings.CACHE_MAX_SIZE())
                .expireAfterAccess(Settings.CACHE_TIME_TO_LIVE(), TimeUnit.MINUTES)
                .removalListener(this::onCacheRemove)
                .build(key -> new CommandBlock());
    }

    public CommandBlock getCommandBlock(Long id)
    {
        if (id == null)
        {
            return null;
        }
        return this.cache.get(id);
    }

    public CommandBlock getCommandBlock(Location location)
    {
        if (location == null)
        {
            return null;
        }
        Long id = this.locationsToIds.get(location);
        return this.getCommandBlock(id);
    }

    public void onCacheRemove(Long id, CommandBlock cmdBlock, RemovalCause cause)
    {
        this.logger.info(String.format("The command block : %s (%n) was removed from the cache because : %s.", cmdBlock.getName(), id, cause.name()));
    }
}
