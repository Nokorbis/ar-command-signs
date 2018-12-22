package be.nokorbis.spigot.commandsigns.menus.news;


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
	public void input(Player player, CommandBlock data, String message, MenuNavigationContext navigationResult) {
		try {
			//TODO fix paging
			int choice = Integer.parseInt(message);
			// Choice 0 ? Do nothing !
			if(0 < choice && choice < menus.size()+1) {
				navigationResult.setCoreMenu(menus.get(choice));
			}
			else if(choice == menus.size()+1) {
				if(getParent() == null) {
					if(complete(player, data)) {
						navigationResult.setCoreMenu(getParent());
					}
				}
				else {
					navigationResult.setCoreMenu(getParent());
				}
			}
			else if(choice != 0) {
				throw new NumberFormatException();
			}
		}
		catch(NumberFormatException e) {
			player.sendMessage(ChatColor.RED + "Expecting a number between 0-"+(menus.size()+1)+" but got : "+message);
		}
	}
}
