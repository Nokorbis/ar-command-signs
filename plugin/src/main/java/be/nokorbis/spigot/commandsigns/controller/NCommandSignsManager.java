package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.CommandBlockPositionChecker;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.PositionCheckerFactory;
import be.nokorbis.spigot.commandsigns.data.CommandBlockConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.data.CommandBlockExecutionDataPersistor;
import be.nokorbis.spigot.commandsigns.data.CommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockConfigurationDataPersister;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockExecutionDataPersister;
import be.nokorbis.spigot.commandsigns.data.json.JsonCommandBlockIDLoader;
import be.nokorbis.spigot.commandsigns.menus.MainMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandBlockPendingInteraction;
import be.nokorbis.spigot.commandsigns.model.CoreAddonSubmenusHolder;
import be.nokorbis.spigot.commandsigns.tasks.ExecuteTask;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import be.nokorbis.spigot.commandsigns.utils.Settings;

import com.google.common.cache.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NCommandSignsManager {
	private final CommandSignsPlugin plugin;
	private final Logger logger;

	private Set<Addon>                        registeredAddons = new HashSet<>();
	private NCommandSignsAddonLifecycleHolder lifecycleHolder  = new NCommandSignsAddonLifecycleHolder();
	private Set<Addon>                        accessibleAddons = Collections.unmodifiableSet(registeredAddons);

	private final Map<Location, Long>           	locationsToIds = new HashMap<>();
	private final LoadingCache<Long, CommandBlock>	cache;

	private final Map<UUID, NCommandSignsConfigurationManager> ncsConfigurationManagers = new HashMap<>();
	private final Map<UUID, CommandBlockPendingInteraction>    ncsPendingInteractions   = new HashMap<>();
	private final Map<UUID, List<ExecuteTask>>                 ncsRunningExecutors      = new HashMap<>();

	private final CoreAddonSubmenusHolder addonSubmenus = new CoreAddonSubmenusHolder();
	private       MainMenu                mainMenu;

	private CommandBlockConfigurationDataPersister configurationPersistor;
	private CommandBlockExecutionDataPersistor     executionPersistor;


	public NCommandSignsManager(CommandSignsPlugin plugin) {
		this.plugin = plugin;
		this.logger = plugin.getLogger();

		configurationPersistor = new JsonCommandBlockConfigurationDataPersister(plugin.getDataFolder());
		executionPersistor = new JsonCommandBlockExecutionDataPersister(plugin.getDataFolder());

		this.cache = CacheBuilder.newBuilder()
								 .maximumSize(Settings.CACHE_MAX_SIZE())
								 .expireAfterAccess(Settings.CACHE_TIME_TO_IDLE(), TimeUnit.MINUTES)
								 //.removalListener(this::onCacheRemove)
								 .build(new CacheLoader<Long, CommandBlock>() {
									 @Override
									 public CommandBlock load(Long key) {
										 CommandBlock co = configurationPersistor.load(key);
										 if (co != null) {
											 executionPersistor.loadExecutionData(co);
										 }
										 return co;
									 }
								 });
	}

	public void loadIdsPerLocations() {
		CommandBlockIDLoader loader = new JsonCommandBlockIDLoader(this.plugin);
		locationsToIds.putAll(loader.loadAllIdsPerLocations());
		CommandBlock.addUsedIDS(this.locationsToIds.values());
	}

	public void reloadConfigurations() {
		CommandBlock.reloadUsedIDs();
		this.cache.invalidateAll();
		this.locationsToIds.clear();
		loadIdsPerLocations();
	}

	public final CommandSignsPlugin getPlugin() {
		return this.plugin;
	}

	public void registerAddon(Addon addon) {
		this.registeredAddons.add(addon);
	}

	public Set<Addon> getAddons() {
		return this.accessibleAddons;
	}

	public void initializeMenus() {
		mainMenu = new MainMenu(addonSubmenus);
	}

	public void initializeSerializers() {
		configurationPersistor.setAddons(accessibleAddons);
		executionPersistor.setAddons(accessibleAddons);
	}

	CoreAddonSubmenusHolder getAddonSubmenus() {
		return addonSubmenus;
	}

	public NCommandSignsAddonLifecycleHolder getLifecycleHolder() {
		return lifecycleHolder;
	}

	public CommandBlockPendingInteraction getPendingInteraction(Player player) {
		return ncsPendingInteractions.get(player.getUniqueId());
	}

	public void addPendingInteraction(CommandBlockPendingInteraction interaction) {
		ncsPendingInteractions.put(interaction.player.getUniqueId(), interaction);
	}

	public void removePendingInteraction(Player player) {
		ncsPendingInteractions.remove(player.getUniqueId());
	}

	public boolean isPlayerRunningCommandBlock(final Player player, final CommandBlock commandBlock) {
		if (commandBlock == null) { return false; }

		List<ExecuteTask> tasks = this.ncsRunningExecutors.get(player.getUniqueId());
		if (tasks == null) { return false; }

		for (ExecuteTask task : tasks) {
			if (commandBlock.equals(task.getCommandBlock())) {
				return true;
			}
		}
		return false;
	}

	public void forEachRunningExecutor(final Player player, final Predicate<ExecuteTask> taskPredicate) {
		List<ExecuteTask> runningExecutors;
		synchronized (ncsRunningExecutors) {
			runningExecutors = ncsRunningExecutors.computeIfAbsent(player.getUniqueId(), (k) -> new LinkedList<>());
		}
		synchronized (runningExecutors) {
			runningExecutors.removeIf(executor -> !taskPredicate.test(executor));
		}
	}

	public void addRunningExecutor(Player player, ExecuteTask task) {
		List<ExecuteTask> runningExecutors;
		synchronized (ncsRunningExecutors) {
			runningExecutors = ncsRunningExecutors.computeIfAbsent(player.getUniqueId(), (k) -> new LinkedList<>());
		}
		synchronized (runningExecutors) {
			runningExecutors.add(task);
		}
	}

	public void removeRunningExecutor(Player player, ExecuteTask task) {
		List<ExecuteTask> runningExecutors = ncsRunningExecutors.get(player.getUniqueId());
		if (runningExecutors != null && !runningExecutors.isEmpty()) {
			synchronized (runningExecutors) {
				runningExecutors.remove(task);
			}
		}
	}

	public Stream<Long> getCommandBlockIDs() {
		return this.locationsToIds.values().stream();
	}

	public CommandBlock getCommandBlock(final long id) {
		if (id == -1L) {
			return null;
		}
		try {
			return this.cache.get(id);
		}
		catch (Exception e) {
			return null;
		}
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

		configurationPersistor.saveConfiguration(commandBlock);
		locationsToIds.put(commandBlock.getLocation(), commandBlock.getId());

		cache.put(commandBlock.getId(), commandBlock);
	}

	public void deleteCommandBlock(CommandBlock commandBlock) {
		this.configurationPersistor.delete(commandBlock.getId());
		this.executionPersistor.deleteExecutionData(commandBlock);
		this.locationsToIds.remove(commandBlock.getLocation());
		CommandBlock.deleteUsedID(commandBlock.getId());
		this.cache.invalidate(commandBlock.getId());
	}

	public int purgeCommandBlocks() {
		int cpt = 0;

		List<CommandBlock> commandBlocks = configurationPersistor.loadAllConfigurations();
		for (CommandBlock commandBlock : commandBlocks) {
			if (!CommandBlockValidator.isValidBlock(commandBlock.getLocation().getBlock())) {
				cpt++;
				deleteCommandBlock(commandBlock);
			}
		}

		return cpt;
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

	public void saveExecutionData(CommandBlock commandBlock) {
		this.executionPersistor.saveExecutionData(commandBlock);
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

	public void handlePlayerExit(Player player) {
		ncsPendingInteractions.remove(player.getUniqueId());
		ncsConfigurationManagers.remove(player.getUniqueId());
		List<ExecuteTask> removedExecutors;
		synchronized (ncsRunningExecutors) {
			removedExecutors = ncsRunningExecutors.remove(player.getUniqueId());
		}
		if (removedExecutors != null) {
			for (ExecuteTask executor : removedExecutors) {
				Bukkit.getScheduler().cancelTask(executor.getTaskId());
			}
		}
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

	public void debug (CommandSender sender) {
		if (sender != null) {
			String addons = registeredAddons.stream().map(Addon::getName).collect(Collectors.joining(", ", "Addons: ", ""));
			sender.sendMessage(addons);

			String cfgs = ncsConfigurationManagers.values().stream().map(NCommandSignsConfigurationManager::debug).collect(Collectors.joining(", ", "ConfigMgrs: ", ""));
			sender.sendMessage(cfgs);

			String inters = ncsPendingInteractions.values().stream().map(CommandBlockPendingInteraction::debug).collect(Collectors.joining(", ", "Interactions: ", ""));
			sender.sendMessage(inters);

			StringBuilder b = new StringBuilder("RunningExec: ");
			for (List<ExecuteTask> tasks : ncsRunningExecutors.values()) {
				if (tasks != null && !tasks.isEmpty()) {
					b.append(tasks.get(0).getPlayer().getName()).append("[");
					for (ExecuteTask task : tasks) {
						b.append(task.getCommandBlock().getId()).append(",");
					}
					b.setCharAt(b.length()-1, ']');
					b.append(",");
				}
			}
			b.deleteCharAt(b.length()-1);
			sender.sendMessage(b.toString());
		}
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
