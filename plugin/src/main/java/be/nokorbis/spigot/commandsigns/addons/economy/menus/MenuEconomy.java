package be.nokorbis.spigot.commandsigns.addons.economy.menus;

import be.nokorbis.spigot.commandsigns.addons.economy.EconomyAddon;
import be.nokorbis.spigot.commandsigns.addons.economy.data.EconomyConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;


public class MenuEconomy extends AddonEditionLeaf {

	private Economy economy;

	public MenuEconomy(EconomyAddon addon, Economy economy) {
		super(addon, messages.get("menu.economy.title"), null);
		this.economy = economy;
	}

	@Override
	public String getDataValue(AddonConfigurationData data) {
		EconomyConfigurationData configurationData = (EconomyConfigurationData) data;
		return economy.format(configurationData.getPrice());
	}

	@Override
	public void display(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		String msg = messages.get("menu.economy.edit");
		msg = msg.replace("{PRICE}", getDataValue(data));
		ClickableMessage clickableMessage = new ClickableMessage(msg);
		clickableMessage.add(CLICKABLE_CANCEL);
		clickableMessage.sendToPlayer(editor);
	}

	@Override
	public void input(Player player, AddonConfigurationData data, String message, MenuNavigationContext navigationContext) {
		try {
			if (!CANCEL_STRING.equals(message)) {
				EconomyConfigurationData configurationData = (EconomyConfigurationData) data;
				String[] args = message.split(" ");
				double value = Double.parseDouble(args[0]);
				configurationData.setPrice(value);
			}
		}
		catch (Exception ignored) {
		}
		finally {
			navigationContext.setAddonMenu(getParent());
		}
	}
}
