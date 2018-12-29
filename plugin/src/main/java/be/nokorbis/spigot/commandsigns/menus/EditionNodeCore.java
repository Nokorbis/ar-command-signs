package be.nokorbis.spigot.commandsigns.menus;


import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public abstract class EditionNodeCore extends EditionNode<CommandBlock> {

	public EditionNodeCore(String name, EditionMenu<CommandBlock> parent) {
		super(name, parent);
	}

	public EditionNodeCore(String name) {
		super(name);
	}

	@Override
	public void input(final Player player, final CommandBlock data, final String message, final MenuNavigationContext navigationResult) {
		try {
			final int choice = Integer.parseInt(message);
			final int page = navigationResult.getPage();
			final int numberEntriesToDisplay = getNumberEntriesToDisplay();

			if(0 < choice && choice <= numberEntriesToDisplay) {
				navigationResult.setPage(1);
				navigationResult.setCoreMenu(menus.get((page-1) * numberEntriesToDisplay + (choice-1)));
			}
			else if (choice == DONE) {
				navigationResult.setPage(1);
				navigationResult.setCoreMenu(getParent());
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
