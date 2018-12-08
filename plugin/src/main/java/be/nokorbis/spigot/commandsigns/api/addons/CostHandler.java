package be.nokorbis.spigot.commandsigns.api.addons;

import org.bukkit.entity.Player;

public interface CostHandler extends RequirementHandler
{
    void withdrawPlayer(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData);
}
