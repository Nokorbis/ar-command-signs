package net.avatar.realms.spigot.commandsign.command.subcommands;

import net.avatar.realms.spigot.commandsign.command.Command;
import net.avatar.realms.spigot.commandsign.controller.Container;
import net.avatar.realms.spigot.commandsign.controller.Economy;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import net.avatar.realms.spigot.commandsign.model.CommandSignsCommandException;
import net.avatar.realms.spigot.commandsign.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class InfoCommand extends Command {

    public InfoCommand() {
        this.command = "info";
        this.aliases.add("i");
        this.basePermission = "commandsign.admin.info";
    }
    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (!(sender instanceof Player)) {
            throw new CommandSignsCommandException(Messages.PLAYER_COMMAND);
        }
        Player player = (Player) sender;

        if (args.size() > 2) {
            try {
                long id = Long.parseLong(args.get(1));
                CommandBlock cmd = Container.getContainer().getCommandBlockById(id);
                if (cmd == null) {
                    throw new CommandSignsCommandException(Messages.INVALID_COMMAND_ID);
                }
                info(player, ChatColor.GREEN, cmd);
            }
            catch (NumberFormatException ex) {
                throw new CommandSignsCommandException(Messages.NUMBER_ARGUMENT);
            }
        }
        else {
            if (!isPlayerAvailable(player)) {
                return false;
            }
            Container.getContainer().getInfoPlayers().add(player);
            player.sendMessage(ChatColor.GOLD + Messages.CLICK_FOR_INFO);
        }

        return true;
    }

    public void info (Player player, ChatColor c, CommandBlock cmdB) {
        player.sendMessage(c + "Id : " + cmdB.getId());
        player.sendMessage(c + Messages.NAME + " : " + ((cmdB.getName() == null)? Messages.NO_NAME : cmdB.getName()));
        player.sendMessage(c + Messages.BLOCK + " : " + cmdB.blockSummary());
        if (Economy.getEconomy() != null) {
            player.sendMessage(c + Messages.COSTS + " : " + Economy.getEconomy().format(cmdB.getEconomyPrice()));
        }
        player.sendMessage(c + Messages.NEEDED_PERMISSIONS + " :");
        int cpt = 1;
        for (String perm : cmdB.getNeededPermissions()) {
            player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
        }
        player.sendMessage(c + Messages.PERMISSIONS + " : ");
        cpt = 1;
        for (String perm :cmdB.getPermissions()) {
            player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
        }
        player.sendMessage(c + Messages.COMMANDS + " : ");
        cpt = 1;
        for (String cmd : cmdB.getCommands()) {
            player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
        }
        if ((cmdB.getTimer() != null) && (cmdB.getTimer() > 0)) {
            player.sendMessage(c + Messages.TIME_BEFORE_EXECUTION + " : ");
            player.sendMessage(ChatColor.GRAY + "" + cmdB.getTimer() + " " + Messages.SECONDS);
            if (cmdB.isCancelledOnMove()) {
                player.sendMessage(ChatColor.GRAY + "---" + Messages.CANCELLED_ON_MOVE);
            }
            if (cmdB.isResetOnMove()) {
                player.sendMessage(ChatColor.GRAY + "---" + Messages.RESET_ON_MOVE);
            }
        }
        if (cmdB.getTimeBetweenUsage() > 0) {
            player.sendMessage(c + Messages.TIME_BETWEEN_USAGES + " : " + cmdB.getTimeBetweenUsage());
        }
        if (cmdB.getTimeBetweenPlayerUsage() > 0) {
            player.sendMessage(c + Messages.TIME_BETWEEN_PLAYER_USAGE + " : " + cmdB.getTimeBetweenPlayerUsage());
        }
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign info [ID]");
    }
}
