package be.nokorbis.spigot.commandsigns.api.menu;

public class ClickableMessage {
	private String message;
	private String command;

	public ClickableMessage(String message, String command) {
		this.message = message;
		this.command = command;
	}

	public ClickableMessage(String message) {
		this(message, null);
	}

	public String toString() {
		return getJsonMessage(message, command);
	}

	public static String getJsonMessage(String message, String command) {
		StringBuilder json = new StringBuilder(64);
		json.append("{\"text\": \"");
		json.append(message);
		json.append("\"");
		if (command != null) {
			command = command.trim();
			if (!command.isEmpty()) {
				json.append(", \"clickEvent\": {\"action\": \"run_command\", \"value\": \"");
				json.append(command);
				json.append("\"}");
			}
		}
		json.append("}");
		return json.toString();
	}

	public static String getJsonMessage(String message) {
		return getJsonMessage(message, null);
	}
}
