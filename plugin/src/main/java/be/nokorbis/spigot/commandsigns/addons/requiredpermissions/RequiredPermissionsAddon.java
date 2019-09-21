package be.nokorbis.spigot.commandsigns.addons.requiredpermissions;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.data.RequiredPermissionsDataEditor;
import be.nokorbis.spigot.commandsigns.addons.requiredpermissions.menus.MenuRequiredPermissions;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


public class RequiredPermissionsAddon extends AddonBase {

	private static final String IDENTIFIER = "ncs_required_permissions";

	private final RequiredPermissionsLifecycleHooker lifecycleHooker = new RequiredPermissionsLifecycleHooker(this);
	private final MenuRequiredPermissions            editionMenu     = new MenuRequiredPermissions(this);

	private final RequiredPermissionsConfigurationDataPersister configurationDataTransformer = new RequiredPermissionsConfigurationDataPersister(this);

	public RequiredPermissionsAddon(CommandSignsPlugin plugin) {
		super(plugin, IDENTIFIER, "Required permissions");
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return lifecycleHooker;
	}

	@Override
	public AddonSubmenuHolder getSubmenus() {
		AddonSubmenuHolder holder = new AddonSubmenuHolder();
		holder.requirementSubmenus.add(editionMenu);
		return holder;
	}

	@Override
	public RequiredPermissionsConfigurationData createConfigurationData() {
		return new RequiredPermissionsConfigurationData(this);
	}

	@Override
	public Class<? extends AddonConfigurationData> getConfigurationDataClass() {
		return RequiredPermissionsConfigurationData.class;
	}

	@Override
	public RequiredPermissionsConfigurationDataPersister getConfigurationDataSerializer() {
		return configurationDataTransformer;
	}

	@Override
	public RequiredPermissionsConfigurationDataPersister getConfigurationDataDeserializer() {
		return configurationDataTransformer;
	}

	@Override
	public Map<String, AddonConfigurationDataEditor> getDataEditors() {
		Map<String, AddonConfigurationDataEditor> editors = new HashMap<>(1);
		editors.put("ncs.required_permissions", new RequiredPermissionsDataEditor(this));
		return editors;
	}
}
