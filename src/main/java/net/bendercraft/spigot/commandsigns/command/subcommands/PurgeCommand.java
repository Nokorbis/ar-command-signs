package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.controller.Container;
import net.bendercraft.spigot.commandsigns.model.CommandBlock;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import net.bendercraft.spigot.commandsigns.utils.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class PurgeCommand extends Command{

    public PurgeCommand () {
        this.command = "purge";

        this.basePermission = "commandsigns.admin.purge";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        LinkedList<Location> toRemove = new LinkedList<Location>();
        for (CommandBlock cmd : Container.getContainer().getCommandBlocks().values()) {
            if (!cmd.validate()) {
                toRemove.add(cmd.getLocation());
            }
        }

        for (Location loc : toRemove) {
            CommandBlock temp = Container.getContainer().getCommandBlocks().remove(loc);
            Container.getContainer().getSaver().delete(temp.getId());
        }

        String msg = Messages.get("info.purged_invalid_blocks");
        msg = msg.replace("{AMOUNT}", String.valueOf(toRemove.size()));
        sender.sendMessage(msg);
        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsigns purge");
    }
}
