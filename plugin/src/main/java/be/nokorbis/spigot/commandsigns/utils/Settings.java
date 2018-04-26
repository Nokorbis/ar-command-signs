package be.nokorbis.spigot.commandsigns.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Settings {

	private static char OP_CHAR;
	public static char OP_CHAR() { return OP_CHAR; }

	private static char SERVER_CHAR;
	public static char SERVER_CHAR() { return SERVER_CHAR; }

	private static char DELAY_CHAR;
	public static char DELAY_CHAR() { return DELAY_CHAR; }

	private static long CACHE_TIME_TO_IDLE; //minutes
	public static long CACHE_TIME_TO_IDLE() { return CACHE_TIME_TO_IDLE; }

	private static long CACHE_MAX_SIZE;
	public static long CACHE_MAX_SIZE() { return CACHE_MAX_SIZE; }

	private Settings(){}

	public static void loadSettings(JavaPlugin plugin)
	{
		plugin.saveDefaultConfig();
		FileConfiguration config = plugin.getConfig();

		OP_CHAR 	= config.getString("prefix_char.op", "^").charAt(0);
		SERVER_CHAR = config.getString("prefix_char.server", "#").charAt(0);
		DELAY_CHAR 	= config.getString("prefix_char.delay", "`").charAt(0);

		CACHE_TIME_TO_IDLE = config.getLong("cache.time_to_idle", 30L);
		CACHE_MAX_SIZE 		= config.getLong("cache.max_size", 100L);
	}



}
