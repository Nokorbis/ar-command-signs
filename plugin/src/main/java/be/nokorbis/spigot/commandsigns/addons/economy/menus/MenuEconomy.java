package be.nokorbis.spigot.commandsigns.addons.economy.menus;

import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.utils.Messages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;


public class MenuEconomy extends EditionLeaf<AddonConfigurationData> {

	private Economy economy;

	public MenuEconomy(Economy economy) {
		super(Messages.get("menu.economy"), null);
		this.economy = economy;
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		EconomyConfigurationData configurationData = (EconomyConfigurationData) data;
		return name.replace("{PRICE}", economy.format(configurationData.getPrice()));
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		EconomyConfigurationData configurationData = (EconomyConfigurationData) data;
		String msg = Messages.get("menu.economy_edit");
		msg = msg.replace("{PRICE}", String.valueOf(configurationData.getPrice()));
		editor.sendMessage(msg);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			EconomyConfigurationData configurationData = (EconomyConfigurationData) data;
			String[] args = message.split(" ");
			double value = Double.parseDouble(args[0]);
			configurationData.setPrice(value);
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
