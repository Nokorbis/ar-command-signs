package net.avatar.realms.spigot.commandsign.utils;

import net.avatar.realms.spigot.commandsign.CommandSign;

import java.io.*;
import java.util.Properties;

public abstract class Messages {

	private static final String CHARSET = "UTF-8";
	private static final String FILENAME = "messages.properties";

	private static Properties defaultMessages = null;
	private static Properties messages = null;

	public static String get(String key) {
		if (defaultMessages == null && messages == null) {
			loadMessages();
		}
		return messages.getProperty(key);
	}

	private static void loadMessages() {
		defaultMessages = new Properties();

		try {
			InputStream in = Messages.class.getClassLoader().getResourceAsStream(FILENAME);
			InputStreamReader reader = new InputStreamReader(in, CHARSET);
			defaultMessages.load(reader);
			reader.close();
		}
		catch (IOException e) {
			// Should never happens
		}
		messages = new Properties(defaultMessages);
		try {
			File folder = CommandSign.getPlugin().getDataFolder();
			File custom = new File(folder, FILENAME);
			if (custom.exists()) {
				InputStream in = new FileInputStream(custom);
				InputStreamReader reader = new InputStreamReader(in, CHARSET);
				messages.load(reader);
				reader.close();
			}
		}
		catch (IOException e) {
		}
	}
}
