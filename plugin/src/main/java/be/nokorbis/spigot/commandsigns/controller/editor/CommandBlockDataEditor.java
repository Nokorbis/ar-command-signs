package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.List;

public interface CommandBlockDataEditor {

    void editValue(CommandBlock commandBlock, List<String> args);

}
