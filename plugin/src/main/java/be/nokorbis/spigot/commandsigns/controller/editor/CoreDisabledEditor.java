package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CoreDisabledEditor extends CommandBlockDataEditorBase {

    @Override
    public void editValue(CommandBlock commandBlock, List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(messages.get("error.command.more_args"));
        }

        String value = args.remove(0);
        boolean newValue = parseBooleanValue(value);
        commandBlock.setDisabled(newValue);
    }

    @Override
    public List<String> onTabComplete(CommandBlock data, List<String> args) {
        if (args.size() <= 1) {
            return Arrays.asList("Yes", "No");
        }
        return Collections.emptyList();
    }
}
