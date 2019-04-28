package be.nokorbis.spigot.commandsigns.api.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.entity.Player;


public abstract class EditionNode <EDITABLE extends MenuEditable> extends EditionMenu<EDITABLE> {

	protected final List<EditionMenu<EDITABLE>> menus;

	private boolean displayPageNavigation = false;
	private int     entriesToDisplay      = 6;

	public EditionNode(String name, EditionMenu<EDITABLE> parent) {
		super(name, parent);
		this.menus = new ArrayList<>();
		initializeSubMenus();
		initializeNavigation();
	}

	public EditionNode(String name) {
		this(name, null);
	}

	public void addMenu(EditionMenu<EDITABLE> menu) {
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
	public void display(final Player editor, final EDITABLE data, final MenuNavigationContext navigationContext) {
		displayBreadcrumb(editor);
		displayMenus(editor, data, navigationContext);
	}

	protected void displayMenus(Player editor, EDITABLE data, MenuNavigationContext navigationContext) {
		clickableMessageRefresh.sendToPlayer(editor);

		displaySubmenus(editor, data, navigationContext);
		displayPageNavigation(editor, navigationContext.getPage());

		clickableMessageDone.sendToPlayer(editor);
	}

	protected final void displaySubmenus(Player editor, EDITABLE data, MenuNavigationContext navigationContext) {
		final int page = navigationContext.getPage();
		final int startingIndex = (page - 1) * entriesToDisplay;

		ListIterator<EditionMenu<EDITABLE>> menuIterator = menus.listIterator(startingIndex);
		for (int i = 1 ; i <= entriesToDisplay && menuIterator.hasNext() ; i++) {
			final EditionMenu<EDITABLE> menu = menuIterator.next();
			if (menu.shouldBeDisplayed(data)) {
				final String index = String.valueOf(i);

				final String message = menu.getDisplayString(data).replace("{INDEX}", index);
				ClickableMessage display = new ClickableMessage(message, index);
				display.sendToPlayer(editor);
			}
		}
	}

	protected void displayPageNavigation(final Player editor, final int page) {
		if (displayPageNavigation) {
			if (page > 1) {
				clickableMessagePrevious.sendToPlayer(editor);
			}
			if ((page * entriesToDisplay) < menus.size()) {
				clickableMessageNext.sendToPlayer(editor);
			}
		}
	}
}