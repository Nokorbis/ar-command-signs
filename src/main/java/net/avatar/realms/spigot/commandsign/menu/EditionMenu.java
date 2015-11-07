package net.avatar.realms.spigot.commandsign.menu;

import java.util.Map;
import java.util.TreeMap;

import org.bukkit.ChatColor;

/**
 * Not used yet.
 *
 */
public abstract class EditionMenu implements IEditionMenu {
	
	/**
	 * Default color for chat message that are sent to the players
	 */
	protected static final ChatColor c = ChatColor.AQUA;

	private String name;

	private EditionMenu parent;

	protected Map<Integer, EditionMenu> subMenus;

	public EditionMenu(EditionMenu parent, String name) {
		this.parent = parent;
		this.name = name;
		this.subMenus = new TreeMap<Integer, EditionMenu>(); // Use treemap to make sure the display is done properly
	}

	public final String getName() {
		return this.name;
	}

	public final EditionMenu getParent() {
		return this.parent;
	}
}
