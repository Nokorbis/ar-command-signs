package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
			JsonObject conf = configurationData.getConfigurationData();
			long globalCooldown = conf.getAsJsonPrimitive("global_cooldown").getAsLong();
			long playerCooldown = conf.getAsJsonPrimitive("player_cooldown").getAsLong();
			if (globalCooldown > 0 || playerCooldown > 0) {


				String playerUuid = player.getUniqueId().toString();

				JsonObject data = executionData.getExecutionData();
				JsonArray usages = data.getAsJsonArray("usages");
				Long lastTimeSomeoneUsed = null;
				Long lastTimePlayerUsed = null;

				for (JsonElement usage : usages) {
					JsonObject object = usage.getAsJsonObject();
					long usageTime = object.getAsJsonPrimitive("time").getAsLong();
					if (lastTimeSomeoneUsed == null || lastTimeSomeoneUsed < usageTime) {
						lastTimeSomeoneUsed = usageTime;
					}

					String usageUuid = object.getAsJsonPrimitive("player_uuid").getAsString();
					if (playerUuid.equals(usageUuid)) {
						lastTimePlayerUsed = usageTime;
					}
				}

				checkPlayerCooldown(lastTimePlayerUsed, playerCooldown);
				checkGlobalCooldown(lastTimeSomeoneUsed, globalCooldown);
			}
		}
	}

	private void checkGlobalCooldown(Long lastTimeSomeoneUsed, long globalCooldown) throws CommandSignsRequirementException {
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

	private void checkPlayerCooldown(Long lastTimePlayerUsed, long playerCooldown) throws CommandSignsRequirementException {
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
