package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class SetCommand extends CommandRequiringManager {

    public SetCommand(NCommandSignsManager manager) {
        super(manager, "set", new String[0]);
        this.basePermission = "commandsign.admin.set";
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign set <ID> <variable> [add/remove/edit] [index] <value>");
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(commandMessages.get("error.command_needs_arguments"));
        }

        CommandBlock commandBlock = getCommandBlock(args);
        if (commandBlock == null) {
            throw new CommandSignsCommandException(commandMessages.get("error.invalid_command_id"));
        }

        return false;
    }

    private CommandBlock getCommandBlock(List<String> args) {
        try {
            String argId = args.remove(0);
            long id = Long.parseLong(argId);
            return manager.getCommandBlock(id);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        return null;
    }
}
