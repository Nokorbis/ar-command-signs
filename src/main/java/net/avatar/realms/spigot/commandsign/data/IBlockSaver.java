package net.avatar.realms.spigot.commandsign.data;

import java.util.Collection;

import net.avatar.realms.spigot.commandsign.model.CommandBlock;

public interface IBlockSaver {
	
	public void save (Collection<CommandBlock> commandBlocks);
	
	public Collection<CommandBlock> load();

}
