package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.Messages;

import com.google.gson.JsonObject;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class EconomyCostHandler implements CostHandler {

    private Economy economy;

    EconomyCostHandler(Economy economy) {
        this.economy = economy;
    }

    @Override
    public void checkRequirement(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException {
    	if (economy != null) {
			JsonObject data = configurationData.getConfigurationData();
			double price = data.getAsJsonPrimitive("price").getAsDouble();
			if (price > 0.0) {
				if (!economy.has(player, player.getWorld().getName(), price) && !player.hasPermission("commandsign.costs.bypass")) {
					String err = Messages.get("usage.not_enough_money");
					err = err.replace("{PRICE}", economy.format(price));
					throw new CommandSignsRequirementException(err);
				}
			}
		}
    }

    @Override
    public void withdrawPlayer(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		if (economy != null) {
			JsonObject data = configurationData.getConfigurationData();
			double price = data.getAsJsonPrimitive("price").getAsDouble();
			if (price > 0.0) {
				economy.withdrawPlayer(player, player.getWorld().getName(), price);
			}
		}
    }
}
