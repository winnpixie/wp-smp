package io.github.winnpixie.smp.listeners;

import io.github.winnpixie.smp.SmpCore;
import org.bukkit.event.Listener;

public class BaseListener implements Listener {
    private final SmpCore plugin;

    public BaseListener(SmpCore plugin) {
        this.plugin = plugin;
    }

    public SmpCore getPlugin() {
        return plugin;
    }
}
