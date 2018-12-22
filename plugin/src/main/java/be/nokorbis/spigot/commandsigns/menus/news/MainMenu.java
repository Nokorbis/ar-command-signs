package be.nokorbis.spigot.commandsigns.menus.news;


public class MainMenu extends EditionNodeCore {

    public MainMenu() {
        super("Main");
    }

    @Override
    protected void initializeSubMenus() {

        addMenu(new CoreMenuDisable(this));
        addMenu(new CoreMenuName(this));
    }

}
