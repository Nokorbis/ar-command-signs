package be.nokorbis.spigot.commandsigns.data.json;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.data.CommandBlockConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import com.google.gson.Gson;
import org.bukkit.Location;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;


public class JsonCommandBlockConfigurationDataPersister extends JsonCommandBlockPersister implements CommandBlockConfigurationDataPersister {

	private static final String DATA_FOLDER_NAME = "configurations";

	public JsonCommandBlockConfigurationDataPersister(final File pluginFolder) {
		super(pluginFolder, DATA_FOLDER_NAME);
		registerPersister(Location.class, new JsonLocationPersister());
	}

	@Override
	public void setAddons(Set<Addon> addons) {
		for (Addon addon : addons) {
			registerPersister(addon.getConfigurationDataClass(), addon.getConfigurationDataSerializer());
		}
		registerPersister(CommandBlock.class, new CommandBlockGsonSerializer(addons));
	}

	@Override
	public boolean saveConfiguration(CommandBlock commandBlock) {
		final File configFile = new File(dataFolder, commandBlock.getId() + EXTENSION);
		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
			}
			catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try (FileOutputStream fos = new FileOutputStream(configFile);
			 OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
			Gson gson = getGson();
			String json = gson.toJson(commandBlock);
			writer.write(json);
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public CommandBlock load(final long id) {
		final File configFile = new File(dataFolder, id + EXTENSION);
		if (!configFile.exists()) {
			return null;
		}
		try (InputStream is = new FileInputStream(configFile);
			 InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
			Gson gson = getGson();
			return gson.fromJson(reader, CommandBlock.class);
		}
		catch (IOException e) {
			CommandSignsPlugin.getPlugin().getLogger().severe("Was not able to read a file while loading a command block : " + configFile.getName());
			CommandSignsPlugin.getPlugin().getLogger().severe(e.getMessage());
		}
		return null;
	}


	@Override
	public boolean delete(final long id) {
		final File configFile = new File(dataFolder, id + EXTENSION);
		if (!configFile.exists()) {
			return false;
		}

		return configFile.delete();
	}
}
