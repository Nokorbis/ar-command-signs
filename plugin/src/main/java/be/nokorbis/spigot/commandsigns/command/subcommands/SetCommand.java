package be.nokorbis.spigot.commandsigns.command.subcommands;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationDataEditor;
import be.nokorbis.spigot.commandsigns.command.CommandRequiringManager;
import be.nokorbis.spigot.commandsigns.controller.NCommandSignsManager;
import be.nokorbis.spigot.commandsigns.model.CommandBlock;
import be.nokorbis.spigot.commandsigns.model.CommandSignsCommandException;
import org.bukkit.command.CommandSender;

import java.util.*;
import java.util.stream.Collectors;

public class SetCommand extends CommandRequiringManager {

    private final Map<String, AddonConfigurationDataEditor> addonDataEditors;

    public SetCommand(NCommandSignsManager manager) {
        super(manager, "set", new String[0]);
        this.basePermission = "commandsign.admin.set";
        this.addonDataEditors = getAddonsDataEditor(manager);
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
        AddonConfigurationDataEditor addonDataEditor = addonDataEditors.get(key);
        if (addonDataEditor == null) {
            throw new CommandSignsCommandException(commandMessages.get("error.command.set.key_invalid"));
        }

        AddonConfigurationData addonConfigurationData = commandBlock.getAddonConfigurationData(addonDataEditor.getAddon());
        addonDataEditor.editValue(addonConfigurationData, args);
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
        System.out.println("args = " + args);
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
        AddonConfigurationDataEditor addonEditor = addonDataEditors.get(key);
        if (args.isEmpty()) {
            if (addonEditor == null) {
                return addonDataEditors.keySet().stream().filter(registeredKey -> registeredKey.contains(key)).collect(Collectors.toList());
            }
            else {
                return Collections.singletonList(key);
            }
        }

        if (addonEditor != null) {
            return addonEditor.onTabComplete(sender,
                    commandBlock == null ? null : commandBlock.getAddonConfigurationData(addonEditor.getAddon()),
                    args);
        }


        return Collections.emptyList();
    }

    private List<String> getDefaultIDs() {
        return Arrays.asList("1", "2", "3");
    }

    private Map<String, AddonConfigurationDataEditor> getAddonsDataEditor(NCommandSignsManager manager) {
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

}
