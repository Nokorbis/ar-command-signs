package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;

import java.util.Collections;
import java.util.List;

public abstract class CommandBlockDataEditorBase implements CommandBlockDataEditor {

    protected final static DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/commands");

    @Override
    public List<String> onTabComplete(CommandBlock data, List<String> args) {
        return Collections.emptyList();
    }
}
