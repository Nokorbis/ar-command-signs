package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
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
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }

        int index = 1;
        //If there is an argument, try to use it as page number
        if (args.size() >= 2) {
            try {
                index = Integer.parseInt(args.get(1));
            }
            catch (NumberFormatException ignored) {
            }
        }

        int max = index * LIST_SIZE;
        int min = max - LIST_SIZE;
        max--;
        String m = Messages.get("info.list_summary");
        m = m.replace("{MIN}", String.valueOf(min));
        m = m.replace("{MAX}", String.valueOf(max));
        m = m.replace("{CMD_AMOUNT}", String.valueOf(CommandBlock.getBiggerUsedId()));
        sender.sendMessage(m);

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
        String msg = Messages.get("info.list_format");
        msg = msg.replace("{POSITION}", cmd.blockSummary());

        if (cmd.getName() != null) {
            msg = msg.replace("{NAME}", cmd.getName());
        }
        else {
            msg = msg.replace("{NAME}", Messages.get("info.no_name"));
        }
        msg = msg.replace("{ID}", String.valueOf(cmd.getId()));

        return msg;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign list [page]");
    }
}
