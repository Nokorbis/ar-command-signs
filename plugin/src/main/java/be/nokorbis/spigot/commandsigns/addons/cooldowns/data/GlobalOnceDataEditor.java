package be.nokorbis.spigot.commandsigns.addons.cooldowns.data;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownAddon;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditorBase;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class GlobalOnceDataEditor extends AddonConfigurationDataEditorBase {

    private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/addons");

    public GlobalOnceDataEditor(final CooldownAddon addon) {
        super(addon);
    }

    @Override
    public void editValue(AddonConfigurationData configurationData, List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(messages.get("error.commmand.require_args"));
        }

        String value = args.remove(0);
        boolean newValue = parseBooleanValue(value);
        CooldownConfigurationData data = (CooldownConfigurationData) configurationData;
        data.setGlobalOnlyOnce(newValue);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, AddonConfigurationData configurationData, List<String> args) {
        return Arrays.asList("Yes", "No");
    }
}
