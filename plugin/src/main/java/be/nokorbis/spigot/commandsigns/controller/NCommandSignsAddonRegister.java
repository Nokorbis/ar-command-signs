package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.api.AddonRegister;
import be.nokorbis.spigot.commandsigns.api.addons.*;
import be.nokorbis.spigot.commandsigns.api.menu.AddonEditionMenu;
import be.nokorbis.spigot.commandsigns.api.menu.AddonSubmenuHolder;
import be.nokorbis.spigot.commandsigns.api.menu.EditionMenu;
import be.nokorbis.spigot.commandsigns.model.CoreAddonSubmenusHolder;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;


public class NCommandSignsAddonRegister implements AddonRegister {

	private final NCommandSignsManager manager;

	public NCommandSignsAddonRegister(NCommandSignsManager manager) {
		this.manager = manager;
	}

	public void registerAddon(final Addon addon) {
		if (addon != null) {
			this.manager.registerAddon(addon);
			registerLifecycle(addon);
			registerSubmenus(addon);
		}
	}

	private void registerSubmenus(Addon addon) {
		AddonSubmenuHolder addonSubmenus = addon.getSubmenus();
		if (addonSubmenus!= null) {
			CoreAddonSubmenusHolder registeredSubmenus = this.manager.getAddonSubmenus();

			if (!addonSubmenus.requirementSubmenus.isEmpty()) {
				List<AddonEditionMenu> menus = registeredSubmenus.requirementSubmenus.computeIfAbsent(addon, (a) -> new LinkedList<>());
				menus.addAll(addonSubmenus.requirementSubmenus);
			}

			if (!addonSubmenus.costSubmenus.isEmpty()) {
				List<AddonEditionMenu> menus = registeredSubmenus.costSubmenus.computeIfAbsent(addon, (a) -> new LinkedList<>());
				menus.addAll(addonSubmenus.costSubmenus);
			}

			if (!addonSubmenus.executionSubmenus.isEmpty()) {
				List<AddonEditionMenu> menus = registeredSubmenus.executionSubmenus.computeIfAbsent(addon, (a) -> new LinkedList<>());
				menus.addAll(addonSubmenus.executionSubmenus);
			}
		}
	}

	private void registerLifecycle(Addon addon) {
		if (addon.shouldAddonBeHooked()) {
			final NCommandSignsAddonLifecycleHolder lifecycleHolder = manager.getLifecycleHolder();
			final AddonLifecycleHooker lifecycleHooker = addon.getLifecycleHooker();

			final Class<? extends AddonLifecycleHooker> hookerClass = lifecycleHooker.getClass();
			final Method[] declaredMethods = hookerClass.getDeclaredMethods();
			for (final Method method : declaredMethods) {
				if (method.isAnnotationPresent(NCSLifecycleHook.class)) {
					final String name = method.getName();
					if ("onStarted".equals(name)) {
						lifecycleHolder.onStartHandlers.add(addon);
					}
					else if ("onRequirementCheck".equals(name)) {
						lifecycleHolder.onRequirementCheckHandlers.add(addon);
					}
					else if ("onCostWithdraw".equals(name)) {
						lifecycleHolder.onCostWithdrawHandlers.add(addon);
					}
					else if ("onPreExecution".equals(name)) {
						lifecycleHolder.onPreExecutionHandlers.add(addon);
					}
					else if ("onExecution".equals(name)) {
						lifecycleHolder.onExecutionHandlers.add(addon);
					}
					else if ("onPostExecution".equals(name)) {
						lifecycleHolder.onPostExecutionHandlers.add(addon);
					}
					else if ("onCompleted".equals(name)) {
						lifecycleHolder.onCompletedHandlers.add(addon);
					}
				}
			}
		}
	}
}
