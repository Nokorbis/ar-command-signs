package net.bendercraft.spigot.commandsigns.data;

import net.bendercraft.spigot.commandsigns.model.CommandBlock;

import java.util.Collection;

/**
 * Created by Nokorbis on 22/01/2016.
 */
public interface ICommandBlockSaver {

    boolean save(CommandBlock cmdB);

    CommandBlock load(long id);

    boolean saveAll(Collection<CommandBlock> cmdBlocks);

    Collection<CommandBlock> loadAll();

    boolean delete (long id);

}
