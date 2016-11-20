package net.bendercraft.spigot.commandsigns.command.subcommands;

import net.bendercraft.spigot.commandsigns.CommandSignsPlugin;
import net.bendercraft.spigot.commandsigns.command.Command;
import net.bendercraft.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by nokorbis on 1/20/16.
 */
public class VersionCommand extends Command {

    public VersionCommand() {
        this.command = "version";
        this.aliases.add("v");
        this.basePermission = "commandsign.admin.version";
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        sender.sendMessage(ChatColor.AQUA + "CommandSign version : " + CommandSignsPlugin.getPlugin().getDescription().getVersion() + " developed by Nokorbis");
        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign version");
    }
}
