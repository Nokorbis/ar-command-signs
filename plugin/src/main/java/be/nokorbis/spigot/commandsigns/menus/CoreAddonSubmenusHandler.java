package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.ClickableMessage;
import be.nokorbis.spigot.commandsigns.api.menu.EditionLeaf;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class CoreAddonSubmenusHandler extends EditionLeaf<CommandBlock> {

	protected Map<Addon, List<EditionMenu<AddonConfigurationData>>> subMenusByAddon = null;

	private boolean displayPageNavigation = false;
	private int entriesToDisplay = 6;
	private int totalMenuCount = 0;

	public CoreAddonSubmenusHandler(String name, EditionMenu<CommandBlock> parent) {
		super(name, parent);
	}

	@Override
	public String getDisplayString(CommandBlock data) {
		return messages.get("menu.entry.display_name_only")
					   .replace("{NAME}", name);
	}

	@Override
	public String getDataValue(CommandBlock data) {
		return "";
	}

	public void setSubMenusByAddon(Map<Addon, List<EditionMenu<AddonConfigurationData>>> menus) {
		this.subMenusByAddon = menus;
		initializeNavigation();
	}

	private void initializeNavigation() {
		totalMenuCount = 0;
		if (subMenusByAddon != null) {
			for (List<EditionMenu<AddonConfigurationData>> list : subMenusByAddon.values()) {
				totalMenuCount += list.size();
			}
		}
		if (totalMenuCount > 8) {
			displayPageNavigation = true;
			entriesToDisplay = 6;
		}
		else {
			displayPageNavigation = false;
			entriesToDisplay = totalMenuCount;
		}
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		displayBreadcrumb(editor);

		displaySubMenus(editor, data, navigationContext);
	}

	protected final void displaySubMenus(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		clickableMessageRefresh.sendToPlayer(editor);

		final int page = navigationContext.getPage();
		final int startingIndex = (page-1) * entriesToDisplay;
		final int endIndex = startingIndex + entriesToDisplay;

		Iterator<Map.Entry<Addon, List<EditionMenu<AddonConfigurationData>>>> menuIterator = subMenusByAddon.entrySet().iterator();
		for (int i = 0; i < endIndex && menuIterator.hasNext(); ) {
			if (startingIndex <= i && i < endIndex) {
				final String index = String.valueOf( i-startingIndex );
				Map.Entry<Addon, List<EditionMenu<AddonConfigurationData>>> entry = menuIterator.next();
				Addon addon = entry.getKey();
				List<EditionMenu<AddonConfigurationData>> menus = entry.getValue();
				for (EditionMenu<AddonConfigurationData> menu : menus) {
					if (i >= endIndex) {
						break;
					}
					AddonConfigurationData confData = data.getAddonConfigurationData(addon);
					final String message = menu.getDisplayString(confData).replace("{INDEX}", index);
					ClickableMessage display = new ClickableMessage(message, index);
					display.sendToPlayer(editor);
					i++;
				}
			}
		}

		displayNavigation(editor, page);
		clickableMessageDone.sendToPlayer(editor);
	}

	private void displayNavigation(Player editor, int page) {
		if (displayPageNavigation) {
			if (page > 1) {
				clickableMessagePrevious.sendToPlayer(editor);
			}
			if ((page * entriesToDisplay) < totalMenuCount) {
				clickableMessageNext.sendToPlayer(editor);
			}
		}
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {

	}
}
