package net.avatar.realms.spigot.commandsign.utils;

import java.util.LinkedList;
import java.util.List;

import net.avatar.realms.spigot.commandsign.controller.Economy;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class CommandSignUtils {

	public static boolean isPlate(Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.WOOD_PLATE) 
				|| t.equals(Material.STONE_PLATE) 
				|| t.equals(Material.IRON_PLATE) 
				|| t.equals(Material.GOLD_PLATE)) {
			return true;
		}

		return false;
	}

	public static boolean isButton (Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.WOOD_BUTTON) || t.equals(Material.STONE_BUTTON)) {
			return true;
		}

		return false;
	}

	public static boolean isSign (Block block) {

		if (block == null) {
			return false;
		}

		Material t = block.getType();

		if (t.equals(Material.SIGN_POST) || t.equals(Material.WALL_SIGN)) {
			return true;
		}

		return false;
	}

	public static boolean isValidBlock (Block block) {
		return isPlate(block) || isButton(block) || isSign(block);
	}

	public static List<Location> getLocationsAroundPoint(Location location, int radius) {
		List<Location> locations = new LinkedList<Location>();

		int xorg = location.getBlockX();
		int yorg = location.getBlockY();
		int zorg = location.getBlockZ();

		for (int x = xorg - radius; x <= (xorg + radius); x++) {
			for (int y = yorg - radius; y <= (yorg + radius); y++) {
				for (int z = zorg - radius; z <= (zorg + radius); z++) {
					Block block = location.getWorld().getBlockAt(x, y, z);
					if (block.getLocation().distance(location) <= radius) {
						locations.add(block.getLocation());
					}
				}
			}
		}
		return locations;
	}

	public static void info (Player player, ChatColor c, CommandBlock cmdB) {
        player.sendMessage(c + "Id : " + cmdB.getId());
        player.sendMessage(c + Messages.NAME + " : " + ((cmdB.getName() == null)? Messages.NO_NAME : cmdB.getName()));
        player.sendMessage(c + Messages.BLOCK + " : " + cmdB.blockSummary());
        if (Economy.getEconomy() != null) {
            player.sendMessage(c + Messages.COSTS + " : " + Economy.getEconomy().format(cmdB.getEconomyPrice()));
        }
        player.sendMessage(c + Messages.NEEDED_PERMISSIONS + " :");
        int cpt = 1;
        for (String perm : cmdB.getNeededPermissions()) {
            player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + perm);
        }
        player.sendMessage(c + Messages.PERMISSIONS + " : ");
        cpt = 1;
        for (String perm :cmdB.getPermissions()) {
            player.sendMessage(ChatColor.GRAY + "---"+ cpt++ + ". " + perm);
        }
        player.sendMessage(c + Messages.COMMANDS + " : ");
        cpt = 1;
        for (String cmd : cmdB.getCommands()) {
            player.sendMessage(ChatColor.GRAY + "---" + cpt++ + ". " + cmd);
        }
        if ((cmdB.getTimer() != null) && (cmdB.getTimer() > 0)) {
            player.sendMessage(c + Messages.TIME_BEFORE_EXECUTION + " : ");
            player.sendMessage(ChatColor.GRAY + "" + cmdB.getTimer() + " " + Messages.SECONDS);
            if (cmdB.isCancelledOnMove()) {
                player.sendMessage(ChatColor.GRAY + "---" + Messages.CANCELLED_ON_MOVE);
            }
            if (cmdB.isResetOnMove()) {
                player.sendMessage(ChatColor.GRAY + "---" + Messages.RESET_ON_MOVE);
            }
        }
        if (cmdB.getTimeBetweenUsage() > 0) {
            player.sendMessage(c + Messages.TIME_BETWEEN_USAGES + " : " + cmdB.getTimeBetweenUsage());
        }
        if (cmdB.getTimeBetweenPlayerUsage() > 0) {
            player.sendMessage(c + Messages.TIME_BETWEEN_PLAYER_USAGE + " : " + cmdB.getTimeBetweenPlayerUsage());
        }
    }
}
