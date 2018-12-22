package be.nokorbis.commandsigns.updater.models;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.Map.Entry;

public class CommandBlock
{
	private long id;
	private String name;

	private Location location;

	private boolean disabled;

	private final List<String> neededPermissions;
	private final List<String> commands;
	private final List<String> permissions;

	private Double economyPrice;

	private Integer timeBeforeExecution; // Value in seconds
	private Boolean resetOnMove;
	private Boolean cancelledOnMove;

	private long timeBetweenUsage; // Value in seconds
	private long timeBetweenPlayerUsage; // Value in seconds

	private transient long lastTimeUsed;
	private final transient Map<UUID, Long> usages;

	public CommandBlock()
	{
		// We use ArrayList because we want to remove/edit them by the index.
		this.commands = new ArrayList<>();
		this.permissions = new ArrayList<>();
		this.neededPermissions = new ArrayList<>();

		this.usages = new HashMap<>();
	}


	/* Getters and setters */

	/* Id */

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return this.id;
	}

	/* Name */

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/* Time between usage */

	public long getTimeBetweenUsage()
	{
		return this.timeBetweenUsage;
	}

	public void setTimeBetweenUsage(long timeBetweenUsage)
	{
		if (timeBetweenUsage < 0)
		{
			timeBetweenUsage = 0;
		}
		this.timeBetweenUsage = timeBetweenUsage;
	}

	/* Time between player usage */

	public long getTimeBetweenPlayerUsage()
	{
		return this.timeBetweenPlayerUsage;
	}

	public void setTimeBetweenPlayerUsage(long timeBetweenPlayerUsage)
	{
		if (timeBetweenPlayerUsage < 0)
		{
			timeBetweenPlayerUsage = 0;
		}
		this.timeBetweenPlayerUsage = timeBetweenPlayerUsage;
	}

	/* Last time used */

	public long getLastTimeUsed()
	{
		return this.lastTimeUsed;
	}

	public void refreshLastTime()
	{
		this.lastTimeUsed = System.currentTimeMillis();
	}

	/* Block */
	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(Location loc)
	{
		this.location = loc;
	}

	/* Commands */
	public void addCommand (String command)
	{
		this.commands.add(command);
	}

	public List<String> getCommands()
	{
		return this.commands;
	}

	public boolean removeCommand (int index)
	{
		if (index < 0)
		{
			return false;
		}
		if (this.commands.size() <= index)
		{
			return false;
		}
		this.commands.remove(index);
		return true;
	}

	public void editCommand (int index, String newCmd)
	{
		if (index < 0)
		{
			return;
		}
		removeCommand(index);
		this.commands.add(index, newCmd);
	}

	/* Needed requiredpermissions */
	public void addNeededPermission (String permission) {
		this.neededPermissions.add(permission);
	}

	public List<String> getNeededPermissions()
	{
		return this.neededPermissions;
	}

	public boolean removeNeededPermission(int index)
	{
		if (index < 0)
		{
			return false;
		}
		if (this.neededPermissions.size() <= index)
		{
			return false;
		}
		this.neededPermissions.remove(index);
		return true;
	}

	public void editNeededPermission(int index, String newPerm)
	{
		if (index < 0)
		{
			return;
		}
		removeNeededPermission(index);
		this.neededPermissions.add(index, newPerm);
	}

	/* Permissions */
	public void addPermission (String permission)
	{
		this.permissions.add(permission);
	}

	public List<String> getPermissions()
	{
		return this.permissions;
	}

	public boolean removePermission(int index)
	{
		if (index < 0)
		{
			return false;
		}
		if (this.permissions.size() <= index)
		{
			return false;
		}

		this.permissions.remove(index);
		return true;
	}

	public void editPermission(int index, String newPerm)
	{
		if (index < 0)
		{
			return;
		}
		removePermission(index);
		this.permissions.add(index, newPerm);
	}

	/* Timers */

	public Integer getTimeBeforeExecution()
	{
		return this.timeBeforeExecution;
	}

	public void setTimeBeforeExecution(Integer timer)
	{
		if ((timer == null) || (timer < 0))
		{
			timer = 0;
		}
		this.timeBeforeExecution = timer;
	}

	public Boolean isCancelledOnMove()
	{
		return this.cancelledOnMove;
	}

	public void setCancelledOnMove(Boolean cancel)
	{
		if (cancel == null)
		{
			cancel = false;
		}
		this.cancelledOnMove = cancel;
	}

	public Boolean isResetOnMove()
	{
		return this.resetOnMove;
	}

	public void setResetOnMove(Boolean reset)
	{
		if (reset == null)
		{
			reset = false;
		}
		this.resetOnMove = reset;
	}

	public boolean hasTimer()
	{
		return this.timeBeforeExecution >= 1;
	}

	/* Economy price */

	public Double getEconomyPrice()
	{
		return this.economyPrice;
	}

	public void setEconomyPrice(Double price)
	{
		if ((price == null) || (price < 0))
		{
			price = 0.0;
		}
		this.economyPrice = price;
	}

	public void addUsage(Player player) {
		if (this.timeBetweenPlayerUsage != 0) {
			this.usages.put(player.getUniqueId(), System.currentTimeMillis());
		}
	}

	public Long getLastTimePlayerRecentlyUsed(Player player)
	{
		if (this.timeBetweenPlayerUsage == 0)
		{
			return null;
		}

		long now = System.currentTimeMillis();
		Iterator<Entry<UUID, Long>> it = this.usages.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<UUID, Long> entry = it.next();
			if (entry.getValue() + (this.timeBetweenPlayerUsage*1000) < now)
			{
				it.remove();
			}
		}

		return this.usages.get(player.getUniqueId());
	}

	/* Disabled */

	public boolean isDisabled()
	{
		return this.disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

}
