package be.nokorbis.spigot.commandsigns.model;

/**
 * Represent an exception thrown when executing a command
 * Created by nokorbis on 1/20/16.
 */
public class CommandSignsCommandException extends CommandSignsException{
    public CommandSignsCommandException(String message) {
        super(message);
    }
}
