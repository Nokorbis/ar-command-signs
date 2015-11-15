package net.avatar.realms.spigot.commandsign.utils;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CommandSignUtils {

	public static boolean isPlate(Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.WOOD_PLATE) 
				|| t.equals(Material.STONE_PLATE) 
				|| t.equals(Material.IRON_PLATE) 
				|| t.equals(Material.GOLD_PLATE)) {
			return true;
		}

		return false;
	}

	public static boolean isButton (Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.WOOD_BUTTON) || t.equals(Material.STONE_BUTTON)) {
			return true;
		}

		return false;
	}

	public static boolean isSign (Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.SIGN_POST) || t.equals(Material.WALL_SIGN)) {
			return true;
		}

		return false;
	}

	public static boolean isValidBlock (Block block) {
		return isPlate(block) || isButton(block) || isSign(block);
	}

	public static List<Location> getLocationsAroundPoint(Location location, int radius) {
		List<Location> locations = new LinkedList<Location>();

		int xorg = location.getBlockX();
		int yorg = location.getBlockY();
		int zorg = location.getBlockZ();

		for (int x = xorg - radius; x <= (xorg + radius); x++) {
			for (int y = yorg - radius; y <= (yorg + radius); y++) {
				for (int z = zorg - radius; z <= (zorg + radius); z++) {
					Block block = location.getWorld().getBlockAt(x, y, z);
					if (block.getLocation().distance(location) <= radius) {
						locations.add(block.getLocation());
					}
				}
			}
		}
		return locations;
	}
}
