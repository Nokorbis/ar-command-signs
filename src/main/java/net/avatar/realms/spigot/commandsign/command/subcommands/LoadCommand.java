package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

/**
 * Created by Nokorbis on 22/08/2016.
 */
public class LoadCommand extends Command {

    public LoadCommand() {
        this.command = "reload";
        this.aliases.add("load");
        this.basePermission = "commandsign.admin.reload";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!sender.hasPermission("commandsign.admin.reload")) {
            throw new CommandSignsCommandException(Messages.get("error.no_permission"));
        }
        if (args.isEmpty()) {
            int errors = Container.getContainer().reload();
            sender.sendMessage(ChatColor.GREEN + Messages.get("info.reload_done"));
            sender.sendMessage(ChatColor.YELLOW + Messages.get("info.reload_errors").replace("{ERRORS}", String.valueOf(errors)));
        }
        else {
            try {
                String idText = args.remove(0);
                int id = Integer.parseInt(idText);
                CommandBlock cmdBlock = Container.getContainer().getSaver().load(id);
                if (cmdBlock == null) {
                    throw new CommandSignsCommandException(Messages.get("error.reload_null"));
                }
                Map<Location, CommandBlock> blocks = Container.getContainer().getCommandBlocks();
                CommandBlock old = blocks.get(cmdBlock.getLocation());
                if (old != null) {
                    if (old.getId() != id) {
                        String msg = Messages.get("error.load_location_used");
                        Location loc = old.getLocation();
                        msg = msg.replace("{WORLD}", loc.getWorld().getName())
                                .replace("{X}", String.valueOf(loc.getBlockX()))
                                .replace("{Y}", String.valueOf(loc.getBlockY()))
                                .replace("{Z}", String.valueOf(loc.getBlockZ()));
                        throw new CommandSignsCommandException(msg);
                    }
                    blocks.remove(cmdBlock.getLocation());
                }
                blocks.put(cmdBlock.getLocation(), cmdBlock);
                sender.sendMessage(ChatColor.GREEN + Messages.get("info.reload_done"));
            }
            catch (NumberFormatException ex) {
                throw new CommandSignsCommandException(Messages.get("error.number_argument"));
            }
        }

        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign reload [ID]");
    }
}
