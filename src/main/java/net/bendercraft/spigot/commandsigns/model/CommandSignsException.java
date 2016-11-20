package net.bendercraft.spigot.commandsigns.model;

public class CommandSignsException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String message;

	public CommandSignsException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
