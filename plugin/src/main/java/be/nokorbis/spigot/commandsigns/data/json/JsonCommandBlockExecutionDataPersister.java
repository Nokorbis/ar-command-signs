package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.data.CommandBlockExecutionDataPersistor;

import java.io.File;


public class JsonCommandBlockExecutionDataPersister extends JsonCommandBlockPersister implements CommandBlockExecutionDataPersistor {

	private static final String DATA_FOLDER_NAME = "executions";

	public JsonCommandBlockExecutionDataPersister(File pluginFolder) {
		super(pluginFolder, DATA_FOLDER_NAME);
	}
}
