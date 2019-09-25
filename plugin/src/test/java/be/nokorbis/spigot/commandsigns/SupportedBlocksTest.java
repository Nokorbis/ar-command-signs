package be.nokorbis.spigot.commandsigns;

import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import org.bukkit.Material;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Collection;

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
        Assert.assertFalse("Not enough plates!", materials.isEmpty());
        /*
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
         */
        final int maxSupportedPlates = 9;
        if (materials.size() > maxSupportedPlates) {
            System.out.println(materials);
        }
        Assert.assertTrue("/!\\Too many pressure plates!", materials.size() == maxSupportedPlates);
    }

    @Test
    void checkSupportedButtons() {
        Collection<Material> materials = CommandBlockValidator.BUTTONS_MATERIAL;
        Assert.assertFalse("Not enough buttons!", materials.isEmpty());
        /*Material[] buttons = {
			Material.ACACIA_BUTTON,
			Material.BIRCH_BUTTON,
			Material.DARK_OAK_BUTTON,
			Material.JUNGLE_BUTTON,
			Material.OAK_BUTTON,
			Material.SPRUCE_BUTTON,
			Material.STONE_BUTTON,
			--Material.LEGACY_STONE_BUTTON
			--Material.LEGACY_WOOD_BUTTON
		};*/
        final int maxSupportedButtons = 9;
        if (materials.size() > maxSupportedButtons) {
            System.err.println(materials);
        }
        Assert.assertTrue("/!\\Too many buttons!",materials.size() == maxSupportedButtons);
    }

    @Test
    void checkSupportedSigns() {
        Collection<Material> materials = CommandBlockValidator.POST_SIGNS_MATERIAL;
        Assert.assertFalse("Not enough post signs", materials.isEmpty());
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
        final int maxSupportedPostSigns = 7;
        if (materials.size() > maxSupportedPostSigns) {
            System.err.println(materials);
        }
        Assert.assertTrue("/!\\Too many post signs!",materials.size() <= maxSupportedPostSigns);

        materials = CommandBlockValidator.WALL_SIGNS_MATERIAL;
        Assert.assertFalse("Not enough wall signs", materials.isEmpty());
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
        final int maxSupportedWallSigns = 7;
        if (materials.size() > maxSupportedWallSigns) {
            System.err.println(materials);
        }
        Assert.assertTrue("/!\\Too many wall signs!",materials.size() <= maxSupportedWallSigns);
    }
}
