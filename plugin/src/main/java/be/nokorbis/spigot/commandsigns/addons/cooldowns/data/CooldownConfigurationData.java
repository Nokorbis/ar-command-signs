package be.nokorbis.spigot.commandsigns.addons.cooldowns.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.utils.CommandSignUtils;
import org.bukkit.entity.Player;


public class CooldownConfigurationData extends AddonConfigurationData {

	private long globalCooldown = -1;
	private long playerCooldown = -1;

	public CooldownConfigurationData(Addon addon) {
		super(addon);
	}

	public boolean hasGlobalCooldown() {
		return globalCooldown > 0;
	}

	public boolean hasPlayerCooldown() {
		return playerCooldown > 0;
	}

	public long getGlobalCooldown() {
		return globalCooldown;
	}

	public void setGlobalCooldown(long globalCooldown) {
		this.globalCooldown = globalCooldown;
	}

	public long getPlayerCooldown() {
		return playerCooldown;
	}

	public void setPlayerCooldown(long playerCooldown) {
		this.playerCooldown = playerCooldown;
	}

	@Override
	public AddonConfigurationData copy() {
		CooldownConfigurationData data = new CooldownConfigurationData(addon);
		data.globalCooldown = this.globalCooldown;
		data.playerCooldown = this.playerCooldown;
		return data;
	}

	@Override
	public void info(Player player) {
		if (hasGlobalCooldown()) {
			player.sendMessage(addonMessages.get("info.global_cooldown").replace("{TIME}", CommandSignUtils.formatTime(globalCooldown)));
		}
		if (hasPlayerCooldown()) {
			player.sendMessage(addonMessages.get("info.player_cooldown").replace("{TIME}", CommandSignUtils.formatTime(playerCooldown)));
		}
	}
}
