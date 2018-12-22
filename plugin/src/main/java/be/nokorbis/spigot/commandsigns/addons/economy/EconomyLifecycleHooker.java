package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;


public class EconomyLifecycleHooker extends AddonLifecycleHookerBase {

	private Economy economy;

	public EconomyLifecycleHooker(Economy economy) {
		this.economy = economy;
	}

	@Override
	@NCSLifecycleHook
	public void onRequirementCheck(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException {
		if (economy != null) {
			EconomyConfigurationData configuration = (EconomyConfigurationData) configurationData;
			double price = configuration.getPrice();
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
	@NCSLifecycleHook
	public void onCostWithdraw(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		if (economy != null) {
			EconomyConfigurationData configuration = (EconomyConfigurationData) configurationData;
			double price = configuration.getPrice();
			if (price > 0.0) {
				economy.withdrawPlayer(player, player.getWorld().getName(), price);
			}
		}
	}

}
