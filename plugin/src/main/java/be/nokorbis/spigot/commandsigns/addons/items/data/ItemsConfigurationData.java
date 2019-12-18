package be.nokorbis.spigot.commandsigns.addons.items.data;

import be.nokorbis.spigot.commandsigns.addons.items.ItemsAddon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemsConfigurationData extends AddonConfigurationData {

    private List<NCSItem> requirementNCSItems = new ArrayList<>(1);
    private List<NCSItem> costsNCSItems = new ArrayList<>(1);

    public ItemsConfigurationData(ItemsAddon addon) {
        super(addon);
    }

    public List<NCSItem> getRequirementNCSItems() {
        return requirementNCSItems;
    }

    public List<NCSItem> getCostsNCSItems() {
        return costsNCSItems;
    }

    @Override
    public AddonConfigurationData copy() {
        ItemsConfigurationData clone = new ItemsConfigurationData((ItemsAddon) this.addon);

        clone.requirementNCSItems = new ArrayList<>(this.requirementNCSItems);
        clone.costsNCSItems = new ArrayList<>(this.costsNCSItems);

        return clone;
    }

    private String formatLine(String format, int index, NCSItem item) {
        return format
                .replace("{NUMBER}", Integer.toString(index))
                .replace("{QUANTITY}", Integer.toString(item.getQuantity()))
                .replace("{TYPE}", item.getType().name())
                .replace("{NAME}", formatName(item.getName()))
                .replace("{LORES}", formatLores(item.getLore()))
                .replace("{HAND_ONLY}", formatHandOnly(item.isHandOnly()));
    }

    private String formatName(String name) {
        if (name == null) {
            return "";
        }
        return "\"" + name + "\"";
    }

    private String formatHandOnly(boolean isHandOnly) {
        if (!isHandOnly) {
            return "";
        }
        return addonMessages.get("info.items.hand_only");
    }

    private String formatLores(List<String> lores) {
        if (lores.isEmpty()) {
            return "[]";
        }
        return lores.stream().map(String::trim)
                .filter(l -> !"".equals(l))
                .collect(Collectors.joining(",", "[", "]"));
    }

    @Override
    public void info(Player player) {
        final String format = addonMessages.get("info.items.format");
        if (!requirementNCSItems.isEmpty()) {
            player.sendMessage(addonMessages.get("info.items.requirements"));
            int i = 1;
            for (NCSItem item : requirementNCSItems) {
                player.sendMessage(formatLine(format, i++, item));
            }
        }

        if (!costsNCSItems.isEmpty()) {
            player.sendMessage(addonMessages.get("info.items.costs"));
            int i = 1;
            for (NCSItem item : costsNCSItems) {
                player.sendMessage(formatLine(format, i++, item));
            }
        }
    }
}
