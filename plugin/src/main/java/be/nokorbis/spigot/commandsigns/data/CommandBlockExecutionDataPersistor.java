package be.nokorbis.spigot.commandsigns.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.Set;


public interface CommandBlockExecutionDataPersistor {
	public void setAddons(Set<Addon> addons);

	public void loadExecutionData(CommandBlock commandBlock);
	public void saveExecutionData(CommandBlock commandBlock);
}
