package be.nokorbis.spigot.commandsigns.model;

import java.util.*;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsException;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import be.nokorbis.spigot.commandsigns.utils.CommandBlockValidator;
import org.bukkit.Location;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;


public class CommandBlock implements MenuEditable
{
	private transient static Set<Long> usedIds = new HashSet<>();
	private transient static Long biggerUsedId = 0L;

	private long id;
	private String name;

	private Location location;

	private boolean disabled;

	private Map<Addon, AddonConfigurationData> addonConfigurations= new HashMap<>();
	private Map<Addon, AddonExecutionData> addonExecutions = new HashMap<>();

	private ArrayList<String> commands = new ArrayList<>();
	private ArrayList<String> temporarilyGrantedPermissions = new ArrayList<>();

	private Integer timeBeforeExecution; // Value in seconds
	private Boolean resetOnMove;
	private Boolean cancelledOnMove;

	public CommandBlock() {
		this.setTimeBeforeExecution(0);
		this.resetOnMove = false;
		this.cancelledOnMove = false;

		this.setId(getFreeId());
	}

	public CommandBlock(Long id) {
		this.setTimeBeforeExecution(0);
		this.resetOnMove = false;
		this.cancelledOnMove = false;

		if (usedIds.contains(id)) {
			CommandSignsPlugin.getPlugin().getLogger().warning("A strange error occured : It seems that the registered id (" + id + ") is already in used... Getting a new one...");
			id = getFreeId();
		}
		this.setId(id);
	}

	public static long getBiggerUsedId() {
		return biggerUsedId;
	}

	private static long getFreeId() {
		for (long i = 0; i <= biggerUsedId; i++) {
			if (!usedIds.contains(i)) {
				return i;
			}
		}
		return ++biggerUsedId;
	}

	/* Getters and setters */

	/* Id */

	private void setId(long id) {
		this.id = id;
		usedIds.add(id);

		if (id > biggerUsedId) {
			biggerUsedId = id;
		}
	}

	public long getId() {
		return this.id;
	}

	/* Configuration data */

	public void setAddonConfigurationData(final Addon addon, final AddonConfigurationData configurationData) {
		this.addonConfigurations.put(addon, configurationData);
	}

	public void setAddonExecutionData(final Addon addon, final AddonExecutionData executionData) {
		this.addonExecutions.put(addon, executionData);
	}

	public AddonConfigurationData getAddonConfigurationData(final Addon addon) {
		if (addon == null) {
			return null;
		}
		return this.addonConfigurations.computeIfAbsent(addon, Addon::createConfigurationData);
	}

	public AddonExecutionData getAddonExecutionData(final Addon addon) {
		if (addon == null) {
			return null;
		}
		return this.addonExecutions.computeIfAbsent(addon, Addon::createExecutionData);
	}

	/* Name */

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* Block */
	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location loc) {
		this.location = loc;
	}


	/* Timers */

	public Integer getTimeBeforeExecution() {
		return this.timeBeforeExecution;
	}

	public void setTimeBeforeExecution(Integer timer) {
		if ((timer == null) || (timer < 0)) {
			timer = 0;
		}
		this.timeBeforeExecution = timer;
	}

	public Boolean isCancelledOnMove() {
		return this.cancelledOnMove;
	}

	public void setCancelledOnMove(Boolean cancel) {
		if (cancel == null) {
			cancel = false;
		}
		this.cancelledOnMove = cancel;
	}

	public Boolean isResetOnMove() {
		return this.resetOnMove;
	}

	public void setResetOnMove(Boolean reset) {
		if (reset == null) {
			reset = false;
		}
		this.resetOnMove = reset;
	}

	public boolean hasTimer() {
		return this.timeBeforeExecution >= 1;
	}


	/* Disabled */

	public boolean isDisabled() {
		return this.disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/* Commands */

	public ArrayList<String> getCommands() {
		return commands;
	}

	public ArrayList<String> getTemporarilyGrantedPermissions() {
		return temporarilyGrantedPermissions;
	}

	/* Business */

	public CommandBlock copy() {
		CommandBlock newBlock = new CommandBlock();

		if (this.hasTimer()) {
			newBlock.setTimeBeforeExecution(this.timeBeforeExecution);
		}

		if (this.cancelledOnMove != null && this.cancelledOnMove) {
			newBlock.setCancelledOnMove(true);
		}

		if (this.resetOnMove != null && this.resetOnMove) {
			newBlock.setResetOnMove(true);
		}

		return newBlock;
	}

	public boolean validate() throws CommandSignsException {
		if (this.location == null) {
			throw new CommandSignsException("A command block is invalid due to null location. You may think about deleting it, its id : " + this.id);
		}
		if (!CommandBlockValidator.isValidBlock(this.location.getBlock())) {
			throw new CommandSignsException("A command block is invalid due to an invalid type (must be sign, plate or button). You may think about deleting it, its id : " + this.id);
		}
		return true;
	}

	public String blockSummary() {
		if (this.location == null) {
			return "";
		}
		return this.location.getBlock().getType() + " #" + this.location.getX() + ":" + this.location.getZ() + "(" + this.location.getY() + ")";
	}

	public static void reloadUsedIDs() {
		usedIds = new HashSet<>();
		biggerUsedId = 0L;
	}

	public static void reloadUsedID(long id) {
		usedIds.remove(id);
	}
}
