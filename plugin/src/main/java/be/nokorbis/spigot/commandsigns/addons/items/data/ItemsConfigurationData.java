package be.nokorbis.spigot.commandsigns.addons.items.data;

import be.nokorbis.spigot.commandsigns.addons.items.ItemsAddon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;

import java.util.ArrayList;
import java.util.List;

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

}
