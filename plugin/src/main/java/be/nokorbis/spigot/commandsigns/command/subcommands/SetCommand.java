package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditor;
import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.controller.editor.*;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetCommand extends CommandRequiringManager {

    private final Map<String, AddonConfigurationDataEditor> addonDataEditors;
    private final Map<String, CommandBlockDataEditor> coreDataEditors;

    public SetCommand(NCommandSignsManager manager) {
        super(manager, "set", new String[0]);
        this.basePermission = "commandsign.admin.set";
        this.addonDataEditors = getAddonsDataEditors(manager);
        this.coreDataEditors = getCoreDataEditors();
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage("/commandsign set <ID> <variable> [add/remove/edit] [index] <value>");
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws CommandSignsCommandException {
        if (args.isEmpty()) {
            throw new CommandSignsCommandException(commandMessages.get("error.command_needs_arguments"));
        }

        CommandBlock commandBlock = getCommandBlock(args);
        if (commandBlock == null) {
            throw new CommandSignsCommandException(commandMessages.get("error.invalid_command_id"));
        }

        if (args.isEmpty()) {
            throw new CommandSignsCommandException(commandMessages.get("error.command.set.key_requirement"));
        }

        String key = args.remove(0);
        CommandBlockDataEditor coreEditor = coreDataEditors.get(key);
        AddonConfigurationDataEditor addonEditor = addonDataEditors.get(key);
        if (coreEditor == null && addonEditor == null) {
            throw new CommandSignsCommandException(commandMessages.get("error.command.set.key_invalid"));
        }

        if (coreEditor != null) {
            coreEditor.editValue(commandBlock, args);
        }
        else {
            AddonConfigurationData addonConfigurationData = commandBlock.getAddonConfigurationData(addonEditor.getAddon());
            addonEditor.editValue(addonConfigurationData, args);
        }

        manager.saveCommandBlock(commandBlock);
        sender.sendMessage(commandMessages.get("success.block_edited"));
        return true;
    }

    private CommandBlock getCommandBlock(List<String> args) {
        return getCommandBlock(args.remove(0));
    }

    private CommandBlock getCommandBlock(String idArg) {
        try {
            long id = Long.parseLong(idArg);
            return manager.getCommandBlock(id);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> autoComplete(CommandSender sender, List<String> args) {
        if (!sender.hasPermission(this.basePermission)) {
            return Collections.emptyList();
        }

        if (args.isEmpty()) {
            return getDefaultIDs();
        }

        String idArg = args.remove(0);
        if ("".equals(idArg.trim())) {
            return getDefaultIDs();
        }

        CommandBlock commandBlock = getCommandBlock(idArg);
        if (args.isEmpty()) {
            return Collections.singletonList(idArg);
        }
        String key = args.remove(0);
        CommandBlockDataEditor coreEditor = coreDataEditors.get(key);
        AddonConfigurationDataEditor addonEditor = addonDataEditors.get(key);
        if (coreEditor == null && addonEditor == null) {
            if (args.isEmpty()) {
                return Stream.concat(addonDataEditors.keySet().stream(), coreDataEditors.keySet().stream())
                        .filter(registeredKey -> registeredKey.contains(key))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }

        if (coreEditor != null) {
            return coreEditor.onTabComplete(commandBlock, args);
        }


        return addonEditor.onTabComplete(sender,
                commandBlock == null ? null : commandBlock.getAddonConfigurationData(addonEditor.getAddon()),
                args);
    }

    private List<String> getDefaultIDs() {
        return Arrays.asList("1", "2", "3");
    }

    private Map<String, AddonConfigurationDataEditor> getAddonsDataEditors(NCommandSignsManager manager) {
        Map<String, AddonConfigurationDataEditor> addonEditors = new HashMap<>();

        for (Addon addon : manager.getAddons()) {
            Map<String, AddonConfigurationDataEditor> dataEditors = addon.getDataEditors();
            if (dataEditors != null) {
                for (Map.Entry<String, AddonConfigurationDataEditor> entry : dataEditors.entrySet()) {
                    addonEditors.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        }

        return addonEditors;
    }

    private Map<String, CommandBlockDataEditor> getCoreDataEditors() {
        Map<String, CommandBlockDataEditor> coreEditors = new HashMap<>();

        coreEditors.put("core.name", new CoreNameEditor());
        coreEditors.put("core.disabled", new CoreDisabledEditor());
        coreEditors.put("core.activation_mode", new CoreActivationModeEditor());
        coreEditors.put("core.commands", new CoreCommandsEditor());
        coreEditors.put("core.temporary_permissions", new CoreTemporaryPermissionsEditor());
        coreEditors.put("core.timer.duration" ,new CoreTimerDurationEditor());
        coreEditors.put("core.timer.reset", new CoreTimerResetEditor());
        coreEditors.put("core.timer.cancel", new CoreTimerCancelEditor());

        return coreEditors;
    }

}
