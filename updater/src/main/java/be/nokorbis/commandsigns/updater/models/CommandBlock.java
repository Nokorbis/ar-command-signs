package be.nokorbis.commandsigns.updater.models;

import java.util.*;

public class CommandBlock
{
	public long id;
	public String name;

	public Location location;

	public boolean disabled = false;

	public final List<String> neededPermissions = new ArrayList<>();
	public final List<String> commands  = new ArrayList<>();
	public final List<String> permissions = new ArrayList<>();

	public Double economyPrice = 0.0;

	public Integer timeBeforeExecution; // Value in seconds
	public Boolean resetOnMove;
	public Boolean cancelledOnMove;

	public long timeBetweenUsage; // Value in seconds
	public long timeBetweenPlayerUsage; // Value in seconds

}
