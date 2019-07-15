package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.Callable;


public class EconomyLifecycleHooker extends AddonLifecycleHookerBase {

	private final Economy economy;

	public EconomyLifecycleHooker(EconomyAddon addon, Economy economy) {
		super(addon);
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
					String err = messages.get("usage.not_enough_money");
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
				if (player.hasPermission("commandsign.costs.bypass")) {
					player.sendMessage(messages.get("usage.economy.bypassed"));
				}
				else {
					Bukkit.getScheduler().callSyncMethod(addon.getPlugin(), new EconomyWithdrawer(economy, player, price));
				}
			}
		}
	}

	private static class EconomyWithdrawer implements Callable<Void> {

		private final Economy economy;
		private final Player player;
		private final double price;

		EconomyWithdrawer(Economy economy, Player player, double price) {
			this.economy = economy;
			this.player = player;
			this.price = price;
		}

		@Override
		public Void call() {
			economy.withdrawPlayer(player, player.getWorld().getName(), price);
			player.sendMessage(messages.get("usage.you_paied").replace("{PRICE}", economy.format(price)));

			return null;
		}
	}

}
