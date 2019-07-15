package be.nokorbis.spigot.commandsigns.addons.confirmation.data;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.AddonConfigurationData;
import org.bukkit.entity.Player;

public class ConfirmationConfigurationData extends AddonConfigurationData {

    private boolean requireConfirmation = false;

    public ConfirmationConfigurationData(Addon addon) {
        super(addon);
    }

    public boolean isRequireConfirmation() {
        return requireConfirmation;
    }

    public void setRequireConfirmation(boolean requireConfirmation) {
        this.requireConfirmation = requireConfirmation;
    }

    @Override
    public AddonConfigurationData copy() {
        ConfirmationConfigurationData copy = new ConfirmationConfigurationData(addon);
        copy.requireConfirmation = this.requireConfirmation;
        return copy;
    }

    @Override
    public void info(Player player) {
        if (requireConfirmation) {
            player.sendMessage(addonMessages.get("info.require_confirmation"));
        }
        super.info(player);
    }
}
