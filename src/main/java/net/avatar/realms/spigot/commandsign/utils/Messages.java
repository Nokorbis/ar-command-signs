package net.avatar.realms.spigot.commandsign.utils;

import net.avatar.realms.spigot.commandsign.CommandSign;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Messages {

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
			defaultMessages.load(in);
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
				messages.load(in);
			}
		}
		catch (IOException e) {
		}
	}
}
