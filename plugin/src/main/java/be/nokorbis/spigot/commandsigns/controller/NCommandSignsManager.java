package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.CommandBlockPositionChecker;
import be.nokorbis.spigot.commandsigns.controller.positionchecker.PositionCheckerFactory;
import be.nokorbis.spigot.commandsigns.menus.news.MainMenu;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.utils.Settings;

import com.google.common.cache.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


public class NCommandSignsManager {
	private final Logger logger;

	private Set<Addon> registeredAddons = new HashSet<>();
	private Set<Addon> accessibleAddons = Collections.unmodifiableSet(registeredAddons);

	private final Map<Location, Long> locationsToIds = new HashMap<>();
	private final LoadingCache<Long, CommandBlock> cache;

	private Map<UUID, NCommandSignsConfigurationManager> ncsConfigurationManagers = new HashMap<>();

	private final MainMenu mainMenu;

	public NCommandSignsManager(CommandSignsPlugin plugin) {
		this.logger = plugin.getLogger();

		this.mainMenu = new MainMenu();

		this.cache = CacheBuilder.newBuilder()
								 .maximumSize(Settings.CACHE_MAX_SIZE())
								 .expireAfterAccess(Settings.CACHE_TIME_TO_IDLE(), TimeUnit.MINUTES)
								 .removalListener(this::onCacheRemove)
								 .build(new CacheLoader<Long, CommandBlock>() {
									 public CommandBlock load(Long key) {
										 return new CommandBlock();
									 }
								 });
	}

	public void registerAddon(Addon addon) {
		this.registeredAddons.add(addon);
	}

	public Set<Addon> getRegisteredAddons() {
		return this.accessibleAddons;
	}

	public CommandBlock getCommandBlock(Long id) {
		if (id == null) {
			return null;
		}
		return this.cache.getUnchecked(id);
	}

	public CommandBlock getCommandBlock(Location location) {
		if (location == null) {
			return null;
		}
		Long id = this.locationsToIds.get(location);
		return this.getCommandBlock(id);
	}

	public boolean doesPlayerHaveAConfigurationManagerRunning(Player player) {
		return this.ncsConfigurationManagers.containsKey(player.getUniqueId());
	}

	public NCommandSignsConfigurationManager getPlayerConfigurationManager(Player player) {
		return this.ncsConfigurationManagers.get(player.getUniqueId());
	}

	public void addConfigurationManager(NCommandSignsConfigurationManager manager) {
		this.ncsConfigurationManagers.put(manager.getEditor().getUniqueId(), manager);
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
