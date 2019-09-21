package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;

import java.util.List;

public interface CommandBlockDataEditor {

    void editValue(CommandBlock commandBlock, List<String> args) throws CommandSignsCommandException;
    List<String> onTabComplete(CommandBlock data, List<String> args);

}
