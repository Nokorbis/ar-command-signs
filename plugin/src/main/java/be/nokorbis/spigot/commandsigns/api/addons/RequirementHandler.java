package be.nokorbis.spigot.commandsigns.api.addons;

import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import org.bukkit.entity.Player;

public interface RequirementHandler <T extends RequirementHolder>
{
    void checkRequirement(Player player, T requirement) throws CommandSignsRequirementException;
}
