package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.CommandSignUtils;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class NearCommand extends Command {

    public NearCommand() {
        this.command = "near";
        this.basePermission = "commandsign.admin.near";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!hasBasePermission(sender)) {
            throw new CommandSignsCommandException(Messages.NO_PERMISSION);
        }

        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }

        if (args.size() < 2) {
            throw new CommandSignsCommandException(Messages.COMMAND_NEEDS_RADIUS);
        }

        Player player = (Player) sender;

        try {
            int radius = Integer.parseInt(args.get(1));
            LinkedList<CommandBlock> cmds = new LinkedList<CommandBlock>();
            for (Location loc : CommandSignUtils.getLocationsAroundPoint(player.getLocation(), radius)) {
                if (Container.getContainer().getCommandBlocks().containsKey(loc))  {
                    cmds.add(Container.getContainer().getCommandBlocks().get(loc));
                }
            }
            for (CommandBlock cmd : cmds) {
                sender.sendMessage(formatCommand(cmd));
            }
        }
        catch (NumberFormatException ex) {
            throw new CommandSignsCommandException(Messages.NUMBER_ARGUMENT);
        }

        return true;
    }

    /**
     * Format the message so that it can be easily read by a player
     * @param cmd
     *      The command block whose String needs to be formatted
     * @return
     *      A formatted String
     */
    private String formatCommand(CommandBlock cmd) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GOLD);
        builder.append(cmd.getName());
        builder.append(ChatColor.GRAY);
        builder.append("[");
        builder.append(ChatColor.DARK_PURPLE);
        builder.append(cmd.getId());
        builder.append(ChatColor.GRAY);
        builder.append("] ");
        builder.append(Messages.AT);
        builder.append(" ");
        builder.append(ChatColor.AQUA);
        builder.append(cmd.blockSummary());
        return builder.toString();
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign near <radius>");
    }
}
