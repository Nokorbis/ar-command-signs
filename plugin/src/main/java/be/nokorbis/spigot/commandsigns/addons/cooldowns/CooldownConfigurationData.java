package be.nokorbis.spigot.commandsigns.addons.cooldowns;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;


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

	public long getGlobalCooldown()
	{
		return globalCooldown;
	}

	public void setGlobalCooldown(long globalCooldown)
	{
		this.globalCooldown = globalCooldown;
	}

	public long getPlayerCooldown()
	{
		return playerCooldown;
	}

	public void setPlayerCooldown(long playerCooldown)
	{
		this.playerCooldown = playerCooldown;
	}
}
