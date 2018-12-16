package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;


public class EconomyConfigurationData extends AddonConfigurationData {

	private double price;

	public EconomyConfigurationData(Addon addon) {
		super(addon);
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
