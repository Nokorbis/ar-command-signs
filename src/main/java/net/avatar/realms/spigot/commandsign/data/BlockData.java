package net.avatar.realms.spigot.commandsign.data;

import org.bukkit.Material;

public class BlockData {
	
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
	
	

}
