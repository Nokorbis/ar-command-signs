package be.nokorbis.spigot.commandsigns.addons.items;

import be.nokorbis.spigot.commandsigns.addons.items.data.ItemsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.items.data.ItemsConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.items.data.ItemsCostsDataEditor;
import be.nokorbis.spigot.commandsigns.addons.items.data.ItemsRequirementDataEditor;
import be.nokorbis.spigot.commandsigns.api.addons.AddonBase;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditor;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ItemsAddon extends AddonBase {

    private static final String IDENTIFIER = "ncs_economy";

    private final ItemsLifecycleHooker lifecycleHooker = new ItemsLifecycleHooker(this);
    private final ItemsConfigurationDataPersister persister = new ItemsConfigurationDataPersister();

    public ItemsAddon(Plugin plugin) {
        super(plugin, IDENTIFIER, "Items");
    }

    @Override
    public AddonSubmenuHolder getSubmenus() {
        AddonSubmenuHolder holder = new AddonSubmenuHolder();

        return holder;
    }

    @Override
    public ItemsConfigurationData createConfigurationData() {
        return new ItemsConfigurationData(this);
    }

    @Override
    public ItemsConfigurationDataPersister getConfigurationDataSerializer() {
        return persister;
    }

    @Override
    public ItemsConfigurationDataPersister getConfigurationDataDeserializer() {
        return persister;
    }

    @Override
    public ItemsLifecycleHooker getLifecycleHooker() {
        return lifecycleHooker;
    }

    @Override
    public Class<? extends AddonConfigurationData> getConfigurationDataClass() {
        return ItemsConfigurationData.class;
    }

    @Override
    public Map<String, AddonConfigurationDataEditor> getDataEditors() {
        Map<String, AddonConfigurationDataEditor> editors = new HashMap<>();

        editors.put("ncs.items.requirements", new ItemsRequirementDataEditor(this));
        editors.put("ncs.items.costs", new ItemsCostsDataEditor(this));

        return editors;
    }
}
