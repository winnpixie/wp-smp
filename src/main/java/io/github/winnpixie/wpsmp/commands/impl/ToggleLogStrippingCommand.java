package io.github.winnpixie.wpsmp.commands.impl;

import io.github.winnpixie.wpsmp.WPSMPPlugin;
import io.github.winnpixie.wpsmp.commands.BaseCommand;
import io.github.winnpixie.wpsmp.utilities.PlayerData;
import io.github.winnpixie.wpsmp.utilities.TextHelper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ToggleLogStrippingCommand extends BaseCommand {
    private final BaseComponent[] permittedMessage = new ComponentBuilder("You may now strip logs again!")
            .color(ChatColor.GREEN)
            .create();
    private final BaseComponent[] deniedMessage = new ComponentBuilder("You will no longer strip logs with an axe.")
            .color(ChatColor.RED)
            .append(" (FOR THIS SESSION)")
            .color(ChatColor.DARK_RED)
            .create();

    public ToggleLogStrippingCommand(WPSMPPlugin plugin) {
        super(plugin, "togglelogstripping");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.spigot().sendMessage(TextHelper.PLAYERS_ONLY);
            return true;
        }

        PlayerData playerData = getPlugin().getPlayerData(player.getUniqueId());
        playerData.canStripLogs = !playerData.canStripLogs;

        if (playerData.canStripLogs) {
            player.spigot().sendMessage(permittedMessage);
        } else {
            player.spigot().sendMessage(deniedMessage);
        }

        return true;
    }
}
