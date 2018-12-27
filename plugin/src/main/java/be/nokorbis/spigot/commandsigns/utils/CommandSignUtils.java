package be.nokorbis.spigot.commandsigns.utils;

import java.util.LinkedList;
import java.util.List;

import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

//@SuppressWarnings("WeakerAccess")
public class CommandSignUtils {



	public static List<Location> getLocationsAroundPoint(Location location, int radius) {
		List<Location> locations = new LinkedList<>();

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

        player.sendMessage(Messages.get("info.needed_permissions"));
        String permFormat = Messages.get("info.permission_format");
        int cpt = 1;

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
    }
}
