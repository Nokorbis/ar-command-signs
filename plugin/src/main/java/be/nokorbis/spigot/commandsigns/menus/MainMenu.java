package be.nokorbis.spigot.commandsigns.menus;


import be.nokorbis.spigot.commandsigns.model.CoreAddonSubmenusHolder;


public class MainMenu extends EditionNodeCore {

    private CoreMenuRequirements requirementsMenu;
    private CoreMenuCosts costsMenu;
    private CoreMenuExecutions executionsMenu;

    public MainMenu(CoreAddonSubmenusHolder addonSubmenus) {
        super("Main");

        this.requirementsMenu.setSubMenusByAddon(addonSubmenus.requirementSubmenus);
        this.costsMenu.setSubMenusByAddon(addonSubmenus.costSubmenus);
        this.executionsMenu.setSubMenusByAddon(addonSubmenus.executionSubmenus);
    }

    @Override
    protected void initializeSubMenus() {

        this.requirementsMenu = new CoreMenuRequirements(this);
        this.costsMenu = new CoreMenuCosts(this);
        this.executionsMenu = new CoreMenuExecutions(this);

        addMenu(new CoreMenuDisable(this));
        addMenu(new CoreMenuName(this));
        addMenu(new CoreMenuTimer(this));
        addMenu(this.requirementsMenu);
        addMenu(this.costsMenu);
        addMenu(this.executionsMenu);
    }

}
