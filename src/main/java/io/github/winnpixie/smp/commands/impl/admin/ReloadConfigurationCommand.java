package io.github.winnpixie.smp.commands.impl.admin;

import io.github.winnpixie.smp.SmpCore;
import io.github.winnpixie.smp.commands.BaseCommand;
import io.github.winnpixie.smp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigurationCommand extends BaseCommand {
    private final BaseComponent[] reloadedMessage = new ComponentBuilder("The configuration has been reloaded.")
            .color(ChatColor.GREEN)
            .create();

    public ReloadConfigurationCommand(SmpCore plugin) {
        super(plugin, "reload-configuration");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.spigot().sendMessage(TextHelper.NO_PERMISSION);
            return true;
        }

        this.getPlugin().reloadConfig();
        this.getPlugin().annoc.setConfiguration(this.getPlugin().getConfig()).load();
        sender.spigot().sendMessage(reloadedMessage);
        return true;
    }
}
