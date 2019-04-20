package be.nokorbis.spigot.commandsigns.controller;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;

import java.util.LinkedList;
import java.util.List;


class NCommandSignsAddonLifecycleHolder {

	final List<Addon> onStartHandlers            = new LinkedList<>();
	final List<Addon> onRequirementCheckHandlers = new LinkedList<>();
	final List<Addon> onCostWithdrawHandlers     = new LinkedList<>();
	final List<Addon> onPreExecutionHandlers     = new LinkedList<>();
	final List<Addon> onPostExecutionHandlers    = new LinkedList<>();
	final List<Addon> onCompletedHandlers        = new LinkedList<>();

}
