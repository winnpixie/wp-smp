package io.github.winnpixie.wpsmp.listeners;

import io.github.winnpixie.wpsmp.WPSMPPlugin;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {
    private final WPSMPPlugin plugin;

    public BaseListener(WPSMPPlugin plugin) {
        this.plugin = plugin;
    }

    public WPSMPPlugin getPlugin() {
        return plugin;
    }
}
