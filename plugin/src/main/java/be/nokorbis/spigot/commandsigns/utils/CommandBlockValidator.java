package be.nokorbis.spigot.commandsigns.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Collection;


public final class CommandBlockValidator {
	private static final Collection<Material> PLATES_MATERIAL;
	private static final Collection<Material> SIGNS_MATERIAL;
	private static final Collection<Material> BUTTONS_MATERIAL;


	public static boolean isValidBlock (Block block) {
		return isPlate(block) || isButton(block) || isSign(block) || isTrappedChest(block) || isTripwire(block) || isLever(block);
	}

	public static boolean isPlate(Block block) {
		if (block == null) {
			return false;
		}

		return isPlate(block.getType());
	}

	public static boolean isPlate(Material material) {
		return PLATES_MATERIAL.contains(material);
	}

	public static boolean isButton (Block block) {
		if (block == null) {
			return false;
		}

		return isButton(block.getType());
	}

	public static boolean isButton(Material material) {
		return BUTTONS_MATERIAL.contains(material);
	}

	public static boolean isSign (Block block) {
		if (block == null) {
			return false;
		}

		return isSign(block.getType());
	}

	public static boolean isSign(Material material) {
		return SIGNS_MATERIAL.contains(material);
	}

	public static boolean isTripwire(Block block) {
		if (block == null) {
			return false;
		}

		return isTripwire(block.getType());
	}

	public static boolean isTripwire(Material material) {
		return Material.TRIPWIRE_HOOK == material;
	}

	public static boolean isTrappedChest(Block block) {
		if (block == null) {
			return false;
		}

		return isTrappedChest(block.getType());
	}

	public static boolean isTrappedChest(Material material) {
		return Material.TRAPPED_CHEST == material;
	}

	public static boolean isLever(Block block) {
		if (block == null) {
			return false;
		}

		return isLever(block.getType());
	}

	public static boolean isLever(Material material) {
		return Material.LEVER == material;
	}


	private CommandBlockValidator() {
	}

	static {
		Material[] plates = {
			Material.STONE_PRESSURE_PLATE,
			Material.ACACIA_PRESSURE_PLATE,
			Material.BIRCH_PRESSURE_PLATE,
			Material.DARK_OAK_PRESSURE_PLATE,
			Material.JUNGLE_PRESSURE_PLATE,
			Material.OAK_PRESSURE_PLATE,
			Material.SPRUCE_PRESSURE_PLATE,
			Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
			Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
		};
		PLATES_MATERIAL = Arrays.asList(plates);

		Material[] signs = {
			Material.SIGN,
			Material.WALL_SIGN,
		};
		SIGNS_MATERIAL = Arrays.asList(signs);

		Material[] buttons = {
			Material.ACACIA_BUTTON,
			Material.BIRCH_BUTTON,
			Material.DARK_OAK_BUTTON,
			Material.JUNGLE_BUTTON,
			Material.OAK_BUTTON,
			Material.SPRUCE_BUTTON,
			Material.STONE_BUTTON,
		};
		BUTTONS_MATERIAL = Arrays.asList(buttons);
	}
}
