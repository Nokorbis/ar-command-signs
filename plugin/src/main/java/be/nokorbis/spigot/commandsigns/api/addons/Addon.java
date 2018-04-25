package be.nokorbis.spigot.commandsigns.api.addons;

public interface Addon
{
    String getName();

    RequirementHandler getRequirementHandler();
    CostHandler getCostHandler();
}
