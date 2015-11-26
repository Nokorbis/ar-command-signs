package net.avatar.realms.spigot.commandsign.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import net.avatar.realms.spigot.commandsign.CommandSign;

public class LocationData {

	private String worldUuid;

	private int x;
	private int y;
	private int z;

	public LocationData() {

	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return this.z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public String getWorldUuid() {
		return this.worldUuid;
	}

	public void setWorldUuid(String worlduuid) {
		this.worldUuid = worlduuid;
	}

	public static LocationData transform (Location location) {
		LocationData data = new LocationData();

		data.setWorldUuid(location.getWorld().getUID().toString());

		data.setX(location.getBlockX());
		data.setY(location.getBlockY());
		data.setZ(location.getBlockZ());

		return data;
	}

	public static Location transform (LocationData data) {
		World world = Bukkit.getServer().getWorld(UUID.fromString(data.getWorldUuid()));
		if (world == null) {
			CommandSign.getPlugin().getLogger().severe("World with UUID : " + data.getWorldUuid() + " cannot be found");
			return null;
		}
		Location loc = new Location(world, data.getX(), data.getY(), data.getZ());

		return loc;
	}

}
