package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class ListCommand extends Command {

    private static final int LIST_SIZE = 10;

    public ListCommand() {
        this.command = "list";
        this.aliases.add("l");
        this.basePermission = "commandsign.admin.list";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }

        int index = 1;
        //If there is an argument, try to use it as page number
        if (args.size() >= 2) {
            try {
                index = Integer.parseInt(args.get(1));
            }
            catch (NumberFormatException ex) {
            }
        }

        int max = index * LIST_SIZE;
        int min = max - LIST_SIZE;
        max--;
        String m = Messages.LIST_SUMMARY;
        m = m.replaceAll("\\{MIN\\}", String.valueOf(min));
        m = m.replaceAll("\\{MAX\\}", String.valueOf(max));
        m = m.replaceAll("\\{CMD_AMOUNT\\}", String.valueOf(CommandBlock.getBiggerUsedId()));
        sender.sendMessage(ChatColor.AQUA + m);

        List<CommandBlock> cmds = Container.getContainer().getCommandBlocksByIdRange(min, max);
        Collections.sort(cmds, new Comparator<CommandBlock>() {
            @Override
            public int compare(CommandBlock o1, CommandBlock o2) {
                return (int) (o1.getId() - o2.getId());
            }
        });

        for (CommandBlock cmd : cmds) {
            String msg = formatCommandBlock(cmd);
            sender.sendMessage(msg);
        }
        return true;
    }

    private String formatCommandBlock(CommandBlock cmd) {
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.AQUA);
        builder.append(cmd.blockSummary());
        builder.append(ChatColor.GRAY);
        builder.append(" --- ");
        builder.append(ChatColor.GOLD);
        if (cmd.getName() != null) {
            builder.append(cmd.getName());
        }
        else {
            builder.append(Messages.NO_NAME);
        }
        builder.append(ChatColor.GRAY);
        builder.append(" --- ");
        builder.append(ChatColor.BOLD);
        builder.append(ChatColor.DARK_PURPLE);
        builder.append(cmd.getId());
        return builder.toString();
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign list [page]");
    }
}
