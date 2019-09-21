package be.nokorbis.spigot.commandsigns.addons.confirmation;

import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfimationDataEditor;
import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfirmationConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfirmationConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfirmationExecutionData;
import be.nokorbis.spigot.commandsigns.addons.confirmation.menus.MenuConfirmation;
import be.nokorbis.spigot.commandsigns.api.addons.AddonBase;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditor;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHooker;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationAddon extends AddonBase {

    private static final String IDENTIFIER = "ncs_confirmation";

    private final ConfirmationLifecycleHooker lifecycleHooker = new ConfirmationLifecycleHooker(this);
    private final ConfirmationConfigurationDataPersister persister = new ConfirmationConfigurationDataPersister(this);

    private final MenuConfirmation menu = new MenuConfirmation(this);

    public ConfirmationAddon(Plugin plugin) {
        super(plugin, IDENTIFIER, "Confirmation");
    }

    @Override
    public AddonSubmenuHolder getSubmenus() {
        AddonSubmenuHolder holder = new AddonSubmenuHolder();
        holder.requirementSubmenus.add(menu);
        return holder;
    }

    @Override
    public AddonLifecycleHooker getLifecycleHooker() {
        return lifecycleHooker;
    }

    @Override
    public ConfirmationConfigurationData createConfigurationData() {
        return new ConfirmationConfigurationData(this);
    }

    @Override
    public Class<? extends AddonConfigurationData> getConfigurationDataClass() {
        return ConfirmationConfigurationData.class;
    }

    @Override
    public ConfirmationConfigurationDataPersister getConfigurationDataSerializer() {
        return persister;
    }

    @Override
    public ConfirmationConfigurationDataPersister getConfigurationDataDeserializer() {
        return persister;
    }

    @Override
    public ConfirmationExecutionData createExecutionData() {
        return new ConfirmationExecutionData(this);
    }

    @Override
    public Class<ConfirmationExecutionData> getExecutionDataClass() {
        return ConfirmationExecutionData.class;
    }

    @Override
    public Map<String, AddonConfigurationDataEditor> getDataEditors() {
        HashMap<String, AddonConfigurationDataEditor> editors = new HashMap<>(1);
        editors.put("ncs.confirmation", new ConfimationDataEditor(this));
        return editors;
    }
}
