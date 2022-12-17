package io.github.winnpixie.smp.commands;

import io.github.winnpixie.smp.SmpCore;
import org.bukkit.command.CommandExecutor;

public abstract class BaseCommand implements CommandExecutor {
    private final SmpCore plugin;
    private final String name;

    public BaseCommand(SmpCore plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public SmpCore getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }
}
