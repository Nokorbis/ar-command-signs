package net.avatar.realms.spigot.commandsign.model;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

/**
 * Not used yet.
 *
 */
public class EditionMenu {

	private String name;

	private EditionMenu parent;

	private IDisplayer displayer;
	private IInputHandler inputHandler;

	private Map<Integer, EditionMenu> subMenus;
	private int lastIndex = 0;

	public EditionMenu(EditionMenu parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void display (Player player) {
		this.displayer.display(player);
	}

	public void input (Player player, String message) {
		this.inputHandler.input(player, message);
	}

	public EditionMenu getParent() {
		return this.parent;
	}

	public void addSubMenu(EditionMenu menu) {
		if (menu != null) {
			if (this.subMenus == null) {
				this.subMenus = new HashMap<Integer, EditionMenu>();
			}
			boolean done = false;
			for (int i = this.lastIndex + 1; !done ; i++) {
				if (!this.subMenus.containsKey(i)) {
					this.subMenus.put(i, menu);
					this.lastIndex = i;
					done = true;
				}
			}
		}
	}

	public void addSubMenu(EditionMenu menu, Integer index) {
		if (menu != null) {
			if (this.subMenus == null) {
				this.subMenus = new HashMap<Integer, EditionMenu>();
			}
			boolean done = false;
			for (int i = index + 1; !done ; i++) {
				if (!this.subMenus.containsKey(i)) {
					this.subMenus.put(i, menu);
					done = true;
				}
			}
		}
	}

}
