package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.data.CommandBlockExecutionDataPersistor;
import be.nokorbis.spigot.commandsigns.model.AddonExecutionDataObject;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Location;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static java.nio.charset.StandardCharsets.UTF_8;


public class JsonCommandBlockExecutionDataPersister extends JsonCommandBlockPersister implements CommandBlockExecutionDataPersistor {

	private static final String DATA_FOLDER_NAME = "executions";

	public JsonCommandBlockExecutionDataPersister(File pluginFolder) {
		super(pluginFolder, DATA_FOLDER_NAME);
		registerPersister(Location.class, new JsonLocationPersister());
	}

	@Override
	public void setAddons(Set<Addon> addons) {
		for (Addon addon : addons) {
			registerPersister(addon.getExecutionDataClass(), addon.getExecutionDataSerializer());
		}
		registerPersister(AddonExecutionDataObject.class, new JsonExecutionDataObjectSerializer(addons));
	}

	@Override
	public void loadExecutionData(CommandBlock commandBlock) {
		final File configFile = new File(dataFolder, commandBlock.getId() + EXTENSION);
		if (!configFile.exists()) {
			return;
		}
		try (InputStream is = new FileInputStream(configFile);
			 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			Gson gson = getGson();
			AddonExecutionDataObject object = gson.fromJson(reader, AddonExecutionDataObject.class);
			commandBlock.importExecutionData(object);
		}
		catch (IOException e) {
			CommandSignsPlugin.getPlugin().getLogger().severe("Was not able to read a file while loading a command block : " + configFile.getName());
			CommandSignsPlugin.getPlugin().getLogger().severe(e.getMessage());
		}
	}

	@Override
	public void saveExecutionData(CommandBlock commandBlock) {
		File blockDataFile = new File(dataFolder, commandBlock.getId() + EXTENSION);

		try {
			if (!blockDataFile.exists()) {
				blockDataFile.createNewFile();
			}

			try (OutputStream os = new FileOutputStream(blockDataFile);
				 OutputStreamWriter writer = new OutputStreamWriter(os, UTF_8)){
				Gson gson = getGson();

				String json = gson.toJson(commandBlock.exportExecutionData());

				writer.write(json);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}
}
