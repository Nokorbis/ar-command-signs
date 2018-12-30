package be.nokorbis.spigot.commandsigns.menus;


import be.nokorbis.spigot.commandsigns.api.menu.MenuNavigationContext;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CoreAddonSubmenusHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class MainMenu extends EditionNodeCore {

    private CoreMenuRequirements requirementsMenu;
    private CoreMenuCosts costsMenu;
    private CoreMenuExecutions executionsMenu;

    public MainMenu(CoreAddonSubmenusHolder addonSubmenus) {
        super(messages.get("menu.main.title"));

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

    @Override
    protected void displayMenus(Player editor, CommandBlock data, MenuNavigationContext navigationContext) {
        clickableMessageRefresh.sendToPlayer(editor);

        if (navigationContext.getPage() == 1) {
            editor.sendMessage(messages.get("menu.block_selection")
                                       .replace("{VALUE}", getCommandBlockLocation(data)));
        }

        displaySubmenus(editor, data, navigationContext);
        displayPageNavigation(editor, navigationContext.getPage());

        clickableMessageDone.sendToPlayer(editor);
    }

    public String getCommandBlockLocation(CommandBlock data) {
        Location loc = data.getLocation();
        if (loc == null) {
            return messages.get("menu.no_block");
        }

        String format = messages.get("menu.block_format");
        return format.replace("{X}", String.valueOf(loc.getBlockX()))
                     .replace("{Y}", String.valueOf(loc.getBlockY()))
                     .replace("{Z}", String.valueOf(loc.getBlockZ()));
    }
}
