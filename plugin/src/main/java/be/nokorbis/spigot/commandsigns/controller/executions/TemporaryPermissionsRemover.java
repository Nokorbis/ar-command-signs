package be.nokorbis.spigot.commandsigns.controller.executions;

import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.concurrent.Callable;


public class TemporaryPermissionsRemover implements Callable<Void> {

	private final PermissionAttachment permissionAttachment;
	private final List<String>         permissions;

	public TemporaryPermissionsRemover(final PermissionAttachment permissionsAttachment, final List<String> permissionsToRemove) {
		this.permissionAttachment = permissionsAttachment;
		this.permissions = permissionsToRemove;
	}

	@Override
	public Void call() {
		if (permissionAttachment != null) {
			for (String permission : permissions) {
				permissionAttachment.unsetPermission(permission);
			}
			permissionAttachment.remove();
		}
		return null;
	}
}