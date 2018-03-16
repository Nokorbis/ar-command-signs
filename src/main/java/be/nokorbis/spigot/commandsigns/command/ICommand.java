package be.nokorbis.spigot.commandsigns.command;

import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public interface ICommand {

    /**
     * Handle the execution of the command with some args by a sender
     * @param sender
     *      The user that sent the command
     * @param args
     *      Arguments of the command
     * @return
     *      <code>true</code> if everything went right.
     *      <code>false</code> if something went wrong so that Bukkit handle an error message.
     */
    boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException;

    /**
     * Send a message to the sender to explain the command should be used
     * @param sender
     *      The user that needs the usage information
     */
    void printUsage(CommandSender sender);

    /**
     * Send a message to the sender to explain the command should be used
     * @param sender
     *      The user that needs the usage information
     * @param permission
     *      Do we have to consider that the user has the permission of the command's usage
     */
    void printUsage(CommandSender sender, boolean permission) throws CommandSignsCommandException;

    /**
     * Check if the given String matches this command or one of its aliases
     * @param command
     *      The String to check
     * @return
     *      <code>true</code> if the given String matches the command
     *      <code>false</code> otherwise
     */
    boolean isCommand(String command);

    /**
     * Get the command String for this command
     * @return a String
     */
    String getCommand();

    /**
     * Get all possibles values for a given command and given arguments
     *
     * @param sender
     *            The sender of the command
     * @param args
     *            The args that the sender sent (without the first arg being the
     *            subcommand)
     * @return List<String> of possible values
     */
    List<String> autoComplete(CommandSender sender, List<String> args);

    /**
     * Check if the user has the needed permission for this command
     * @param sender
     *      The user that we want to check
     * @return
     *      <code>true</code> If the user has the permission
     *      <code>false</code> If the user does NOT have the permission
     */
    boolean hasBasePermission(CommandSender sender);
}
