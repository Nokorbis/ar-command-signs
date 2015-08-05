package net.avatar.realms.spigot.commandsign.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.avatar.realms.spigot.commandsign.CommandSign;

public class BlockData {
	
	private String worldUuid;
	
	private int x;
	private int y;
	private int z;
	
	private Material type;
	
	public BlockData() {
		
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public Material getType() {
		return type;
	}

	public void setType(Material type) {
		this.type = type;
	}
	
	public String getWorldUuid() {
		return worldUuid;
	}

	public void setWorldUuid(String worlduuid) {
		this.worldUuid = worlduuid;
	}
	
	public static BlockData transform (Block block) {
		BlockData data = new BlockData();
		
		data.setWorldUuid(block.getWorld().getUID().toString());
		
		data.setX(block.getX());
		data.setY(block.getY());
		data.setZ(block.getZ());
		
		data.setType(block.getType());
		return data;
	}
	
	public static Block transform (BlockData data) {
		Block block = null;
		World world = Bukkit.getServer().getWorld(UUID.fromString(data.getWorldUuid()));
		if (world == null) {
			CommandSign.getPlugin().getLogger().severe("World with UUID : " + data.getWorldUuid() + " cannot be found");
			return null;
		}
		Location loc = new Location(world, data.getX(), data.getY(), data.getZ());
		block = loc.getBlock();
		
		if (!block.getType().equals(data.getType())) {
			CommandSign.getPlugin().getLogger().warning("A Command sign has been loaded at " + block.getX() + ":"+ block.getY()+":" + block.getZ()+" with a wrong type.");
		}
		
		return block;
	}

}
