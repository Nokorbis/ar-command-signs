package net.bendercraft.spigot.commandsigns.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Settings {

	public final static char opChar = '^';

	public final static char serverChar = '#';

	public final static char delayChar = '`';

	private static boolean savePlayerCooldowns = false;


	public static void loadSettings(JavaPlugin plugin) {
		plugin.saveDefaultConfig();
		FileConfiguration config = plugin.getConfig();
		savePlayerCooldowns = config.getBoolean("save.player_cooldowns", true);
	}


	public static boolean savePlayerCooldowns() {
		return savePlayerCooldowns;
	}


}
