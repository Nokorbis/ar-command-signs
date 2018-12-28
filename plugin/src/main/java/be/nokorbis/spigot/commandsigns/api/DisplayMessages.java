package be.nokorbis.spigot.commandsigns.api;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public final class DisplayMessages {

	private static final Map<String, DisplayMessages> loadedMessages = new HashMap<>();

	public static DisplayMessages getDisplayMessages(final String filename) {
		return loadedMessages.computeIfAbsent(filename, DisplayMessages::loadDisplayMessage);
	}

	private static DisplayMessages loadDisplayMessage(final String filename) {
		final String path = filename + ".lang";
		return new DisplayMessages(path);
	}

	private Properties messages;

	private DisplayMessages(final String path) {
		final Properties defaultMessages = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(path);

		if (in != null) {
			try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)){
				defaultMessages.load(reader);
			}
			catch (IOException ignored) {
			}
		}

		try {
			messages = new Properties(defaultMessages);
			File folder = CommandSignsPlugin.getPlugin().getDataFolder();
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File custom = new File(folder, path);
			if (custom.exists()) {
				in = new FileInputStream(custom);
				try (InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
					messages.load(reader);
				}
			}
			else {
				File parentFolder = custom.getParentFile();
				if (parentFolder.exists() || parentFolder.mkdirs()) {
					if (custom.createNewFile()) {
						try (OutputStream out = new FileOutputStream(custom);
							 OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
							defaultMessages.store(writer, "See https://github.com/Nokorbis/ar-command-signs/wiki/Custom-messages for more information about this file");
						}
					}
				}
			}
		}
		catch (IOException ignored) {
		}
	}

	public String get(final String key) {
		return messages.getProperty(key);
	}
}
