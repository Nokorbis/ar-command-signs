package be.nokorbis.spigot.commandsigns.api.addons;

import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.List;


public interface AddonConfigurationDataEditor extends AddonRelated {

    void editValue(AddonConfigurationData configurationData, List<String> args) throws CommandSignsCommandException;

    List<String> onTabComplete(CommandSender sender,  AddonConfigurationData configurationData, List<String> args);
}
