package be.nokorbis.spigot.commandsigns.api.menu;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class MenuNavigationContext implements Cloneable {

	private EditionMenu<CommandBlock> coreMenu;
	private AddonEditionMenu addonMenu;

	private int page = 1;

	public MenuNavigationContext() {
	}

	public MenuNavigationContext(EditionMenu<CommandBlock> menu, int page) {
		this.coreMenu = menu;
		this.page = page;
	}

	public EditionMenu<CommandBlock> getCoreMenu() {
		return coreMenu;
	}

	public void setCoreMenu(EditionMenu<CommandBlock> menu) {
		this.coreMenu = menu;
	}

	public AddonEditionMenu getAddonMenu() {
		return addonMenu;
	}

	public void setAddonMenu(AddonEditionMenu addonMenu) {
		this.addonMenu = addonMenu;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
