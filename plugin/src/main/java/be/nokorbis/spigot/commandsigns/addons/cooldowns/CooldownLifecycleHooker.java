package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.data.CooldownConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.cooldowns.data.CooldownExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class CooldownLifecycleHooker extends AddonLifecycleHookerBase {


	public CooldownLifecycleHooker(CooldownAddon addon) {
		super(addon);
	}

	@Override
	@NCSLifecycleHook
	public final void onRequirementCheck(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) throws CommandSignsRequirementException {
		if (player != null && !player.hasPermission("commandsign.timer.bypass")) {
			final CooldownConfigurationData conf = (CooldownConfigurationData) configurationData;

			if (conf.hasGlobalCooldown() || conf.hasPlayerCooldown()) {

				final CooldownExecutionData data = (CooldownExecutionData) executionData;
				final Long lastTimeSomeoneUsed = data.getLastTimeUsed();
				final Long lastTimePlayerUsed = data.getLastPlayerUsage(player);

				checkPlayerCooldown(lastTimePlayerUsed, conf.getPlayerCooldown() * 1000);
				checkGlobalCooldown(lastTimeSomeoneUsed, conf.getGlobalCooldown() * 1000);
			}
		}
	}

	private void checkGlobalCooldown(final Long lastTimeSomeoneUsed, final long globalCooldown) throws CommandSignsRequirementException {
		if (lastTimeSomeoneUsed != null) {
			final long now = System.currentTimeMillis();
			long timeToWait = lastTimeSomeoneUsed + globalCooldown - now;
			if (timeToWait > 0) {
				String msg = messages.get("usage.general_cooldown");
				String time = CommandSignUtils.formatTime((globalCooldown-timeToWait)/1000.0);
				String remaining = CommandSignUtils.formatTime((timeToWait)/1000.0);
				msg = msg.replace("{TIME}", time);
				msg = msg.replace("{REMAINING}", remaining);
				throw new CommandSignsRequirementException(msg);
			}
		}
	}

	private void checkPlayerCooldown(final Long lastTimePlayerUsed, final long playerCooldown) throws CommandSignsRequirementException {
		if (lastTimePlayerUsed != null) {
			final long now = System.currentTimeMillis();
			long timeToWait = lastTimePlayerUsed + playerCooldown - now;
			if (timeToWait > 0) {
				String msg = messages.get("usage.player_cooldown");
				String time = CommandSignUtils.formatTime((now - lastTimePlayerUsed)/1000.0);
				String remaining = CommandSignUtils.formatTime((timeToWait)/1000.0);
				msg = msg.replace("{TIME}", time);
				msg = msg.replace("{REMAINING}", remaining);
				throw new CommandSignsRequirementException(msg);
			}
		}
	}

	@Override
	@NCSLifecycleHook
	public final void onPostExecution(final Player player, final AddonConfigurationData configurationData, final AddonExecutionData executionData) {
		final CooldownExecutionData data = (CooldownExecutionData) executionData;

		data.refresh((CooldownConfigurationData) configurationData);
		data.addPlayerUsage(player);
	}
}
