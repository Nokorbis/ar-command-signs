package be.nokorbis.spigot.commandsigns.addons.economy;

import be.nokorbis.spigot.commandsigns.CommandSignsPlugin;
import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;

public class EconomyAddon implements Addon
{
    private net.milkbowl.vault.economy.Economy economy = null;

    private EconomyCostHandler handler = null;

    public EconomyAddon(CommandSignsPlugin plugin)
    {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") != null)
        {
            plugin.getLogger().info("Plugin vault detected");
            this.economy = plugin.getServer().getServicesManager().load(net.milkbowl.vault.economy.Economy.class);
            if (this.economy != null)
            {
                this.handler = new EconomyCostHandler(this.economy);
                plugin.getLogger().info("Vault economy linked with command signs ! ");
            }
            else
            {
                plugin.getLogger().info("No vault economy hooked.");
            }
        }
    }

    @Override
    public String getName()
    {
        return "ncs_economy";
    }

    @Override
    public RequirementHandler getRequirementHandler()
    {
        return null;
    }

    @Override
    public CostHandler getCostHandler()
    {
        return null;
    }
}
