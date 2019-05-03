package be.nokorbis.spigot.commandsigns.controller.positionchecker;


import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import org.bukkit.Material;


public final class PositionCheckerFactory {

	//keep them cached to avoid too many instantiation
	private static final CommandBlockPositionChecker WALL_SIGN_CHECKER  = new WallSignPositionChecker();
	private static final CommandBlockPositionChecker POST_SIGN_CHECKER  = new PostSignPositionChecker();
	private static final CommandBlockPositionChecker BUTTON_CHECKER     = new ButtonPositionChecker();
	private static final CommandBlockPositionChecker PLATE_CHECKER      = new PlatePositionChecker();

	private static final CommandBlockPositionChecker DEFAULT_CHECKER = new DefaultPositionChecker();

	public static CommandBlockPositionChecker createChecker(Material material) {

		if (CommandBlockValidator.isWallSign(material)) { return WALL_SIGN_CHECKER; }
		if (CommandBlockValidator.isPostSign(material)) { return POST_SIGN_CHECKER; }
		if (CommandBlockValidator.isButton(material))   { return BUTTON_CHECKER; }
		if (CommandBlockValidator.isPlate(material))    { return PLATE_CHECKER; }

		return DEFAULT_CHECKER;
	}

	private PositionCheckerFactory() {
	}

}

