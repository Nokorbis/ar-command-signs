package be.nokorbis.spigot.commandsigns.addons.items.data;

import be.nokorbis.spigot.commandsigns.addons.items.ItemsAddon;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditorBase;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ItemsRequirementDataEditor extends AddonConfigurationDataEditorBase {

    private static final List<String> SUB_COMMANDS = Arrays.asList("add", "edit", "remove");

    public ItemsRequirementDataEditor(ItemsAddon addon) {
        super(addon);
    }

    @Override
    public void editValue(AddonConfigurationData configurationData, List<String> args) throws CommandSignsCommandException {
        ItemsConfigurationData data = (ItemsConfigurationData) configurationData;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, AddonConfigurationData configurationData, List<String> args) {
        if (args.size() <= 1) {
            return SUB_COMMANDS;
        }

        //remove INDEX
        String subCommand = args.remove(0);
        ItemsConfigurationData data = (ItemsConfigurationData) configurationData;
        if (args.size() == 1) {
            if ("edit".equals(subCommand) || "remove".equals(subCommand)) {
                int limit = data.getRequirementNCSItems().size();
                if (limit > 0) {
                    return IntStream.range(1, limit+1).mapToObj(Integer::toString).collect(Collectors.toList());
                }
                else {
                    return Collections.emptyList();
                }
            }
        }

        if ("edit".equals(subCommand)) {
            String index = args.remove(0);
        }
        //edit INDEX QUANTITY TYPE HAND_ONLY NAME LORES
        //add QUANTITY TYPE HAND_ONLY NAME LORES

        if (args.size() == 1) {
            return IntStream.range(1, 6).mapToObj(Integer::toString).collect(Collectors.toList());
        }
        String quantity = args.remove(0);

        if (args.size() == 1) {
            String type = args.remove(0).toUpperCase();
            if (type.length() < 3) {
                return Collections.emptyList();
            }
            return Arrays.stream(Material.values())
                    .map(Enum::name)
                    .filter(m -> m.contains(type))
                    .collect(Collectors.toList());
        }

        String type = args.remove(0);
        if (args.size() == 1) {
            return Arrays.asList("Yes", "No");
        }

        return Collections.emptyList();
    }

}
