package io.github.winnpixie.smp.commands;

import io.github.winnpixie.smp.SmpCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BaseCommand implements TabExecutor {
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

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
