package io.github.winnpixie.wpsmp.commands;

import io.github.winnpixie.wpsmp.WPSMPPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseCommand implements TabExecutor {
    private final WPSMPPlugin plugin;
    private final String name;

    public BaseCommand(WPSMPPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public WPSMPPlugin getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
