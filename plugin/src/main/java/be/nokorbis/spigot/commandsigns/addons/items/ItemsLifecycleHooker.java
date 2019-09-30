package be.nokorbis.spigot.commandsigns.addons.items;

import be.nokorbis.spigot.commandsigns.addons.items.data.ItemsConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.items.data.NCSItem;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.stream.Collectors;

public class ItemsLifecycleHooker extends AddonLifecycleHookerBase {

    public ItemsLifecycleHooker(ItemsAddon addon) {
        super(addon);
    }

    @Override
    @NCSLifecycleHook
    public void onRequirementCheck(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) throws CommandSignsRequirementException {
        ItemsConfigurationData data = (ItemsConfigurationData) configurationData;

        if (!data.getRequirementNCSItems().isEmpty()) {
            for (NCSItem item : data.getRequirementNCSItems()) {
                if (!isPlayerMeetingItemRequirement(player, item)) {
                    throw new CommandSignsRequirementException (getMessage(item));
                }
            }
        }

        if (!data.getCostsNCSItems().isEmpty()) {
            for (NCSItem item : data.getCostsNCSItems()) {
                if (!isPlayerMeetingItemRequirement(player, item)) {
                    throw new CommandSignsRequirementException (getMessage(item));
                }
            }
        }
    }

    private boolean isPlayerMeetingItemRequirement(Player player, NCSItem item) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] contents = inventory.getContents();
        System.out.println(contents);
        return false;
    }

    @Override
    @NCSLifecycleHook
    public void onCostWithdraw(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {
        super.onCostWithdraw(player, configurationData, executionData);
    }

    private String getMessage(NCSItem item) {
        String msg = messages.get("usage.items.required");
        msg = msg.replace("{QUANTITY}", Integer.toString(item.getQuantity()));
        msg = msg.replace("{TYPE}", item.getType().name());

        if (item.getName() == null) {
            msg = msg.replace("{NAME}" ,"");
        }
        else {
            String format = messages.get("usage.items.name_format");
            format = format.replace("{NAME}", item.getName());
            msg = msg.replace("{NAME}", format);
        }

        if (item.getLore().isEmpty()) {
            msg = msg.replace("{LORES}", "");
        }
        else {
            String lores = item.getLore().stream().map(l -> "\"" + l + "\"").collect(Collectors.joining(","));
            String format = messages.get("usage.items.lores_format");
            format = format.replace("{LORES}", lores);
            msg = msg.replace("{LORES}", format);
        }

        return msg;
    }
}
