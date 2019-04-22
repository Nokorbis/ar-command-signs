package be.nokorbis.spigot.commandsigns.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.List;
import java.util.Set;


public interface CommandBlockConfigurationDataPersister {

	void setAddons(Set<Addon> addons);

	boolean saveConfiguration(CommandBlock commandBlock);

	CommandBlock load(long id);

	boolean delete(long id);

	List<CommandBlock> loadAllConfigurations();

}
