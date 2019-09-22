package be.nokorbis.spigot.commandsigns.model;

import java.util.*;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.menu.MenuEditable;
import org.bukkit.Location;


public class CommandBlock implements MenuEditable, Cloneable {

	private transient static Set<Long> usedIds = new HashSet<>();
	private transient static Long biggerUsedId = 0L;

	private long id;
	private String name;

	private Location location;
	private BlockActivationMode activationMode = BlockActivationMode.BOTH;

	private boolean disabled;

	private Map<Addon, AddonConfigurationData> addonConfigurations= new HashMap<>();
	private Map<Addon, AddonExecutionData> addonExecutions = new HashMap<>();

	private ArrayList<String> commands = new ArrayList<>();
	private ArrayList<String> temporarilyGrantedPermissions = new ArrayList<>();

	private Timer timer = new Timer();

	public CommandBlock() {
		this.setId(getFreeId());
		timer.setDuration(0);
	}

	public CommandBlock(Long id) {
		this.setId(id);
		this.timer.setDuration(0);
	}

	public static long getBiggerUsedId() {
		return biggerUsedId;
	}

	private static long getFreeId() {
		return ++biggerUsedId;
	}

	public static void addUsedIDS(Collection<Long> values) {
		usedIds.addAll(values);
		biggerUsedId = usedIds.stream().max(Long::compareTo).orElse(0L);
	}

	//region Getters and Setters

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

	public BlockActivationMode getActivationMode() {
		return activationMode;
	}

	public void setActivationMode(BlockActivationMode activationMode) {
		this.activationMode = activationMode;
	}

	/* Timers */

	public Integer getTimeBeforeExecution() {
		return this.timer.getDuration();
	}

	public void setTimeBeforeExecution(Integer timer) {
		if ((timer == null) || (timer < 0)) {
			timer = 0;
		}
		this.timer.setDuration(timer);
	}

	public Boolean isCancelledOnMove() {
		return this.timer.isCancel();
	}

	public void setCancelledOnMove(Boolean cancel) {
		if (cancel == null) {
			cancel = false;
		}
		this.timer.setCancel(cancel);
	}

	public Boolean isResetOnMove() {
		return this.timer.isReset();
	}

	public void setResetOnMove(Boolean reset) {
		if (reset == null) {
			reset = false;
		}
		this.timer.setReset(reset);
	}

	public boolean hasTimer() {
		return this.timer.getDuration() >= 1;
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

	//endregion Getters and Setters

	/* Business */

	public CommandBlock copy() {
		CommandBlock newBlock = new CommandBlock();

		if (this.name != null) {
			newBlock.name = this.name + " (copy)";
		}

		newBlock.disabled = this.disabled;

		newBlock.commands.addAll(this.commands);
		newBlock.temporarilyGrantedPermissions.addAll(this.temporarilyGrantedPermissions);
		newBlock.timer = this.timer.copy();

		for (Map.Entry<Addon, AddonConfigurationData> dataEntry : this.addonConfigurations.entrySet()) {
			AddonConfigurationData data = dataEntry.getValue();
			if (data != null) {
				AddonConfigurationData copiedData = data.copy();
				if (copiedData != null) {
					newBlock.addonConfigurations.put(dataEntry.getKey(), copiedData);
				}
			}
		}

		return newBlock;
	}

	public AddonExecutionDataObject exportExecutionData() {
		AddonExecutionDataObject object = new AddonExecutionDataObject();
		object.id = this.id;
		for (Map.Entry<Addon, AddonExecutionData> entry : addonExecutions.entrySet()) {
			Addon addon = entry.getKey();
			object.addonExecutions.put(addon, entry.getValue().copy());
		}

		return object;
	}

	public void importExecutionData(AddonExecutionDataObject object) {
		if (object.id == this.id) {
			for (Map.Entry<Addon, AddonExecutionData> entry : object.addonExecutions.entrySet()) {
				this.addonExecutions.put(entry.getKey(), entry.getValue().copy());
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }
		CommandBlock that = (CommandBlock) o;
		return id == that.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public CommandBlock clone() throws CloneNotSupportedException {
		CommandBlock clone = (CommandBlock) super.clone();
		clone.timer = this.timer.clone();
		clone.commands = new ArrayList<>(this.commands);
		clone.temporarilyGrantedPermissions = new ArrayList<>(this.temporarilyGrantedPermissions);

		clone.addonConfigurations = new HashMap<>();
		clone.addonExecutions = new HashMap<>();

		for (Map.Entry<Addon, AddonConfigurationData> entry : this.addonConfigurations.entrySet()) {
			clone.addonConfigurations.put(entry.getKey(), entry.getValue().copy());
		}

		for (Map.Entry<Addon, AddonExecutionData> entry : this.addonExecutions.entrySet()) {
			clone.addonExecutions.put(entry.getKey(), entry.getValue().copy());
		}

		return clone;
	}

	public static void deleteUsedID(long id) {
		usedIds.remove(id);
	}

	public static void reloadUsedIDs() {
		usedIds = new HashSet<>();
		biggerUsedId = 0L;
	}

	public static void setMaxIdIfBigger(long id) {
		if (id > biggerUsedId) {
			biggerUsedId = id;
		}
	}

	public static long getBiggerUsedID() {
		return biggerUsedId;
	}
}
