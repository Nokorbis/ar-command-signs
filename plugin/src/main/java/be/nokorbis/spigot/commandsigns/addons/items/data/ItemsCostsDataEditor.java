package be.nokorbis.spigot.commandsigns.addons.items.data;

import be.nokorbis.spigot.commandsigns.addons.items.ItemsAddon;
import be.nokorbis.spigot.commandsigns.api.DisplayMessages;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditorBase;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ItemsCostsDataEditor extends AddonConfigurationDataEditorBase {

    private static final List<String> SUB_COMMANDS = Arrays.asList("add", "edit", "remove");
    private static final DisplayMessages messages = DisplayMessages.getDisplayMessages("messages/addons");

    public ItemsCostsDataEditor(ItemsAddon addon) {
        super(addon);
    }

    @Override
    public void editValue(AddonConfigurationData configurationData, List<String> args) throws CommandSignsCommandException {
        if (args.size() < 2) {
            throw new CommandSignsCommandException(messages.get("error.command.require_args"));
        }

        String subCommand = args.remove(0).toLowerCase();
        if(!SUB_COMMANDS.contains(subCommand)) {
            throw new CommandSignsCommandException(messages.get("error.command.require_list_action"));
        }

        ItemsConfigurationData data = (ItemsConfigurationData) configurationData;
        List<NCSItem> items = data.getCostsNCSItems();

        if ("remove".equals(subCommand)) {
            int index = getIndex(args.remove(0));
            if (index >= items.size()) {
                throw new CommandSignsCommandException(messages.get("error.command.index_too_large"));
            }
            items.remove(index);
        }
        else if ("edit".equals(subCommand)) {
            int index = getIndex(args.remove(0));
            if (index >= items.size()) {
                throw new CommandSignsCommandException(messages.get("error.command.index_too_large"));
            }
            NCSItem ncsItem = parseItemArgs(args);
            items.set(index, ncsItem);
        }
        else {
            NCSItem item = parseItemArgs(args);
            items.add(item);
        }
    }

    private NCSItem parseItemArgs(List<String> args) throws CommandSignsCommandException {
        if (args.size() < 2) {
            throw new CommandSignsCommandException(messages.get("error.command.require_args"));
        }
        try {
            int quantity = Integer.parseInt(args.remove(0));
            String type = args.remove(0).toUpperCase();
            Material material = Material.getMaterial(type);
            if (material == null) {
                throw new CommandSignsCommandException(messages.get("error.command.invalid_type"));
            }

            boolean handOnly = false;
            if (!args.isEmpty()) {
                handOnly = parseBooleanValue(args.remove(0));
            }

            NCSItem item = new NCSItem(material);
            item.setQuantity(quantity);
            item.setHandOnly(handOnly);

            String description = String.join(" ", args);
            List<String> descriptions = Arrays.stream(description.split("\""))
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(desc -> !desc.isEmpty())
                    .collect(Collectors.toList());

            if (args.isEmpty()) {
                return item;
            }

            item.setName(descriptions.remove(0));
            item.setLore(descriptions);

            return item;
        }
        catch (NumberFormatException e) {
            throw new CommandSignsCommandException(messages.get("error.command.require_number"));
        }
    }

    private int getIndex(String index) throws CommandSignsCommandException {
        try {
            int i = Integer.parseUnsignedInt(index);
            if (i > 0) {
                return i - 1;
            }
            return 0;
        }
        catch (NumberFormatException e) {
            throw new CommandSignsCommandException(messages.get("error.command.require_number"));
        }
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
                int limit = data.getCostsNCSItems().size();
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
