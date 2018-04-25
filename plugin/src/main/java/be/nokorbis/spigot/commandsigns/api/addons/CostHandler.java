package be.nokorbis.spigot.commandsigns.api.addons;

import org.bukkit.entity.Player;

public interface CostHandler <T extends CostHolder> extends RequirementHandler <T>
{
    void withdrawPlayer(Player player, T holder);
}
