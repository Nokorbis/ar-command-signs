package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public abstract class AddonEditionNode extends AddonEditionMenu {
	protected final List<AddonEditionMenu> menus;

	private boolean displayPageNavigation = false;
	private int     entriesToDisplay      = 6;

	public AddonEditionNode(Addon addon, String name, AddonEditionMenu parent) {
		super(addon, name, parent);
		this.menus = new ArrayList<>();
		initializeSubMenus();
		initializeNavigation();
	}

	public AddonEditionNode(Addon addon, String name) {
		this(addon, name, null);
	}

	public void addMenu(AddonEditionMenu menu) {
		menus.add(menu);
	}

	protected abstract void initializeSubMenus();

	private void initializeNavigation() {
		if (menus.size() > 8) {
			displayPageNavigation = true;
			entriesToDisplay = 6;
		}
		else {
			displayPageNavigation = false;
			entriesToDisplay = menus.size();
		}
	}

	protected final int getNumberEntriesToDisplay() {
		return entriesToDisplay;
	}

	protected final boolean shouldDisplayNavigation() {
		return displayPageNavigation;
	}

	@Override
	public void display(final Player editor, final AddonConfigurationData data, final MenuNavigationContext navigationContext) {
		displayBreadcrumb(editor);
		displayMenus(editor, data, navigationContext);
	}

	protected void displayMenus(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		clickableMessageRefresh.sendToPlayer(editor);

		displaySubmenus(editor, data, navigationContext);
		displayPageNavigation(editor, navigationContext.getPage());

		clickableMessageDone.sendToPlayer(editor);
	}

	protected final void displaySubmenus(Player editor, AddonConfigurationData data, MenuNavigationContext navigationContext) {
		final int page = navigationContext.getPage();
		final int startingIndex = (page - 1) * entriesToDisplay;

		ListIterator<AddonEditionMenu> menuIterator = menus.listIterator(startingIndex);
		for (int i = 1 ; i <= entriesToDisplay && menuIterator.hasNext() ; i++) {
			final AddonEditionMenu menu = menuIterator.next();
			final String index = String.valueOf(i);

			final String message = menu.getDisplayString(data).replace("{INDEX}", index);
			ClickableMessage display = new ClickableMessage(message, index);
			display.sendToPlayer(editor);
		}
	}

	protected void displayPageNavigation(final Player editor, final int page) {
		if (displayPageNavigation) {
			if (page > 1) {
				getClickableMessagePrevious().sendToPlayer(editor);
			}
			if ((page * entriesToDisplay) < menus.size()) {
				getClickableMessageNext().sendToPlayer(editor);
			}
		}
	}

	@Override
	public void input(final Player player, final AddonConfigurationData data, final String message, final MenuNavigationContext navigationResult) {
		try {
			final int choice = Integer.parseInt(message);
			final int page = navigationResult.getPage();
			final int numberEntriesToDisplay = getNumberEntriesToDisplay();

			if(0 < choice && choice <= numberEntriesToDisplay) {
				navigationResult.setPage(1);
				AddonEditionMenu newAddonMenu = menus.get((page - 1) * numberEntriesToDisplay + (choice - 1));
				navigationResult.setAddonMenu(newAddonMenu);
			}
			else if (choice == DONE) {
				navigationResult.setPage(1);
				navigationResult.setAddonMenu(getParent());
			}
			else if (shouldDisplayNavigation()) {
				if (choice == NEXT && menus.size() > ((page) * numberEntriesToDisplay)) {
					navigationResult.setPage(page+1);
				}
				else if (choice == PREVIOUS && page > 1) {
					navigationResult.setPage(page-1);
				}
			}
		}
		catch(NumberFormatException e) {
			player.sendMessage(messages.get("menu.entry.number_required"));
		}
	}
}
