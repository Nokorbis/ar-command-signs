package be.nokorbis.spigot.commandsigns.api.menu;

public class MenuNavigationResult {

	private EditionMenu<? extends MenuEditable> menu;
	private int page;

	public MenuNavigationResult() {
	}

	public MenuNavigationResult(EditionMenu<? extends MenuEditable> menu, int page) {
		this.menu = menu;
		this.page = page;
	}

	public EditionMenu<? extends MenuEditable> getMenu() {
		return menu;
	}

	public void setMenu(EditionMenu<? extends MenuEditable> menu) {
		this.menu = menu;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}
