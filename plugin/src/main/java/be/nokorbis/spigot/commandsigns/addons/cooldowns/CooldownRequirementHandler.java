package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.Messages;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class CooldownRequirementHandler implements RequirementHandler {

	private static final DecimalFormat decimalFormat = new DecimalFormat();
	static {
		DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		decimalFormat.setDecimalFormatSymbols(symbols);
		decimalFormat.setMaximumFractionDigits(2);
	}

	@Override
	public void checkRequirement(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException {
		if (player != null && !player.hasPermission("commandsign.timer.bypass")) {
			final CooldownConfigurationData conf = (CooldownConfigurationData) configurationData;

			if (conf.hasGlobalCooldown() || conf.hasPlayerCooldown()) {

				final CooldownExecutionData data = (CooldownExecutionData) executionData;
				final Long lastTimeSomeoneUsed = data.getLastTimeUsed();
				final Long lastTimePlayerUsed = data.getLastPlayerUsage(player);

				checkPlayerCooldown(lastTimePlayerUsed, conf.getPlayerCooldown());
				checkGlobalCooldown(lastTimeSomeoneUsed, conf.getGlobalCooldown());
			}
		}
	}

	private void checkGlobalCooldown(final Long lastTimeSomeoneUsed, final long globalCooldown) throws CommandSignsRequirementException {
		if (lastTimeSomeoneUsed != null) {
			final long now = System.currentTimeMillis();
			long timeToWait = lastTimeSomeoneUsed + globalCooldown - now;
			if (timeToWait > 0) {
				String msg = Messages.get("usage.general_cooldown");
				msg = msg.replace("{TIME}", decimalFormat.format((globalCooldown- timeToWait) / 1000.0));
				msg = msg.replace("{REMAINING}", decimalFormat.format(timeToWait / 1000.0));
				throw new CommandSignsRequirementException(msg);
			}
		}
	}

	private void checkPlayerCooldown(final Long lastTimePlayerUsed, final long playerCooldown) throws CommandSignsRequirementException {
		if (lastTimePlayerUsed != null) {
			final long now = System.currentTimeMillis();
			long timeToWait = lastTimePlayerUsed + playerCooldown - now;
			if (timeToWait > 0) {
				String msg = Messages.get("usage.player_cooldown");
				msg = msg.replace("{TIME}", decimalFormat.format((now - lastTimePlayerUsed) / 1000.0));
				msg = msg.replace("{REMAINING}", decimalFormat.format(timeToWait / 1000.0));
				throw new CommandSignsRequirementException(msg);
			}
		}
	}
}
