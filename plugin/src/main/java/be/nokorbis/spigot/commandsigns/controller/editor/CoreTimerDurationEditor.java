package be.nokorbis.spigot.commandsigns.controller.editor;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CoreTimerDurationEditor extends CommandBlockDataEditorBase {

    @Override
    public void editValue(CommandBlock data, List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(messages.get("error.command.more_args"));
        }

        try {
            int duration = Integer.parseInt(args.remove(0));
            data.setTimeBeforeExecution(duration);
        }
        catch (NumberFormatException e) {
            throw new CommandSignsCommandException(messages.get("error.command.number_requirement"));
        }
    }

    @Override
    public List<String> onTabComplete(CommandBlock data, List<String> args) {
        if (args.size() <= 1) {
            return Arrays.asList("5", "10", "60", "3600");
        }
        return Collections.emptyList();
    }
}
