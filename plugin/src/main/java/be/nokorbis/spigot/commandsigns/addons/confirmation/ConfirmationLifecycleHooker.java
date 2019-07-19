package be.nokorbis.spigot.commandsigns.addons.confirmation;

import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfirmationConfigurationData;
import be.nokorbis.spigot.commandsigns.addons.confirmation.data.ConfirmationExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonExecutionData;
import be.nokorbis.spigot.commandsigns.api.addons.AddonLifecycleHookerBase;
import be.nokorbis.spigot.commandsigns.api.addons.NCSLifecycleHook;
import be.nokorbis.spigot.commandsigns.api.exceptions.CommandSignsRequirementException;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class ConfirmationLifecycleHooker extends AddonLifecycleHookerBase {

    public ConfirmationLifecycleHooker(ConfirmationAddon addon) {
        super(addon);
    }

    @Override
    @NCSLifecycleHook
    public void onRequirementCheck(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) throws CommandSignsRequirementException {
        final ConfirmationConfigurationData confData = (ConfirmationConfigurationData) configurationData;
        if (confData.isRequireConfirmation()) {
            final ConfirmationExecutionData execData = (ConfirmationExecutionData) executionData;
            Map<UUID, Long> warnedPlayers = execData.getWarnedPlayers();

            Long timeClicked = warnedPlayers.get(player.getUniqueId());
            final long now = System.currentTimeMillis();

            final boolean canExecute = hasClickedWithinTime(now, timeClicked);

            warnedPlayers.put(player.getUniqueId(), now);
            if (!canExecute) {
                throw new CommandSignsRequirementException(messages.get("usage.confirmation.required"));
            }
        }
    }

    private boolean hasClickedWithinTime(long now, Long timeClicked) {
        return (timeClicked != null) && (now - timeClicked < 15_000);
    }

    @Override
    @NCSLifecycleHook
    public void onPostExecution(Player player, AddonConfigurationData configurationData, AddonExecutionData executionData) {
        final ConfirmationConfigurationData confData = (ConfirmationConfigurationData) configurationData;
        if (confData.isRequireConfirmation()) {
            final ConfirmationExecutionData execData = (ConfirmationExecutionData) executionData;
            Map<UUID, Long> warnedPlayers = execData.getWarnedPlayers();
            warnedPlayers.remove(player.getUniqueId());
        }
    }
}
