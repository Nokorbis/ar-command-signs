package be.nokorbis.spigot.commandsigns.data;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.Collection;

/**
 * Created by Nokorbis on 22/01/2016.
 */
public interface CommandBlockSaver
{

    boolean save(CommandBlock cmdB);

    CommandBlock load(long id);

    boolean saveAll(Collection<CommandBlock> cmdBlocks);

    Collection<CommandBlock> loadAll();

    boolean delete (long id);

}
