package be.nokorbis.spigot.commandsigns.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class DisplayMessages
{
    private static final Map<String, DisplayMessages> loadedMessages = new HashMap<>(); //cache system to avoid reloading too many times

    public static final DisplayMessages getDisplayMessages(String path)
    {
        return new DisplayMessages(path);
    }

    private Properties messages;

    private DisplayMessages(String path)
    {

    }
}
