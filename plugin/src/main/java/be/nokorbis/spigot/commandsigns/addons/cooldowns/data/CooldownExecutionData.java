package be.nokorbis.spigot.commandsigns.addons.cooldowns.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class CooldownExecutionData extends AddonExecutionData  {

	private Map<UUID, Long> playerUsages = new HashMap<>();
	private Long lastTimeUsed = null;

	public CooldownExecutionData(final Addon addon) {
		super(addon);
	}

	public Long getLastTimeUsed() {
		return lastTimeUsed;
	}

	public void refresh(final CooldownConfigurationData configuration) {
		removeUneccessaryUsages(configuration);
	}

	private void removeUneccessaryUsages(final CooldownConfigurationData configuration) {
		final long now = System.currentTimeMillis();
		final long playerCooldown = configuration.getPlayerCooldown();
		playerUsages.entrySet().removeIf(entry -> entry.getValue() + playerCooldown < now);
	}

	public Long getLastPlayerUsage(final Player player) {
		return playerUsages.get(player.getUniqueId());
	}

	public void addPlayerUsage(final UUID uuid, final long time) {
		this.playerUsages.put(uuid, time);
		if (lastTimeUsed == null || time > lastTimeUsed) {
			this.lastTimeUsed = time;
		}
	}

	public void addPlayerUsage(final Player player, final long time) {
		addPlayerUsage(player.getUniqueId(), time);
	}

	public void addPlayerUsage(final Player player) {
		addPlayerUsage(player, System.currentTimeMillis());
	}

	Map<UUID, Long> getPlayerUsages() {
		return this.playerUsages;
	}

	@Override
	public AddonExecutionData copy() {
		CooldownExecutionData executionData = new CooldownExecutionData(addon);

		executionData.lastTimeUsed = this.lastTimeUsed;
		executionData.playerUsages = new HashMap<>(this.playerUsages);

		return executionData;
	}
}
