package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
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

	public CoreAddonSubmenusHandler(String name, EditionMenu<CommandBlock> parent) {
		super(name, parent);
	}

	@Override
	public String getDataString(CommandBlock data) {
		return name;
	}

	public void setSubMenusByAddon(Map<Addon, List<EditionMenu<AddonConfigurationData>>> menus) {
		this.subMenusByAddon = menus;
		initializeNavigation();
	}

	private void initializeNavigation() {
		int count = 0;
		if (subMenusByAddon != null) {
			for (List<EditionMenu<AddonConfigurationData>> list : subMenusByAddon.values()) {
				count += list.size();
			}
		}
		if (count > 8) {
			displayPageNavigation = true;
			entriesToDisplay = 6;
		}
		else {
			displayPageNavigation = false;
			entriesToDisplay = count;
		}
	}

	@Override
	public void display(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		displayBreadcrumb(editor);

		displaySubMenus(editor, data, navigationContext);
	}

	protected final void displaySubMenus(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		editor.sendMessage(mainMessages.getString("menu.entry.refresh"));

		final int page = navigationContext.getPage();
		final int startingIndex = (page-1) * entriesToDisplay;
		final int endIndex = startingIndex + entriesToDisplay;

		Iterator<Map.Entry<Addon, List<EditionMenu<AddonConfigurationData>>>> menuIterator = subMenusByAddon.entrySet().iterator();
		for (int i = 0; i < endIndex && menuIterator.hasNext(); ) {
			if (startingIndex <= i && i < endIndex) {
				Map.Entry<Addon, List<EditionMenu<AddonConfigurationData>>> entry = menuIterator.next();
				Addon addon = entry.getKey();
				List<EditionMenu<AddonConfigurationData>> menus = entry.getValue();
				for (EditionMenu<AddonConfigurationData> menu : menus) {
					if (i >= endIndex) {
						break;
					}
					editor.sendMessage(menu.getDisplayString(data.getAddonConfigurationData(addon)));
					i++;
				}
			}
		}

		if (displayPageNavigation) {
			editor.sendMessage(mainMessages.getString("menu.entry.previous"));
			editor.sendMessage(mainMessages.getString("menu.entry.next"));
		}
		editor.sendMessage(mainMessages.getString("menu.entry.done"));
	}

	@Override
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationContext) {

	}
}
