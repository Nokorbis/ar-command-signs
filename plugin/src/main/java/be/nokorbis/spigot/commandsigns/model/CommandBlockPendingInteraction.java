package be.nokorbis.spigot.commandsigns.model;

import org.bukkit.entity.Player;


public class CommandBlockPendingInteraction {
	public enum Type {
		INFO,
		COPY,
		DELETE
	}

	public Type   type;
	public Player player;
	public CommandBlock commandBlock;

	public String debug() {
		return player.getName() + "[" + type.name() + ":" + (commandBlock == null ? null : commandBlock.getId()) + "]";
	}
}
