package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.data.CommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.data.Pair;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;


public class JsonCommandBlockIDLoader extends JsonCommandBlockPersister implements CommandBlockIDLoader {

	private static final Type MAPPING_TYPE = new TypeToken<Pair<Location, Long>>(){}.getType();
	private static final String DATA_FOLDER_NAME = "configurations";

	private final Logger logger;

	public JsonCommandBlockIDLoader(CommandSignsPlugin plugin) {
		super(plugin.getDataFolder(), DATA_FOLDER_NAME);

		logger = plugin.getLogger();

		registerPersister(Location.class, new JsonLocationPersister());
		registerPersister(MAPPING_TYPE, new JsonCommandBlockLocationExtractor());
	}

	public long getLastID() {
		long maxID = 0L;
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
			return maxID;
		}

		File[] files = dataFolder.listFiles(filter);
		if (files != null && files.length > 0) {
			for (File file : files) {
				String fileName = file.getName();
				String name = fileName.substring(0, fileName.indexOf('.'));
				long id = Long.parseLong(name);
				if (id > maxID) {
					maxID = id;
				}
			}
		}

		return maxID;
	}

	@Override
	public Map<Location, Long> loadAllIdsPerLocations() {
		Map<Location, Long> idsPerLocations = new HashMap<>();

		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
			return idsPerLocations;
		}

		File[] files = dataFolder.listFiles(filter);

		if (files != null && files.length > 0) {
			Gson gson = getGson();
			for (File file : files) {
				try (InputStream fis = new FileInputStream(file);
					 InputStreamReader reader = new InputStreamReader(fis, UTF_8)){

					Pair<Location, Long> pair = gson.fromJson(reader, MAPPING_TYPE);
					if (pair == null) {
						logger.warning(String.format("Command block location could not be found in: %s (might be from another not-yet-loaded world)", file.getPath()));
					}
					else if (!CommandBlockValidator.isValidBlock(pair.getFirst().getBlock())) {
						logger.warning(String.format("Invalid command block location in: %s", file.getPath()));
					}
					else {
						idsPerLocations.put(pair.getFirst(), pair.getSecond());
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return idsPerLocations;
	}

}
