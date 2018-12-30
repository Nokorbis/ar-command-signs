package be.nokorbis.spigot.commandsigns.addons.commands;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.commands.data.CommandsConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.addons.commands.menus.MenuCommands;
import be.nokorbis.spigot.commandsigns.addons.commands.menus.MenuTemporaryPermissions;
import be.nokorbis.spigot.commandsigns.api.addons.AddonBase;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHooker;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;


public class CommandsAddon extends AddonBase {

	private static final String IDENTIFIER = "ncs_commands";

	private final CommandsConfigurationDataPersister dataPersister = new CommandsConfigurationDataPersister(this);
	private final CommandsLifecycleHooker lifecycleHooker = new CommandsLifecycleHooker(this);

	private final MenuCommands menuCommands = new MenuCommands(this);
	private final MenuTemporaryPermissions menuTemporaryPermissions = new MenuTemporaryPermissions(this);

	public CommandsAddon(CommandSignsPlugin plugin) {
		super(plugin, IDENTIFIER, "Commands");
	}

	@Override
	public AddonLifecycleHooker getLifecycleHooker() {
		return lifecycleHooker;
	}

	@Override
	public AddonSubmenuHolder getSubmenus() {
		AddonSubmenuHolder holder = new AddonSubmenuHolder();
		holder.executionSubmenus.add(menuTemporaryPermissions);
		holder.executionSubmenus.add(menuCommands);
		return holder;
	}

	@Override
	public CommandsConfigurationData createConfigurationData() {
		return new CommandsConfigurationData(this);
	}

	@Override
	public JsonSerializer<CommandsConfigurationData> getConfigurationDataSerializer() {
		return dataPersister;
	}

	@Override
	public JsonDeserializer<CommandsConfigurationData> getConfigurationDataDeserializer() {
		return dataPersister;
	}
}
