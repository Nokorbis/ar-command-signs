package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.CommandSignUtils;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
        this.basePermission = "commandsigns.admin.near";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }

        if (args.size() < 2) {
            throw new CommandSignsCommandException(Messages.get("error.command_needs_radius"));
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
            throw new CommandSignsCommandException(Messages.get("error.number_argument"));
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
        String msg = Messages.get("info.near_format");
        msg = msg.replace("{NAME}", cmd.getName())
                    .replace("{ID}", String.valueOf(cmd.getId()))
                    .replace("{POSITION}", cmd.blockSummary());
        return msg;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsigns near <radius>");
    }
}
