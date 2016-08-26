package net.avatar.realms.spigot.commandsign.utils;

import java.util.LinkedList;
import java.util.List;

import net.avatar.realms.spigot.commandsign.controller.Economy;
import net.avatar.realms.spigot.commandsign.model.CommandBlock;
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

	public static void info (Player player, CommandBlock cmdB) {

        player.sendMessage(Messages.get("info.id_format").replace("{ID}", String.valueOf(cmdB.getId())));
        player.sendMessage(Messages.get("info.name_format").replace("{NAME}", ((cmdB.getName() == null)? Messages.get("info.no_name") : cmdB.getName())));
        player.sendMessage(Messages.get("info.block_format").replace("{POSITION}", cmdB.blockSummary()));

        if (Economy.getEconomy() != null) {
            player.sendMessage(Messages.get("info.costs_format").replace("{PRICE}", Economy.getEconomy().format(cmdB.getEconomyPrice())));
        }

        player.sendMessage(Messages.get("info.needed_permissions"));
        String permFormat = Messages.get("info.permission_format");
        int cpt = 1;
        for (String perm : cmdB.getNeededPermissions()) {
            player.sendMessage(permFormat.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm));
        }

        player.sendMessage(Messages.get("info.permissions"));
        cpt = 1;
        for (String perm :cmdB.getPermissions()) {
            player.sendMessage(permFormat.replace("{NUMBER}", String.valueOf(cpt++)).replace("{PERMISSION}", perm));
        }

        player.sendMessage(Messages.get("info.commands"));
        cpt = 1;
        String cmdFormat = Messages.get("info.command_format");
        for (String cmd : cmdB.getCommands()) {
            player.sendMessage(cmdFormat.replace("{NUMBER}", String.valueOf(cpt++)).replace("{COMMAND}", cmd));
        }

        if ((cmdB.getTimeBeforeExecution() != null) && (cmdB.getTimeBeforeExecution() > 0)) {
            player.sendMessage(Messages.get("info.time_before_execution").replace("{TIME}", String.valueOf(cmdB.getTimeBeforeExecution())));
            if (cmdB.isCancelledOnMove()) {
                player.sendMessage(Messages.get("info.cancelled_on_move"));
            }
            if (cmdB.isResetOnMove()) {
                player.sendMessage(Messages.get("info.reset_on_move"));
            }
        }
        if (cmdB.getTimeBetweenUsage() > 0) {
            player.sendMessage(Messages.get("info.time_between_usages").replace("{TIME}", String.valueOf(cmdB.getTimeBetweenUsage())));
        }
        if (cmdB.getTimeBetweenPlayerUsage() > 0) {
            player.sendMessage(Messages.get("info.time_between_player_usage").replace("{TIME}", String.valueOf(cmdB.getTimeBetweenPlayerUsage())));
        }
    }
}
