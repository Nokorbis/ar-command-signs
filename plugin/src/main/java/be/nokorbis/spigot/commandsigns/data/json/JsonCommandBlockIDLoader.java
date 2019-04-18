package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.data.CommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.data.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Location;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;


public class JsonCommandBlockIDLoader extends JsonCommandBlockPersister implements CommandBlockIDLoader {

	private static final Type MAPPING_TYPE = new TypeToken<Pair<Location, Long>>(){}.getType();

	private static final String DATA_FOLDER_NAME = "configurations";

	public JsonCommandBlockIDLoader(File pluginFolder) {
		super(pluginFolder, DATA_FOLDER_NAME);

		registerPersister(Location.class, new JsonLocationPersister());
		registerPersister(MAPPING_TYPE, new JsonCommandBlockLocationExtractor());
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
					idsPerLocations.put(pair.getFirst(), pair.getSecond());
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}


		return idsPerLocations;
	}

}
