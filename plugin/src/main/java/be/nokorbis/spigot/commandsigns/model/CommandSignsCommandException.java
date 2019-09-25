package be.nokorbis.spigot.commandsigns.model;

import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;

/**
 * Represent an exception thrown when executing a command
 * Created by nokorbis on 1/20/16.
 */
public class CommandSignsCommandException extends CommandSignsException {
    public CommandSignsCommandException(String message) {
        super(message);
    }
}
