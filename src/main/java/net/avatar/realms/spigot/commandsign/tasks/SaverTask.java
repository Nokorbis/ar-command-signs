package net.avatar.realms.spigot.commandsign.tasks;

import net.avatar.realms.spigot.commandsign.data.Container;

@Deprecated
public class SaverTask implements Runnable{

	private Container container;

	public SaverTask(Container container) {
		this.container = container;
	}

	@Override
	public void run() {
		this.container.saveData();
	}

}
