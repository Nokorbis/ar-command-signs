package be.nokorbis.spigot.commandsigns.controller.executions;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.concurrent.Callable;


public class TemporaryPermissionsGranter implements Callable<PermissionAttachment> {

	private Plugin       plugin;
	private Player       player;
	private List<String> permissions;

	public TemporaryPermissionsGranter(Plugin plugin, Player player, List<String> permissions) {
		this.plugin = plugin;
		this.player = player;
		this.permissions = permissions;
	}

	@Override
	public PermissionAttachment call()  {
		PermissionAttachment permissionAttachment = player.addAttachment(plugin);
		for (String permission : permissions) {
			if (!player.hasPermission(permission)) {
				permissionAttachment.setPermission(permission, true);
			}
		}
		return permissionAttachment;
	}
}

