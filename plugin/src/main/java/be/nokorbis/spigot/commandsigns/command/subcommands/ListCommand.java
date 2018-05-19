package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.controller.Container;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import be.nokorbis.spigot.commandsigns.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class ListCommand extends Command
{
    private static final int LIST_SIZE = 10;
    private NCommandSignsManager manager;

    public ListCommand(NCommandSignsManager manager)
    {
        super("list", new String[]{ "l" });
        this.manager = manager;
        this.basePermission = "commandsign.admin.list";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException
    {
        if (!(sender instanceof Player))
        {
            throw new CommandSignsCommandException(Messages.get("error.player_command"));
        }

        int index = 1;
        //If there is an argument, try to use it as page number
        if (!args.isEmpty())
        {
            try
            {
                index = Integer.parseInt(args.get(0));
            }
            catch (NumberFormatException ignored)
            {
            }
        }

        int max = (index * LIST_SIZE) - 1;
        int min = (max+1) - LIST_SIZE;

        String m = Messages.get("info.list_summary");
        m = m.replace("{MIN}", String.valueOf(min));
        m = m.replace("{MAX}", String.valueOf(max));
        m = m.replace("{CMD_AMOUNT}", String.valueOf(CommandBlock.getBiggerUsedId()));
        sender.sendMessage(m);

        Container.getContainer().getCommandBlocks().values().stream()
                .filter(cmd -> cmd.getId() >= min && cmd.getId() <= max)
                .sorted(((o1, o2) -> (int)(o1.getId()-o2.getId())))
                .forEach(cmd ->
                {
                    sender.sendMessage(formatCommandBlock(cmd));
                });

        return true;
    }

    private String formatCommandBlock(CommandBlock cmd)
    {
        String msg = Messages.get("info.list_format");
        msg = msg.replace("{POSITION}", cmd.blockSummary());

        if (cmd.getName() != null)
        {
            msg = msg.replace("{NAME}", cmd.getName());
        }
        else
        {
            msg = msg.replace("{NAME}", Messages.get("info.no_name"));
        }
        msg = msg.replace("{ID}", String.valueOf(cmd.getId()));

        return msg;
    }

    @Override
    public void printUsage(CommandSender sender)
    {
        sender.sendMessage("/commandsign list [page]");
    }
}
