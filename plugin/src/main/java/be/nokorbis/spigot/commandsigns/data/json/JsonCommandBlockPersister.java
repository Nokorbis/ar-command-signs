package be.nokorbis.spigot.commandsigns.data.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;


public class JsonCommandBlockPersister {

	protected static final String EXTENSION = ".json";

	private static final String BASENAME       = "CommandBlocks";

	protected File            dataFolder;
	private GsonBuilder builder;
	protected DataFilesFilter filter;

	protected JsonCommandBlockPersister(final File pluginFolder, final String folderName) {
		builder = new GsonBuilder();
		builder.setPrettyPrinting();

		File baseFolder = new File(pluginFolder, BASENAME);
		dataFolder = new File(baseFolder, folderName);

		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}

		filter = new DataFilesFilter();
	}

	protected Gson getGson() {
		return builder.create();
	}

	public void registerPersister(Class<?> klass, Object persister) {
		if (klass != null && persister != null) {
			builder.registerTypeAdapter(klass, persister);
		}
	}

	public void registerPersister(Type type, Object persister) {
		builder.registerTypeAdapter(type, persister);
	}


	protected static class DataFilesFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(EXTENSION);
		}
	}
}
