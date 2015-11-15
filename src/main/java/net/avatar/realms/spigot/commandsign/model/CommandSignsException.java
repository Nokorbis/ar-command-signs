package net.avatar.realms.spigot.commandsign.model;

public class CommandSignsException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String message;

	public CommandSignsException(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
