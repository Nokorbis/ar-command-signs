package be.nokorbis.spigot.commandsigns.addons.permissions;

import be.nokorbis.spigot.commandsigns.api.addons.Addon;
import be.nokorbis.spigot.commandsigns.api.addons.CostHandler;
import be.nokorbis.spigot.commandsigns.api.addons.RequirementHandler;

public class PermissionsAddon implements Addon
{
    public static final String NAME = "ncs_required_permissions";

    private PermissionsRequirementHandler handler;

    public PermissionsAddon()
    {
        this.handler = new PermissionsRequirementHandler();
    }

    @Override
    public String getName()
    {
        return NAME;
    }

    @Override
    public RequirementHandler getRequirementHandler()
    {
        return handler;
    }

    @Override
    public CostHandler getCostHandler()
    {
        return null;
    }
}
