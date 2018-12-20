package be.nokorbis.spigot.commandsigns.menus.news;

import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;


public class MainMenu extends EditionNode<CommandBlock> {

    public MainMenu() {
        super("Main");
    }

    @Override
    protected void initializeSubMenus() {

        addMenu(new CoreMenuDisable(this));
        addMenu(new CoreMenuName(this));
    }
}
