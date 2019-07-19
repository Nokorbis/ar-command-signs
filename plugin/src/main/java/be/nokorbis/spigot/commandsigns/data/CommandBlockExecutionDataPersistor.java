package be.nokorbis.spigot.commandsigns.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.Set;


public interface CommandBlockExecutionDataPersistor {
	void setAddons(Set<Addon> addons);

	void loadExecutionData(CommandBlock commandBlock);
	void saveExecutionData(CommandBlock commandBlock);
	boolean deleteExecutionData(CommandBlock commandBlock);
}
