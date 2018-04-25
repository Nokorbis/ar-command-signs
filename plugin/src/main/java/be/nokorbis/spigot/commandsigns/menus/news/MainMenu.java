package be.nokorbis.spigot.commandsigns.menus.news;

import be.nokorbis.spigot.commandsigns.api.menu.EditionNode;

import java.util.ResourceBundle;

public class MainMenu extends EditionNode
{
    private final ResourceBundle messages = ResourceBundle.getBundle("messages/menu");

    public MainMenu()
    {
        super("Main");
    }
}
