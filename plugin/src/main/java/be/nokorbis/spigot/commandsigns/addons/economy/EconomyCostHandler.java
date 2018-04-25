package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class EconomyCostHandler implements CostHandler<EconomyHolder>
{
    private Economy economy;

    public EconomyCostHandler(Economy economy)
    {
        this.economy = economy;
    }

    @Override
    public void withdrawPlayer(Player player, EconomyHolder holder)
    {

    }

    @Override
    public void checkRequirement(Player player, EconomyHolder requirement) throws CommandSignsRequirementException
    {

    }
}
