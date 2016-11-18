package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.command.ICommand;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class HelpCommand extends Command {

    private List<ICommand> commands;

    public HelpCommand(List<ICommand> commands) {
        this.command = "help";
        this.aliases.add("h");
        this.aliases.add("?");
        this.basePermission = "commandsigns.admin.help";

        this.commands = commands;
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (args.size() < 2) {
            if (!(sender instanceof Player)) {
                throw new CommandSignsCommandException(Messages.get("error.player_command"));
            }
            for (ICommand cmd : this.commands) {
                cmd.printUsage(sender, false);
            }
        }
        else {
            String subCmd = args.get(1);
            for (ICommand cmd : this.commands) {
                if (cmd.isCommand(subCmd)) {
                    cmd.printUsage(sender);
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsigns help [command]");
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        LinkedList<String> values = new LinkedList<String>();
        if (args.size() == 1) {
            for (ICommand cmd : this.commands) {
                if (cmd.hasBasePermission(sender)) {
                    values.add(cmd.getCommand());
                }
            }
        }
        return values;
    }
}