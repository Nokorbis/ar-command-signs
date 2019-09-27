package be.nokorbis.spigot.commandsigns.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public final class CommandBlockValidator {
	public static final Collection<Material> PLATES_MATERIAL;
	public static final Collection<Material> POST_SIGNS_MATERIAL;
	public static final Collection<Material> WALL_SIGNS_MATERIAL;
	public static final Collection<Material> BUTTONS_MATERIAL;


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
		return isWallSign(material) || isPostSign(material);
	}

	public static boolean isWallSign(Material material) {
		return WALL_SIGNS_MATERIAL.contains(material);
	}

	public static boolean isPostSign(Material material) {
		return POST_SIGNS_MATERIAL.contains(material);
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
		/*
		 * This method is used to load supported materials.
		 * There is a change in the API between API 1.13 and 1.14 for signs names.
		 * To avoid maintaining 2 versions of the code, we check the name to see if the matches.
		 * But this might break if new blocks are added.
		 */
		ArrayList<Material> plates = new ArrayList<>();
		ArrayList<Material> postSigns = new ArrayList<>(6);
		ArrayList<Material> wallSigns = new ArrayList<>(6);
		ArrayList<Material> buttons = new ArrayList<>(7);

		for (Material material : Material.values()) {
			String name = material.name();
			if (name.endsWith("_PRESSURE_PLATE")) {
				plates.add(material);
			}
			else if (name.endsWith("SIGN")) {
				if (name.contains("WALL_")) {
					wallSigns.add(material);
				}
				else {
					postSigns.add(material);
				}
			}
			else if (name.endsWith("_BUTTON")) {
				buttons.add(material);
			}
		}

		//they are re-instantiated to avoid having empty extra capacity
		PLATES_MATERIAL = Collections.unmodifiableList(new ArrayList<>(plates));
		POST_SIGNS_MATERIAL = Collections.unmodifiableList(new ArrayList<>(postSigns));
		WALL_SIGNS_MATERIAL = Collections.unmodifiableList(new ArrayList<>(wallSigns));
		BUTTONS_MATERIAL = Collections.unmodifiableList(new ArrayList<>(buttons));

	}
}
