package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;

import java.util.List;

public final class CoreNameEditor extends CommandBlockDataEditorBase {

    @Override
    public void editValue(CommandBlock commandBlock, List<String> args) throws CommandSignsCommandException {
        String name = String.join(" ", args).trim();
        if (name.isEmpty()) {
            throw new CommandSignsCommandException(messages.get("error.command.set.name.invalid"));
        }
        commandBlock.setName(name);
    }

}
