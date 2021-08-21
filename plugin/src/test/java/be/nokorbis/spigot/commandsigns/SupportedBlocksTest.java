package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * This isn't really accurate.
 * But since we support different api versions, we can't rely on strict enum values for the material validator
 * Thus I check the count of validated blocks in case of another api change
 * to be warned if some more blocks are suddenly valid
 */
public class SupportedBlocksTest {

    @Test
    void checkSupportedPlates() {
        Collection<Material> materials = CommandBlockValidator.PLATES_MATERIAL;
        assertFalse(materials.isEmpty(), "Not enough plates!");

        /*Material[] plates = {
			Material.STONE_PRESSURE_PLATE,
			Material.ACACIA_PRESSURE_PLATE,
			Material.BIRCH_PRESSURE_PLATE,
			Material.DARK_OAK_PRESSURE_PLATE,
			Material.JUNGLE_PRESSURE_PLATE,
			Material.OAK_PRESSURE_PLATE,
			Material.SPRUCE_PRESSURE_PLATE,
			Material.HEAVY_WEIGHTED_PRESSURE_PLATE,
			Material.LIGHT_WEIGHTED_PRESSURE_PLATE,
            Material.CRIMSON_PRESSURE_PLATE,
            Material.WARPED_PRESSURE_PLATE,
            Material.POLISHED_BLACKSTONE_PRESSURE_PLATE,
		};*/

        final int maxSupportedPlates = 12;
        if (materials.size() > maxSupportedPlates) {
            System.out.println(materials);
        }
        assertEquals(maxSupportedPlates, materials.size(), "/!\\Too many pressure plates!");
    }

    @Test
    void checkSupportedButtons() {
        Collection<Material> materials = CommandBlockValidator.BUTTONS_MATERIAL;
        assertFalse(materials.isEmpty(), "Not enough buttons!");
        /*Material[] buttons = {
			Material.ACACIA_BUTTON,
			Material.BIRCH_BUTTON,
			Material.DARK_OAK_BUTTON,
			Material.JUNGLE_BUTTON,
			Material.OAK_BUTTON,
			Material.SPRUCE_BUTTON,
			Material.STONE_BUTTON,
            Material.CRIMSON_BUTTON,
            Material.WARPED_BUTTON,
            Material.POLISHED_BLACKSTONE_BUTTON,
			--Material.LEGACY_STONE_BUTTON,
			--Material.LEGACY_WOOD_BUTTON,
		};*/
        final int maxSupportedButtons = 12;
        if (materials.size() > maxSupportedButtons) {
            System.err.println(materials);
        }
        assertEquals(maxSupportedButtons, materials.size(), "/!\\Too many buttons!");
    }

    @Test
    void checkSupportedSigns() {
        Collection<Material> materials = CommandBlockValidator.POST_SIGNS_MATERIAL;
        assertFalse(materials.isEmpty(), "Not enough post signs");
        /*
        Material[] signs = {
			Material.OAK_SIGN,
			Material.ACACIA_SIGN,
			Material.BIRCH_SIGN,
			Material.JUNGLE_SIGN,
			Material.SPRUCE_SIGN,
			Material.DARK_OAK_SIGN,
			--Material.LEGACY_SIGN
		};
         */
        final int maxSupportedPostSigns = 9;
        if (materials.size() > maxSupportedPostSigns) {
            System.err.println(materials);
        }
        assertTrue(materials.size() <= maxSupportedPostSigns, "/!\\Too many post signs!");

        materials = CommandBlockValidator.WALL_SIGNS_MATERIAL;
        assertFalse(materials.isEmpty(), "Not enough wall signs");
        /*
        signs = new Material[]{
            Material.OAK_WALL_SIGN,
            Material.ACACIA_WALL_SIGN,
            Material.BIRCH_WALL_SIGN,
            Material.JUNGLE_WALL_SIGN,
            Material.SPRUCE_WALL_SIGN,
            Material.DARK_OAK_WALL_SIGN,
            --Material.LEGACY_WALL_SIGN
		};*/
        final int maxSupportedWallSigns = 9;
        if (materials.size() > maxSupportedWallSigns) {
            System.err.println(materials);
        }
        assertTrue(materials.size() <= maxSupportedWallSigns, "/!\\Too many wall signs!");
    }
}
