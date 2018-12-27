package be.nokorbis.spigot.commandsigns.addons.commands;

import org.bukkit.permissions.PermissionAttachment;

import java.util.List;


public class TemporaryPermissionsRemover implements Runnable {

	private final PermissionAttachment permissionAttachment;
	private final List<String>         permissions;

	public TemporaryPermissionsRemover(final PermissionAttachment permissionsAttachment, final List<String> permissionsToRemove) {
		this.permissionAttachment = permissionsAttachment;
		this.permissions = permissionsToRemove;
	}

	@Override
	public void run() {
		if (permissionAttachment != null) {
			for (String permission : permissions) {
				permissionAttachment.unsetPermission(permission);
			}
		}
	}
}