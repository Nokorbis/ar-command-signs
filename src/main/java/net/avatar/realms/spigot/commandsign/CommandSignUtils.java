package net.avatar.realms.spigot.commandsign;

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
}
