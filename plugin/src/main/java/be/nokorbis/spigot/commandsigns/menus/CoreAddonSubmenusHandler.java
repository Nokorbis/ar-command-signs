package be.nokorbis.spigot.commandsigns.menus;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.menu.*;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


public abstract class CoreAddonSubmenusHandler extends EditionLeaf<CommandBlock> {

	protected Map<Addon, List<AddonEditionMenu>> subMenusByAddon = null;

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

	public void setSubMenusByAddon(Map<Addon, List<AddonEditionMenu>> menus) {
		this.subMenusByAddon = menus;
		initializeNavigation();
	}

	private void initializeNavigation() {
		totalMenuCount = 0;
		if (subMenusByAddon != null) {
			for (List<AddonEditionMenu> list : subMenusByAddon.values()) {
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
		AddonEditionMenu addonMenu = navigationContext.getAddonMenu();
		if (addonMenu == null) {
			displayBreadcrumb(editor);
			displaySubMenus(editor, data, navigationContext);
		}
		else {
			addonMenu.display(editor, data.getAddonConfigurationData(addonMenu.getAddon()), navigationContext);
		}
	}

	protected final void displaySubMenus(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
		clickableMessageRefresh.sendToPlayer(editor);

		final int page = navigationContext.getPage();
		final int startingIndex = (page-1) * entriesToDisplay;
		final int endIndex = startingIndex + entriesToDisplay;

		Iterator<Map.Entry<Addon, List<AddonEditionMenu>>> menuIterator = subMenusByAddon.entrySet().iterator();
		for (int i = 0; i < endIndex && menuIterator.hasNext(); ) {
			if (startingIndex <= i && i < endIndex) {
				Map.Entry<Addon, List<AddonEditionMenu>> entry = menuIterator.next();
				Addon addon = entry.getKey();
				List<AddonEditionMenu> menus = entry.getValue();
				for (EditionMenu<AddonConfigurationData> menu : menus) {
					if (i >= endIndex) {
						break;
					}
					final String index = String.valueOf( i-startingIndex+1 );
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
		try {
			AddonEditionMenu menu = navigationContext.getAddonMenu();
			if (menu == null) {
				final int choice = Integer.parseInt(message);
				final int page = navigationContext.getPage();
				if(0 < choice && choice <= entriesToDisplay) {
					navigationContext.setPage(1);
					AddonEditionMenu submenu = getSubmenus(page, choice);
					if (submenu != null) {
						navigationContext.setAddonMenu(submenu);
					}
				}
				else if (choice == DONE) {
					navigationContext.setPage(1);
					navigationContext.setCoreMenu(getParent());
				}
				else if (displayPageNavigation) {
					if (choice == NEXT && totalMenuCount > ((page) * entriesToDisplay)) {
						navigationContext.setPage(page+1);
					}
					else if (choice == PREVIOUS && page > 1) {
						navigationContext.setPage(page-1);
					}
				}
			}
			else {
				menu.input(player, data.getAddonConfigurationData(menu.getAddon()), message, navigationContext);
			}
		}
		catch(NumberFormatException e) {
			player.sendMessage(messages.get("menu.entry.number_required"));
		}
	}

	private AddonEditionMenu getSubmenus(final int page, final int index) {
		final int actualIndex = (page-1) * entriesToDisplay + (index-1);
		int i = 0;
		for (Map.Entry<Addon, List<AddonEditionMenu>> addonMenus : subMenusByAddon.entrySet()) {
			List<AddonEditionMenu> menus = addonMenus.getValue();
			if (i + menus.size() <= actualIndex) {
				i+= menus.size();
				continue;
			}
			return menus.get(actualIndex - i);
		}
		return null;
	}
}
