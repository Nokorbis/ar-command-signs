package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.CommandBlockPositionChecker;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.PositionCheckerFactory;
import be.nokorbis.spigot.commandsigns.data.CommandBlockConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.data.CommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.menus.MainMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CoreAddonSubmenusHolder;
import be.nokorbis.spigot.commandsigns.utils.Settings;

import com.google.common.cache.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class NCommandSignsManager {
	private final CommandSignsPlugin plugin;
	private final Logger logger;

	private Set<Addon>                        registeredAddons = new HashSet<>();
	private NCommandSignsAddonLifecycleHolder lifecycleHolder  = new NCommandSignsAddonLifecycleHolder();
	private Set<Addon>                        accessibleAddons = Collections.unmodifiableSet(registeredAddons);

	private final Map<Location, Long>           	locationsToIds = new HashMap<>();
	private final LoadingCache<Long, CommandBlock>	cache;

	private final Map<UUID, NCommandSignsConfigurationManager> ncsConfigurationManagers = new HashMap<>();

	private final CoreAddonSubmenusHolder addonSubmenus = new CoreAddonSubmenusHolder();
	private       MainMenu                mainMenu;

	private CommandBlockConfigurationDataPersister commandBlockPersister;

	private final Map<UUID, PermissionAttachment> playersPermissions = new HashMap<>();


	public NCommandSignsManager(CommandSignsPlugin plugin) {
		this.plugin = plugin;
		this.logger = plugin.getLogger();

		this.cache = CacheBuilder.newBuilder()
								 .maximumSize(Settings.CACHE_MAX_SIZE())
								 .expireAfterAccess(Settings.CACHE_TIME_TO_IDLE(), TimeUnit.MINUTES)
								 .removalListener(this::onCacheRemove)
								 .build(new CacheLoader<Long, CommandBlock>() {
									 @Override
									 public CommandBlock load(Long key) {
										 return commandBlockPersister.load(key);
									 }
								 });

		commandBlockPersister = new JsonCommandBlockConfigurationDataPersister(plugin.getDataFolder());
	}

	public void loadIdsPerLocations() {
		CommandBlockIDLoader loader = new JsonCommandBlockIDLoader(this.plugin.getDataFolder());
		locationsToIds.putAll(loader.loadAllIdsPerLocations());
	}

	public final CommandSignsPlugin getPlugin() {
		return this.plugin;
	}

	public void registerAddon(Addon addon) {
		this.registeredAddons.add(addon);
	}

	public void initializeMenus() {
		mainMenu = new MainMenu(addonSubmenus);
	}

	public void initializeSerializers() {
		commandBlockPersister.setAddons(accessibleAddons);
	}

	public Set<Addon> getRegisteredAddons() {
		return this.accessibleAddons;
	}

	CoreAddonSubmenusHolder getAddonSubmenus() {
		return addonSubmenus;
	}

	public NCommandSignsAddonLifecycleHolder getLifecycleHolder() {
		return lifecycleHolder;
	}

	public CommandBlock getCommandBlock(final long id) {
		if (id == -1) {
			return null;
		}
		return this.cache.getUnchecked(id);
	}

	public CommandBlock getCommandBlock(Location location) {
		if (location == null) {
			return null;
		}
		long id = this.locationsToIds.getOrDefault(location, -1L);
		return this.getCommandBlock(id);
	}

	public void saveCommandBlock(CommandBlock commandBlock) {
		Location location = findLocationByID(commandBlock.getId());
		if (location != null) {
			locationsToIds.remove(location);
		}

		commandBlockPersister.saveConfiguration(commandBlock);
		locationsToIds.put(commandBlock.getLocation(), commandBlock.getId());

		cache.put(commandBlock.getId(), commandBlock);
	}

	public boolean isCommandBlock(Block block) {
		if (block == null) {
			return false;
		}

		return isCommandBlock(block.getLocation());
	}

	public boolean isCommandBlock(Location location) {
		if (location == null) {
			return false;
		}

		return locationsToIds.containsKey(location);
	}

	public Location findLocationByID(long id) {
		for (Map.Entry<Location, Long> entry : locationsToIds.entrySet()) {
			if (entry.getValue() == id) {
				return entry.getKey();
			}
		}
		return null;
	}

	public boolean doesPlayerHaveAConfigurationManagerRunning(Player player) {
		return this.ncsConfigurationManagers.containsKey(player.getUniqueId());
	}

	public NCommandSignsConfigurationManager getPlayerConfigurationManager(Player player) {
		return this.ncsConfigurationManagers.get(player.getUniqueId());
	}

	public void removeConfigurationManager(Player player) {
		this.ncsConfigurationManagers.remove(player.getUniqueId());
	}

	public void addConfigurationManager(NCommandSignsConfigurationManager manager) {
		this.ncsConfigurationManagers.put(manager.getEditor().getUniqueId(), manager);
	}

	public PermissionAttachment getPlayerPermissions(final Player player) {
		return playersPermissions.computeIfAbsent(player.getUniqueId(), (uuid) -> player.addAttachment(plugin));
	}

	public void handlePlayerExit(Player player) {
		Container.getContainer().getCopyingConfigurations().remove(player);
		ncsConfigurationManagers.remove(player.getUniqueId());
		Container.getContainer().getInfoPlayers().remove(player);
	}

	public boolean hasCommandSignsAdjacentToBlock(Block block) {
		if (this.locationsToIds.containsKey(block.getLocation())) {
			return true;
		}

		for (BlockFace blockFace : BlockFace.values()) {
			Block relativeBlock = block.getRelative(blockFace);
			if (this.locationsToIds.containsKey(relativeBlock.getLocation())) {
				if (this.isCommandBlockPosedOnBlock(relativeBlock, block, blockFace)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isCommandBlockPosedOnBlock(Block relativeCommandBlock, Block block, BlockFace face) {
		BlockData blockData = relativeCommandBlock.getBlockData();
		Material material = blockData.getMaterial();
		CommandBlockPositionChecker checker = PositionCheckerFactory.createChecker(material);
		return checker.isCommandBlockPosedOnBlock(blockData, block, face);
	}

	public MainMenu getMainMenu() {
		return mainMenu;
	}

	private void onCacheRemove(RemovalNotification<Long, CommandBlock> removalNotification) {
		CommandBlock cmdBlock = removalNotification.getValue();
		Long id = removalNotification.getKey();
		RemovalCause cause = removalNotification.getCause();
		this.logger.info(String.format("The command block : %s (%d) was removed from the cache because : %s.", cmdBlock.getName(), id, cause.name()));
	}


}
