package be.nokorbis.spigot.commandsigns.menu;

import java.util.Map;
import java.util.TreeMap;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;

/**
 *
 */
public abstract class EditionMenu implements IEditionMenu<CommandBlock> {

	private String name;

	private EditionMenu parent;

	protected Map<Integer, EditionMenu> subMenus;

	public EditionMenu(EditionMenu parent, String name) {
		this.parent = parent;
		this.name = name.trim();
		this.subMenus = new TreeMap<Integer, EditionMenu>(); // Use treemap to make sure the display is done properly
	}

	/**
	 * Get the name of this menu
	 *
	 * @return
	 * 		A String with the name
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Get the parent menu of this menu
	 *
	 * @return <code>null</code> if this is the main menu
	 *         <code>An EditionMenu</<code> otherwhise
	 */
	public final EditionMenu getParent() {
		return this.parent;
	}

	/**
	 * Get the name that must be shown in the menu formatted with the command block data
	 *
	 * @param cmd
	 *        The command block data
	 * @return
	 * 		A String containing the format name to show
	 */
	public String formatName(CommandBlock cmd) {
		return this.name;
	}
}
