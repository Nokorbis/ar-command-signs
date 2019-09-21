package be.nokorbis.spigot.commandsigns.addons.cooldowns.data;

import be.nokorbis.spigot.commandsigns.addons.cooldowns.CooldownAddon;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditorBase;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class GlobalCooldownDataEditor extends AddonConfigurationDataEditorBase {

    private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/addons");

    public GlobalCooldownDataEditor(final CooldownAddon addon) {
        super(addon);
    }

    @Override
    public void editValue(final AddonConfigurationData configurationData, final List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(messages.get("error.command.require_args"));
        }

        try {
            String value = args.remove(0);
            long cooldown = Long.parseLong(value);
            CooldownConfigurationData data = (CooldownConfigurationData) configurationData;
            data.setGlobalCooldown(cooldown);
        }
        catch (NumberFormatException e) {
            throw new CommandSignsCommandException(messages.get("error.command.require_number"));
        }
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, AddonConfigurationData configurationData, final List<String> args) {
        return Arrays.asList("60", "300", "600", "3600", "86400");
    }
}
